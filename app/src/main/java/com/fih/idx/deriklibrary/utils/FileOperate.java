package com.fih.idx.deriklibrary.utils;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by derik on 17-2-28.
 */

public class FileOperate {

    /**
     * 功能：遍历目录，获取文件
     *
     * @param dir       文件目录
     * @param listChild 是否遍历子目录
     * @return List<filePath>
     */
    public static List<String> getFiles(File dir, boolean listChild) {
        if (!dir.isDirectory()) {
            return null;
        }

        List<String> list = null;
        File files[] = dir.listFiles();

        if (files != null && files.length > 0) {
            list = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f.isDirectory() && listChild) {
                    getFiles(f, listChild);
                } else {
                    list.add(f.getAbsolutePath());
                }
            }
        }
        return list;
    }


    /**
     * 功能：单个文件比较
     *
     * @param localFile 本地文件
     * @param tarFile   目标文件
     * @return 正确，返回true；错误，返回false
     */
    public static boolean compare(File localFile, File tarFile) {
        String cmpFileMD5 = FileDigest.getFileMD5(localFile);
        String tarFileMD5 = FileDigest.getFileMD5(tarFile);
        if (cmpFileMD5 != null && tarFileMD5 != null) {
            if (cmpFileMD5.equals(tarFileMD5)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 功能：指定路径下，建立文件
     * @param path     存储目录的路径
     * @param fileName 存储文件名
     */
    public static File prepareFile(final String path, final String fileName) {

        // 外部存储, 先判断外部存储是否可用
        if (SDCardTest.sdcardState() == 1) {

            // 传入SD卡的路径
            File fileDir = new File(path);

            if (!fileDir.exists()) {
                if (fileDir.mkdirs()) {
                    Log.i("ExternalPath", fileDir.getAbsolutePath());
                } else {
                    Log.e("ExternalPath", path + " make failed!");
                }
            }

            File targetFile = new File(fileDir.getAbsolutePath() + "/"
                    + fileName);
            try {
                if (targetFile.exists()) {
                    if (!targetFile.delete()) {
                        Log.e("File", "Delete failed:" + targetFile.getAbsolutePath());
                    } else {
                        Log.i("File", "Deleted " + targetFile.getAbsolutePath());
                    }
                }

                if (targetFile.createNewFile()) {
                    Log.i("File", "Created" + targetFile.getAbsolutePath());
                } else {
                    Log.e("File", "Create failed:" + targetFile.getAbsolutePath());
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("File", "Create failed:" + targetFile.getAbsolutePath());
                return null;
            }

            return targetFile;
        }
        return null;
    }

}
