package com.sky.library.utils;

import android.os.Environment;

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
        switch (Environment.getExternalStorageState()) {
            case Environment.MEDIA_MOUNTED:
                state = 1;
//            File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
//            String str_dir = sdDir.toString();
//            Log.i("SDCard", str_dir);
                break;
            case Environment.MEDIA_MOUNTED_READ_ONLY:
                state = 0;
                LogUtil.e("", "MEDIA_MOUNTED_READ_ONLY");
                break;
            case Environment.MEDIA_NOFS:
                state = 0;
                LogUtil.e("", "MEDIA_NOFS");
                break;
            case Environment.MEDIA_SHARED:
                state = 0;
                LogUtil.e("", "MEDIA_SHARED");
                break;
            case Environment.MEDIA_CHECKING:
                state = 0;
                LogUtil.e("", "MEDIA_CHECKING");
                break;
            case Environment.MEDIA_BAD_REMOVAL:
                state = 0;
                LogUtil.e("", "MEDIA_BAD_REMOVAL");
                break;
            case Environment.MEDIA_UNMOUNTABLE:
                state = 0;
                LogUtil.e("", "MEDIA_UNMOUNTABLE");
                break;
            case Environment.MEDIA_UNMOUNTED:
                state = 0;
                LogUtil.e("", "MEDIA_UNMOUNTED");
                break;
            case Environment.MEDIA_REMOVED:
                state = 0;
                LogUtil.e("", "MEDIA_REMOVED");
                break;
        }
        return state;
    }
}
