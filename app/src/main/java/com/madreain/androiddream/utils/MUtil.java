package com.madreain.androiddream.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author madreain
 * @desc
 * @time 2017/4/11
 */

public class MUtil {
    public static Context getContext() {
        return MadreainApplication.getContext();
    }

    public static boolean isEmail(CharSequence input) {
        return isMatch("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", input);
    }
    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    public  static  String getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
