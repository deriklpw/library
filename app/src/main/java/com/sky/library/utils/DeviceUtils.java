package com.sky.library.utils;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresPermission;

import java.net.NetworkInterface;

/**
 * description ：
 * author : Derik.Wu
 * email : Derik.Wu@waclighting.com.cn
 * date : 2020/7/8
 */
public class DeviceUtils {
    private static final String TAG = "DeviceUtils";

    /**
     * 国际移动设备识别码，GSM和WCDMA手机终端需要使用IMEI号码
     *
     * @param context
     * @param slotIndex
     * @return
     */
    public static String getPhoneIMEI(Context context, int slotIndex) {
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            if (tm != null) {
                if (Build.VERSION.SDK_INT >= 26) {
                    //8.0以上,需要权限
                    //GSM, WCDMA
                    if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        imei = tm.getImei(slotIndex);
                        if (TextUtils.isEmpty(imei)) {
                            //CDMA
                            imei = tm.getMeid(slotIndex);
                        }
                    }
                } else if (Build.VERSION.SDK_INT >= 23) {
                    //6.0以上
                    imei = tm.getDeviceId(slotIndex);
                } else {
                    //小于6.0
                    imei = tm.getDeviceId();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "getPhoneIMEI: IMEI=" + imei);
        return imei;
    }

    /**
     * 国际移动用户识别码
     *
     * @param context
     * @return
     */
    @RequiresPermission(value = Manifest.permission.READ_PHONE_STATE)
    public static String getIMSI(Context context) {
        String imsi = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            if (tm != null) {
                imsi = tm.getSubscriberId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getIMSI: IMSI=" + imsi);
        return imsi;
    }

    /**
     * MEID 移动设备识别码(Mobile Equipment Identifier)是CDMA手机的身份识别码
     *
     * @param context
     * @param slotIndex
     * @return
     */
    @RequiresPermission(value = Manifest.permission.READ_PHONE_STATE)
    public static String getPhoneMEID(Context context, int slotIndex) {
        String meid = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            if (tm != null) {
                if (Build.VERSION.SDK_INT >= 26) {
                    //8.0
                    meid = tm.getMeid(slotIndex);
                } else if (Build.VERSION.SDK_INT >= 23) {
                    //6.0
                    meid = tm.getDeviceId(slotIndex);
                } else {
                    meid = tm.getDeviceId();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getPhoneMEID: MEID=" + meid);
        return meid;
    }

//    private String getIMEI(int slotId) {
//        try {
//            Class clazz = Class.forName("android.os.SystemProperties");
//            Method method = clazz.getMethod("get", String.class, String.class);
//            String imei = (String) method.invoke(null, "ril.gsm.imei", "");
//            if (!TextUtils.isEmpty(imei)) {
//                String[] split = imei.split(",");
//                if (split.length > slotId) {
//                    imei = split[slotId];
//                }
//                Log.d(TAG, "getIMEI imei: " + imei);
//                return imei;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.w(TAG, "getIMEI error : " + e.getMessage());
//        }
//        return "";
//    }
//
//    private String getMEID() {
//        try {
//            Class clazz = Class.forName("android.os.SystemProperties");
//            Method method = clazz.getMethod("get", String.class, String.class);
//
//            String meid = (String) method.invoke(null, "ril.cdma.meid", "");
//            if (!TextUtils.isEmpty(meid)) {
//                Log.d(TAG, "getMEID meid: " + meid);
//                return meid;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.w(TAG, "getMEID error : " + e.getMessage());
//        }
//        return "";
//    }

    /**
     * 不需要任何权限
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        String androidId = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        Log.d(TAG, "getAndroidId: AndroidId=" + androidId);
        return androidId;
    }

    public String getWifiSSID(Context context) {
        String ssid = "unknown id";
        if (Build.VERSION.SDK_INT <= 26 || Build.VERSION.SDK_INT == 28) {

            WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            assert mWifiManager != null;
            WifiInfo info = mWifiManager.getConnectionInfo();

            if (Build.VERSION.SDK_INT < 19) {
                return info.getSSID();
            } else {
                return info.getSSID().replace("\"", "");
            }
        } else if (Build.VERSION.SDK_INT == 27) {
            ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connManager != null;
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo.isConnected()) {
                if (networkInfo.getExtraInfo() != null) {
                    return networkInfo.getExtraInfo().replace("\"", "");
                }
            }
        }
        return ssid;
    }

    public static String getMacAddress(Context context) {
        String macAddress = "";
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                StringBuilder stringBuilder = new StringBuilder();
                NetworkInterface networkInterface;
                networkInterface = NetworkInterface.getByName("eth1");
                if (networkInterface == null) {
                    networkInterface = NetworkInterface.getByName("wlan0");
                }
                if (networkInterface == null) {
                    macAddress = "02:00:00:00:00:02";
                } else {
                    byte[] addr = networkInterface.getHardwareAddress();
                    for (byte b : addr) {
                        stringBuilder.append(String.format("%02X:", b));
                    }
                    if (stringBuilder.length() > 0) {
                        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    }
                    macAddress = stringBuilder.toString();
                }
            } else {
                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                macAddress = wifiInfo.getMacAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;
    }

    public static String getSerialNumber(Context context) {
        String serialNum = "";
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                //8.0以上，Build.getSerial()，需要权限
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    serialNum = Build.getSerial();
                }
            } else {
                //小于8.0，可以直接用Build.SERIAL
                serialNum = Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getSerialNumber: SN=" + serialNum);
        return serialNum;
    }
}
