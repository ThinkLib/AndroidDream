package com.madreain.androiddream.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * @author madreain
 * @desc
 * @time 2017/4/5
 */

public class WindowUtils {
    //    //宽度高度适配   6
//     WindowManager wm = getActivity().getWindowManager();
//    int width = wm.getDefaultDisplay().getWidth();

    public static int getWindowWidth(Activity activity) {
        WindowManager wm = activity.getWindowManager();
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getWindowHeight(Activity activity) {
        WindowManager wm = activity.getWindowManager();
        return wm.getDefaultDisplay().getHeight();
    }

    public static void SetStatusBar(Window window, Context context, Resources resources, String color, ViewGroup layout) {
        //状态栏  导航栏的开启
        if (android.os.Build.VERSION.SDK_INT > 18) {
//            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            layout.setPadding(0, getStatusBarHeight(resources), 0,
                    0);

        }
        //设置状态栏
//        TextView textView = new TextView(this);
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams lParams = new
                LinearLayout.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(resources));
//        textView.setBackgroundColor(Color.parseColor("#004E48"));
        textView.setBackgroundColor(Color.parseColor(color));
//        textView.setBackground();
        textView.setLayoutParams(lParams);
        // 获得根视图并把TextView加进去。
//        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        ViewGroup viewGroup = (ViewGroup) window.getDecorView();
        viewGroup.addView(textView);
    }


    public static TextView getSetStatusBar(Window window, Context context, Resources resources, String color, ViewGroup layout) {
        //状态栏  导航栏的开启
        if (android.os.Build.VERSION.SDK_INT > 18) {
//            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            layout.setPadding(0, getStatusBarHeight(resources), 0,
                    0);

        }
        //设置状态栏
//        TextView textView = new TextView(this);
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams lParams = new
                LinearLayout.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(resources));
//        textView.setBackgroundColor(Color.parseColor("#004E48"));
        textView.setBackgroundColor(Color.parseColor(color));
//        textView.setBackground();
        textView.setLayoutParams(lParams);
        // 获得根视图并把TextView加进去。
//        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        ViewGroup viewGroup = (ViewGroup) window.getDecorView();
        viewGroup.addView(textView);
        return textView;
    }

    //    获取手机状态栏高度
    public static int getStatusBarHeight(Resources resources) {
        Class c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
//            statusBarHeight = getResources().getDimensionPixelSize(x);
            statusBarHeight = resources.getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    // 获取ActionBar的高度

    public int getActionBarHeight(Resources resources) {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
//        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))//
        if (resources.newTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))//
//            如果资源是存在的、有效的
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.getDisplayMetrics());
        }
        return actionBarHeight;
    }

    //背景透明度的设置    这个背景设置会导致一些问题  黑屏
    public static void backgroundAlpha(float bgAlpha, Window window) {
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = bgAlpha; //0.0-1.0
//        getWindow().setAttributes(lp);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
//                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

    }

}
