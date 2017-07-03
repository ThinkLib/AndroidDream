package com.madreain.androiddream.utils;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import com.madreain.androiddream.core.Constants;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;


import cn.bmob.v3.Bmob;

/**
 * Created by Paris_2214 on 2017/4/1.
 */

public class MadreainApplication extends MultiDexApplication {

    private static Context context;
    protected static MadreainApplication mInstance;

    public MadreainApplication() {
        mInstance = this;
    }

    public static MadreainApplication getApp() {
        if (mInstance != null) {
            return mInstance;
        } else {
            mInstance = new MadreainApplication();
            mInstance.onCreate();
            return mInstance;
        }
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //提供以下两种方式进行初始化操作：

        //第一：默认初始化
        Bmob.initialize(this, Constants.BmobID);

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);

             /* Bugly SDK初始化
        * 参数1：上下文对象
        * 参数2：APPID，平台注册时得到,注意替换成你的appId
        * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
        */
//        CrashReport.initCrashReport(getApplicationContext(), Constants.Bugly_appId, false);

        //友盟分享
        PlatformConfig.setWeixin(Constants.YOUMENG_WEIXIN_AppID,Constants.YOUMENG_WEIXIN_AppSecret);
        PlatformConfig.setQQZone(Constants.YOUMENG_QQ_AppID,Constants.YOUMENG_QQ_Appkey);
//        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
        UMShareAPI.get(this);


//        //有米广告
//        AdManager.getInstance(context).init(Constants.YOUMIappId, Constants.YOUMIappSecret, true);

        //友盟的push版本更新
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e("deviceToken", deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.d("MadreainApplication", s);
            }
        });
        //处理推送点击行为
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

    }

    //解决方法数超过65535  让自己写的application继承MultiDexApplication
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }
}
