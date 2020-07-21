package com.derik.library.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by derik on 17-7-12 14:52.
 * Email：Derik.LP.Wu@mail.foxconn.com
 */

public class LanguageUtil {

    public static boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;

    }

    /**
     * 是否是英文
     *
     * @param charaString 指定文字
     * @return true，是英文，false，不是英文
     */
    public static boolean isEnglish(String charaString) {

        return charaString.matches("^[a-zA-Z]*");

    }

    /**
     * 是否是中文
     *
     * @param str 指定文字
     * @return true，是中文，false，不是中文
     */
    public static boolean isChinese(String str) {

        String regEx = "[\\u4e00-\\u9fa5]+";

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);

        return m.find();

    }
}
