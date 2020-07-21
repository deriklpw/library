package com.derik.library.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.derik.library.view.MsgToast;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by derik on 17-2-15.
 */

public class DetectNetwork {

    /**
     * 功能：检测url是否接收请求
     * 描述：先检测网络是否可用，再和指定的url建立连接，依据不同结果发送消息给主线程处理
     *
     * @param ctx      上下文
     * @param url      指定url
     * @param callback true url可用，false url不可用
     */
    public synchronized static void detect(Context ctx, String url, Callback callback) {
        if (url == null || url.equals("")) {
            throw new IllegalArgumentException("Illegal argument");
        }
        if (isNetworkAvailable(ctx)) {
            connect(url, callback);
        } else {
            MsgToast.show(ctx, "Fail, please check the net connection");
        }
    }

    /**
     * 判断网路状态
     *
     * @param context 上下文
     * @return true，可用。false，不可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

    /**
     * 连接指定的url
     *
     * @param urlStr   指定url
     * @param callback 回调方法
     */
    private synchronized static void connect(final String urlStr, final Callback callback) {
        new Thread() {
            int state = -1;
            int counts = 0;
            HttpURLConnection httpUrlConnection;

            @Override
            public void run() {

                while (counts < 3) {
                    try {
                        URL url = new URL(urlStr);
                        httpUrlConnection = (HttpURLConnection) url.openConnection();
                        httpUrlConnection.setConnectTimeout(3 * 1000);
                        state = httpUrlConnection.getResponseCode();

                        if (state == 200) {
                            if (callback != null) {
                                callback.success(true);
                            }
                            break;
                        }

                    } catch (Exception ex) {
                        counts++;
                        Log.e("Url test", "url is unavailable, retry:" + counts);

                    } finally {
                        Log.i("State", "" + state);
                        httpUrlConnection.disconnect();
                        if (callback != null) {
                            callback.success(false);
                        }
                    }

                }

                if (state != 200) {
                    if (callback != null) {
                        callback.success(false);
                    }
                }
            }

        }.start();
    }

    @FunctionalInterface
    public interface Callback {
        void success(boolean result);
    }

}
