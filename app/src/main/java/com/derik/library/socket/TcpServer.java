package com.derik.library.socket;

import com.derik.library.view.BiConsumer;
import com.derik.library.view.Consumer;
import com.derik.library.utils.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by derik on 18-6-4.
 */

public class TcpServer implements Runnable {

    private static final String TAG = "TcpServer";
    private static final TcpServer instance = new TcpServer();
    private final List<Socket> sockets = new ArrayList<>();
    private Thread mThread;
    private Consumer<String> successListener;
    private Consumer<String> errorListener;
    private BiConsumer<String, Integer> actionListener;
    private boolean isRunning = false;

    public static TcpServer getInstance() {
        return instance;
    }

    /**
     * 数据监听器
     *
     * @param success 成功时，函数式接口
     * @param error   错误时，函数式接口
     */
    public void setDataListener(Consumer<String> success, Consumer<String> error) {
        this.successListener = success;
        this.errorListener = error;
    }

    /**
     * 数据监听器
     *
     * @param success 成功时，函数式接口
     */
    public void setDataListener(Consumer<String> success) {
        this.successListener = success;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            if (actionListener != null) {
                actionListener.accept(serverSocket.getInetAddress().getHostAddress(),
                        serverSocket.getLocalPort());
            }
            while (true) {
                Log.d(TAG, "run: ThreadId=" + Thread.currentThread().getId());
                Socket socket = serverSocket.accept();
                sockets.add(socket);
                Log.d(TAG, "run: socket size=" + sockets.size());
                new Thread(new TcpServerRunnable(socket)).start();
                if (!isRunning) {
                    serverSocket.close();
                    break;
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.d(TAG, "TCP server run: finally");
        }
    }

    /**
     * 开启TCP服务，如果已经开启，不进行处理
     *
     * @param actionListener 状态监听器，开启成功后，将对应信息返回给接口方法
     */
    public void startServer(BiConsumer<String, Integer> actionListener) {
        Log.d(TAG, "startServer: ");
        if (!isRunning) {
            isRunning = true;
            this.actionListener = actionListener;
            mThread = new Thread(this);
            mThread.start();
        }
    }

    /**
     * 停止TCP服务
     */
    public void stopServer() {
        Log.d(TAG, "stopServer: ");
        isRunning = false;
        if (mThread != null) {
            mThread = null;
        }
    }

    /**
     * TCP服务线程
     */
    public class TcpServerRunnable implements Runnable {
        private Socket socket;
        private BufferedReader bread;
        private BufferedWriter bwrite;

        TcpServerRunnable(Socket socket) throws IOException {
            this.socket = socket;
            bread = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
            bwrite = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
        }

        @Override
        public void run() {
            try {
                while (isRunning) {
                    StringBuilder stringBuilder = new StringBuilder();
                    char[] buffer = new char[2048];
                    int length;
                    while ((length = bread.read(buffer)) != -1) {
                        stringBuilder.append(buffer, 0, length);
                        if (stringBuilder.toString().contains("#eof#")) {
                            break;
                        }
                    }

                    String result = stringBuilder.toString();
                    if (result.length() >= 5) {
                        result = result.substring(0, result.length() - 5);
                    }
                    Log.d(TAG, "run: receive length=" + result.length());

                    if (successListener != null) {
                        successListener.accept(result);
                    }
                    String reply = "TCP Server reply, success.";
                    Log.d(TAG, "run: write length=" + reply);
                    bwrite.write(reply + "#eof#");
                    bwrite.flush();
                    Log.d(TAG, "run: go to sleep 500");
                    Thread.sleep(500);
                }

            } catch (Exception e) {
                e.printStackTrace();
                sockets.remove(socket);
                if (errorListener != null) {
                    errorListener.accept("tcp error");
                }
            } finally {
                Log.d(TAG, "TcpServerRunnable run: finally");
            }

        }
    }
}