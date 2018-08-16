package com.fih.idx.deriklibrary.jakku;

import android.util.Log;

import com.fih.idx.deriklibrary.custom.Consumer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TcpClient {

    private static final String TAG = "TcpClient";
    private Socket socket;
    private BufferedReader bread = null;
    private BufferedWriter bwrite = null;
    private String mIp;
    private int mPort;

    private Consumer<String> success;
    private Consumer<String> error;

    public TcpClient(String ip, int port) {
        // TODO Auto-generated constructor stub
        if (ip == null || port < 1024) {
            throw new IllegalArgumentException("ip 或 port参数错误");
        }
        mIp = ip;
        mPort = port;
    }

    /**
     * 设置数据监听器
     * @param success 成功的函数式接口
     * @param error 错误的函数式接口
     */
    public void setDataListener(Consumer<String> success,Consumer<String> error){
        this.success = success;
        this.error = error;
    }

    /**
     * 设置数据监听器
     * @param success 成功的函数式接口
     */
    public void setDataListener(Consumer<String> success){
        this.success = success;
    }

    /**
     * 发送一条TCP消息
     * @param msg 消息内容
     */
    public void sendMsg(final String msg) {
        new Thread(() -> {
            try {
                if (socket == null || !socket.isConnected()) {
                    socket = new Socket(mIp, mPort);
                    bwrite = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
                    bread = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
                }

                Log.d(TAG, "run: write length=" + msg.length());
                bwrite.write(msg + "#eof#");
                bwrite.flush();

                char[] buffer = new char[1024];
                int read;
                StringBuilder stringBuffer = new StringBuilder();
                while ((read = bread.read(buffer)) != -1) {
                    stringBuffer.append(buffer, 0, read);
                    if (stringBuffer.toString().contains("#eof#")) {
                        break;
                    }
                }
                String result = stringBuffer.toString();
                if (result.length() >= 5) {
                    result = result.substring(0, result.length() - 5);
                }
                Log.d(TAG, "run: receive length=" + result.length());

                if (success != null) {
                    success.accept(result);
                }
            } catch (Exception e) {
                // TODO: handle exception
                if (error != null) {
                    error.accept("TCP client error");
                }
                e.printStackTrace();
            }
        }).start();
    }

}
