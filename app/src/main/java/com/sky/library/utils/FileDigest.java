package com.sky.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class FileDigest {

    /**
     * 获取一串字符的MD5值
     *
     * @param text 字符串，用于生成MD5值
     * @return String
     */
    public static String getStringMD5(String text) {
        String result = null;
        try {
            byte[] data = MessageDigest.getInstance("MD5").digest(text.getBytes("UTF-8"));

            result = new BigInteger(1, data).toString(16);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取单个文件的MD5值
     *
     * @param file 文件，用于产生MD5值
     * @return String
     */
    public static String getFileMD5(File file) {
        String result = null;
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte[] buffer = new byte[2048];
        int read;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((read = in.read(buffer, 0, 1024)) > 0) {
                digest.update(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (digest != null) {
            result = new BigInteger(1, digest.digest()).toString(16); //16进制
        }
        return result;
    }

    /**
     * 获取文件夹中文件的MD5值
     *
     * @param dir       目录
     * @param listChild true递归其子目录中的文件
     * @return Map 存储所有文件MD5值的哈希表
     */
    public static Map<String, String> getDirMD5(File dir, boolean listChild) {
        if (!dir.isDirectory()) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        String md5;
        File files[] = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.isDirectory() && listChild) {
                    map.putAll(getDirMD5(f, true));
                } else {
                    md5 = getFileMD5(f);
                    if (md5 != null) {
                        map.put(f.getPath(), md5);
                    }
                }
            }
        }
        return map;
    }
}