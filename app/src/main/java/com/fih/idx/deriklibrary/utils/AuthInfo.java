package com.fih.idx.deriklibrary.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fujiayi on 2017/6/24.
 */

public class AuthInfo {

    private static HashMap<String, Object> authInfo;

    private static final String TAG = "AuthInfo";

    /**
     * Get the values of specify names
     * @param context
     * @param metaNameAppId app id name in AndroidManifest.xml
     * @param metaNameAppKey app key name in AndroidManifest.xml
     * @param metaNameAppSecret app secret name in AndroidManifest.xml
     * @return Map, the key is the name, the value is the value of specify name in AndroidManifest.xml
     */
    public static Map<String, Object> getAuthParams(final Context context, final String metaNameAppId,
                                                    final String metaNameAppKey, final String metaNameAppSecret) {
        if (authInfo == null) {
            try {
                authInfo = new HashMap<String, Object>(3);

                ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                String appId = Integer.toString(appInfo.metaData.getInt(metaNameAppId));
                String appKey = appInfo.metaData.getString(metaNameAppKey);
                String appSecret = appInfo.metaData.getString(metaNameAppSecret);

                authInfo.put(metaNameAppId, appId); // 认证相关, key, 从开放平台(http://yuyin.baidu.com)中获取的key
                authInfo.put(metaNameAppKey, appKey); // 认证相关, key, 从开放平台(http://yuyin.baidu.com)中获取的key
                authInfo.put(metaNameAppSecret, appSecret); // 认证相关, secret, 从开放平台(http://yuyin.baidu.com)secret

            } catch (Exception e) {
                e.printStackTrace();
                String message = "请在AndroidManifest.xml中配置APP_ID, API_KEY 和 SECRET_KEY";
                Log.e(TAG, message);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                return null;
            }
        }
        return authInfo;
    }

}
