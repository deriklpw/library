package com.fih.idx.deriklibrary.utils;

import android.os.Environment;
import android.util.Log;

/**
 * Created by derik on 17-2-15.
 */

public class SDCardTest {

    /**
     * 判断SD是否可读写
     *
     * @return int, 1可用，0不可用
     */
    public static int sdcardState() {
        // TODO Auto-generated method stub
        int state = 0;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            state = 1;
//            File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
//            String str_dir = sdDir.toString();
//            Log.i("SDCard", str_dir);
        } else if (Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            state = 0;
            Log.e("", "MEDIA_MOUNTED_READ_ONLY");
        } else if (Environment.getExternalStorageState().equals(Environment.MEDIA_NOFS)) {
            state = 0;
            Log.e("", "MEDIA_NOFS");
        } else if (Environment.getExternalStorageState().equals(Environment.MEDIA_SHARED)) {
            state = 0;
            Log.e("", "MEDIA_SHARED");
        } else if (Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
            state = 0;
            Log.e("", "MEDIA_CHECKING");
        } else if (Environment.getExternalStorageState().equals(Environment.MEDIA_BAD_REMOVAL)) {
            state = 0;
            Log.e("", "MEDIA_BAD_REMOVAL");
        } else if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTABLE)) {
            state = 0;
            Log.e("", "MEDIA_UNMOUNTABLE");
        } else if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED)) {
            state = 0;
            Log.e("", "MEDIA_UNMOUNTED");
        } else if (Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED)) {
            state = 0;
            Log.e("", "MEDIA_REMOVED");
        }
        return state;
    }
}
