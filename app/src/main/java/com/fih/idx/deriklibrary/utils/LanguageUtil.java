package com.fih.idx.deriklibrary.utils;

import android.content.Context;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by derik on 17-7-12 14:52.
 * Email：Derik.LP.Wu@mail.foxconn.com
 */

public class LanguageUtil {
    private static final String ICON_SAVED_NAME = "head_icon.jpg";

    public static String getIconPath(Context context){

        return context.getFilesDir().getAbsolutePath() + File.separator + ICON_SAVED_NAME;
    }

    private static boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

            return true;

        }

        return false;

    }

    /**

     * 是否是英文

     * @param charaString

     * @return

     */

    private static boolean isEnglish(String charaString){

        return charaString.matches("^[a-zA-Z]*");

    }

    private static boolean isChinese(String str){

        String regEx = "[\\u4e00-\\u9fa5]+";

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);

        if(m.find())

            return true;

        else

            return false;

    }
}
