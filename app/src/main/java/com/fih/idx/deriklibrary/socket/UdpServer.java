package com.fih.idx.deriklibrary.socket;

import com.fih.idx.deriklibrary.custom.BiConsumer;
import com.fih.idx.deriklibrary.custom.Consumer;
import com.fih.idx.deriklibrary.utils.Log;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by derik on 18-5-18.
 */

public class UdpServer {

    private static final String TAG = "UdpServer";
    /**
     * 指定发送时的广播组
     */
    private InetAddress toGroup;
    /**
     * 指定接收时的广播组
     */
    private InetAddress fromGroup;
    /**
     * 指定发送，接收时port口
     */
    private int port;
    /**
     * 多播套接字，用于发送多播信息
     */
    private MulticastSocket sendSocket = null;
    /**
     * 多播套接字，用于接收多播信息
     */
    private MulticastSocket receiveSocket = null;
    /**
     * 多播信息接收监听器
     */
    private BiConsumer<String, String> success;
    private Consumer<String> error;
    private boolean isRunning = false;

    /**
     * 用于消息发送，避免多次创建线程
     */
    private final Executor mExecutor = Executors.newSingleThreadExecutor();

    /**
     * 构造一个UDP Server。sendIp和receiveIp不能相同
     *
     * @param sendIp    发送的组IP
     * @param receiveIp 接收的组IP
     * @param port      信息收发port口
     */
    public UdpServer(String sendIp, String receiveIp, int port) {
        if (sendIp.equals(receiveIp)) {
            throw new IllegalArgumentException("Illegal argument.");
        }
        try {
            this.port = port;
            toGroup = InetAddress.getByName(sendIp);
            fromGroup = InetAddress.getByName(receiveIp);
            sendSocket = new MulticastSocket(port);
            receiveSocket = new MulticastSocket(port);
            sendSocket.setTimeToLive(1);
            sendSocket.joinGroup(toGroup);
            receiveSocket.setTimeToLive(1);
            receiveSocket.joinGroup(fromGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置监听器，接收的UDP广播信息，将在其中响应。
     *
     * @param success 成功回调接口
     * @param error 错误回调接口
     */
    public void setDataListener(BiConsumer<String, String> success, Consumer<String> error) {
        this.success = success;
        this.error = error;
    }

    /**
     * 设置监听器，接收的UDP广播信息，只响应成功时的消息。
     *
     * @param success 成功回调接口
     */
    public void setDataListener(BiConsumer<String, String> success) {
        this.success = success;
    }

    /**
     * 向目标组发送一条UDP信息
     *
     * @param msg 发送的消息
     */
    public void sendMsg(final String msg) {
        if (isRunning && msg != null) {
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        DatagramPacket packet;
                        byte data[] = msg.getBytes("utf-8");
                        packet = new DatagramPacket(data, data.length, toGroup, port);
                        sendSocket.send(packet);
                        Log.d(TAG, "run: send by udp, msg=" + msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    /**
     * 开启UDP服务，开始接收UDP消息，并且可以发送消息
     */
    public synchronized void startServer() {
        Log.d(TAG, "startServer: ");
        if (!isRunning) {
            isRunning = true;
            new Thread(new UdpServerRunnable()).start();
        }
    }

    /**
     * 关闭UDP服务，结束UDP消息接收，并且无法发送消息
     */
    public synchronized void stopServer() {
        Log.d(TAG, "stopServer: ");
        isRunning = false;
    }

    /**
     * 返回当前UDP Server的状态
     *
     * @return boolean. true，正在运行，false，未运行
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * UDP消息接收服务，是一个循环处理线程。在接收到一条消息之前，处于阻塞状态。
     */
    private class UdpServerRunnable implements Runnable {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    Log.d(TAG, "run: ready");
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length, fromGroup, port);
                    receiveSocket.receive(packet);
                    String ip = packet.getAddress().getHostAddress();
                    String message;
                    message = new String(packet.getData(), 0, packet.getLength(), "utf-8");
                    if (!message.isEmpty()) {
                        if (success != null && isRunning) {
                            success.accept(ip, message);
                        }
                    }
                    Log.d(TAG, "run:from udp host=" + ip + ", from udp port=" + packet.getPort() + ", receive message=" + message);
                } catch (Exception e) {
                    isRunning = false;
                    receiveSocket.close();
                    e.printStackTrace();
                    if (error != null) {
                        error.accept("udp error");
                    }
                }
            }
        }
    }

}
