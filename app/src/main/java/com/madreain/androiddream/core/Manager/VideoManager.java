package com.madreain.androiddream.core.Manager;

import android.content.Context;
import android.graphics.Movie;
import android.util.Log;

import com.madreain.androiddream.BuildConfig;
import com.madreain.androiddream.core.Constants;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.MTKnowledge;
import com.madreain.androiddream.core.Model.MType;
import com.madreain.androiddream.core.Model.ShareKnowledge;
import com.madreain.androiddream.core.Model.VideoKnowledge;
import com.madreain.androiddream.library.kprogresshud.KProgressHUD;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * @author madreain
 * @desc
 * @time 2017/4/25
 */

public class VideoManager {
    private static VideoManager instance;

    private VideoManager() {
    }

    public static VideoManager getInstance() {
        if (instance == null) {
            synchronized (VideoManager.class) {
                if (instance == null) {
                    instance = new VideoManager();
                }
            }
        }
        return instance;
    }

    /**
     * 上传一条单个文件的数据
     *
     * @param file
     * @param mbValueCallBack
     */
    public void addVideoKnowledge(final File file, final MBCallback.MBDataCallback mbValueCallBack) {
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
//                    mbValueCallBack.onSuccess();
                    insertObject(new VideoKnowledge(0,0,"1-1 环境组成介绍 ", "", bmobFile), new MBCallback.MBDataCallback() {
                        @Override
                        public void onSuccess() {
                            mbValueCallBack.onSuccess();
                        }

                        @Override
                        public void onError(String code) {
                            mbValueCallBack.onError(Constants.Error);
                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                } else {
                    mbValueCallBack.onError(Constants.Error);
                }
            }
        });
    }

    private void insertObject(final VideoKnowledge videoKnowledge, final MBCallback.MBDataCallback mbValueCallBack) {
        videoKnowledge.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    mbValueCallBack.onSuccess();
                } else {
                    mbValueCallBack.onError(Constants.Error);
                }
            }
        });
    }


    /**
     * 上传多个文件
     *
     * @param videoKnowledgeArrayList
     * @param mbValueCallBack
     */
    public void addVideoKnowledgeList(final ArrayList<VideoKnowledge> videoKnowledgeArrayList, final MBCallback.MUploadCallback mbValueCallBack) {
        String[] filePaths = new String[videoKnowledgeArrayList.size()];
        for (int i = 0; i < videoKnowledgeArrayList.size(); i++) {
            filePaths[i] = videoKnowledgeArrayList.get(i).getFile();
        }
        final ArrayList<VideoKnowledge> videoKnowledgeList = new ArrayList<>();
        Bmob.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //所有文件上传成功后
                if (urls.size() == videoKnowledgeArrayList.size()) {
                    for (int i = 0; i < urls.size(); i++) {
                        VideoKnowledge videoKnowledge = new VideoKnowledge(videoKnowledgeArrayList.get(i).getSelfid(),videoKnowledgeArrayList.get(i).getVid(),videoKnowledgeArrayList.get(i).getTitle(), videoKnowledgeArrayList.get(i).getFile(), files.get(i));
                        videoKnowledgeList.add(videoKnowledge);
                    }
                    //所有数据全部准备好
                    insertBatch(videoKnowledgeList, new MBCallback.MBDataCallback() {
                        @Override
                        public void onSuccess() {
                            mbValueCallBack.onUploadSuccess();
                        }

                        @Override
                        public void onError(String code) {
                            mbValueCallBack.onUploadFail(Constants.Error);
                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                mbValueCallBack.onUploadProgress(total, curIndex);
            }

            @Override
            public void onError(int i, String s) {
                mbValueCallBack.onUploadFail(Constants.Error);
            }
        });


    }

    public void insertBatch(List<VideoKnowledge> videoKnowledgeArrayList, final MBCallback.MBDataCallback mbValueCallBack) {
        //需要用bomb的对象进行存储
        List<BmobObject> bmobObjectList = new ArrayList<BmobObject>();
        //循环插入
        for (VideoKnowledge videoKnowledge : videoKnowledgeArrayList) {
            bmobObjectList.add(videoKnowledge);
        }
        new BmobBatch().insertBatch(bmobObjectList).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if (e == null) {
                    mbValueCallBack.onSuccess();
                } else {
                    if (BuildConfig.DEBUG) Log.d("KnowledgeManager", "e:" + e);
                    mbValueCallBack.onError(Constants.Error);
                }
            }
        });

    }

    KProgressHUD kProgressHUD;
    public void addVideoKnowledgeToServer(Context mContext) {
//        //上传一个
//        VideoManager.getInstance().addVideoKnowledge(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/1-1 环境组成介绍 (01'47) .mp4"), new MBCallback.MBDataCallback() {
//            @Override
//            public void onSuccess() {
//                Log.d("SelfStudyFragment", "成功");
//            }
//
//            @Override
//            public void onError(String code) {
//                Log.d("SelfStudyFragment", "失败");
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
        kProgressHUD = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
                .show();
        //上传多个
        ArrayList<VideoKnowledge> videoKnowledgeArrayList = new ArrayList<>();
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,0,"1-1 环境组成介绍", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/1-1 环境组成介绍 (01'47) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/1-1 环境组成介绍 (01'47) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,1,"1-2 安装JDK ", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/1-2 安装JDK (03'06) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/1-2 安装JDK (03'06) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,2,"1-3 集成环境下载演示温情版", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/1-3 集成环境下载演示温情版 (04'20) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/1-3 集成环境下载演示温情版 (04'20) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,3,"1-4 环境变量介绍（仅供了解）", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/1-4 环境变量介绍（仅供了解） (02'44) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/1-4 环境变量介绍（仅供了解） (02'44) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,4,"1-5 安装ADT（仅供了解）", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/1-5 安装ADT（仅供了解） (07'55) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/1-5 安装ADT（仅供了解） (07'55) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,5,"1-6 总结（仅供了解） ", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/1-6 总结（仅供了解） (01'39) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/1-6 总结（仅供了解） (01'39) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,6,"2-1 新建android项目", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/2-1 新建android项目 (03'24) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/2-1 新建android项目 (03'24) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,7,"2-2 项目结构介绍", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/2-2 项目结构介绍 (08'45) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/2-2 项目结构介绍 (08'45) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,8,"3-1 控件概述 ", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/3-1 控件概述 (01'21) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/3-1 控件概述 (01'21) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,9,"3-2 控件属性解析 ", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/3-2 控件属性解析 (01'20) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/3-2 控件属性解析 (01'20) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,10,"3-3 使用TextView与EditText ", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/3-3 使用TextView与EditText (12'31) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/3-3 使用TextView与EditText (12'31) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,11,"4-1 ImageView概述", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/4-1 ImageView概述 (01'28) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/4-1 ImageView概述 (01'28) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,12,"4-2 使用ImageView显示图片", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/4-2 使用ImageView显示图片 (05'35) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/4-2 使用ImageView显示图片 (05'35) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,13,"4-3 不同分辨率下图片的显示", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/4-3 不同分辨率下图片的显示 (06'31) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/4-3 不同分辨率下图片的显示 (06'31) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,14,"5-1 按钮概述", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/5-1 按钮概述 (02'42) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/5-1 按钮概述 (02'42) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,15,"5-2 使用按钮Button ", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/5-2 使用按钮Button (04'23) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/5-2 使用按钮Button (04'23) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,16,"5-3 使用按钮ImageButton", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/5-3 使用按钮ImageButton (03'58) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/5-3 使用按钮ImageButton (03'58) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,17,"5-4 总结", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/5-4 总结 (02'00) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/5-4 总结 (02'00) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,18,"6-1 概述 ", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/6-1 概述 (01'29) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/6-1 概述 (01'29) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,19,"6-2 匿名内部类监听按钮点击事件", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/6-2 匿名内部类监听按钮点击事件 (09'57) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/6-2 匿名内部类监听按钮点击事件 (09'57) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,20,"6-3 外部类监听点击事件", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/6-3 外部类监听点击事件 (15'00) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/6-3 外部类监听点击事件 (15'00) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,21,"6-4 接口方式监听按钮点击事件", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/6-4 接口方式监听按钮点击事件 (06'49) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/6-4 接口方式监听按钮点击事件 (06'49) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,22,"7-1 使用TextView实现跑马灯效果 ", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/7-1 使用TextView实现跑马灯效果 (15'04) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/7-1 使用TextView实现跑马灯效果 (15'04) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,23,"8-1 AutoCompleteTextView概述", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/8-1 AutoCompleteTextView概述 (02'59) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/8-1 AutoCompleteTextView概述 (02'59) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,24,"8-2 使用AutoCompleteTextView实现自动匹配输入的内容", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/8-2 使用AutoCompleteTextView实现自动匹配输入的内容 (11'09) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/8-2 使用AutoCompleteTextView实现自动匹配输入的内容 (11'09) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,25,"8-3 MultiAutoCompleteTextView概述", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/8-3 MultiAutoCompleteTextView概述 (01'37) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/8-3 MultiAutoCompleteTextView概述 (01'37) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,26,"8-4 使用MultiAutoCompleteTextView实现自动匹配输入的内容", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/8-4 使用MultiAutoCompleteTextView实现自动匹配输入的内容 (06'44) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/8-4 使用MultiAutoCompleteTextView实现自动匹配输入的内容 (06'44) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,27,"9-1 ToggleButton概述", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/9-1 ToggleButton概述 (02'30) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/9-1 ToggleButton概述 (02'30) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,28,"9-2 使用ToggleButton按钮实现开关效果", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/9-2 使用ToggleButton按钮实现开关效果 (12'39) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/9-2 使用ToggleButton按钮实现开关效果 (12'39) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,29,"10-1 复选框CheckBox概述", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/10-1 复选框CheckBox概述 (02'03) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/10-1 复选框CheckBox概述 (02'03) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,30,"10-2 使用CheckBox", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/10-2 使用CheckBox (13'55) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/10-2 使用CheckBox (13'55) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,31,"10-3 总结", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/10-3 总结 (00'27) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/10-3 总结 (00'27) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,32,"11-1 概述", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/11-1 概述 (02'08) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/11-1 概述 (02'08) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,33,"11-2 使用RadioGroup与RadioButton", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/11-2 使用RadioGroup与RadioButton (09'58) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/11-2 使用RadioGroup与RadioButton (09'58) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,34,"12-1 布局概述", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/12-1 布局概述 (00'56) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/12-1 布局概述 (00'56) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,35,"12-2 理解线性布局", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/12-2 理解线性布局 (01'19) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/12-2 理解线性布局 (01'19) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,36,"12-3 使用线性布局", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/12-3 使用线性布局 (12'50) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/12-3 使用线性布局 (12'50) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,37,"12-4 总结", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/12-4 总结 (00'51) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/12-4 总结 (00'51) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,38,"13-1 理解相对布局", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/13-1 理解相对布局 (03'42) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/13-1 理解相对布局 (03'42) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,39,"13-2 使用相对布局", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/13-2 使用相对布局 (19'00) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/13-2 使用相对布局 (19'00) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,40,"14-1 五布局之帧布局FrameLayout", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/14-1 五布局之帧布局FrameLayout (08'54) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/14-1 五布局之帧布局FrameLayout (08'54) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,41,"15-1 五布局之绝对布局AbsoluteLayout", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/15-1 五布局之绝对布局AbsoluteLayout (05'04) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/15-1 五布局之绝对布局AbsoluteLayout (05'04) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,42,"16-1 属性简介", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/16-1 属性简介 (04'35) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/16-1 属性简介 (04'35) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,43,"16-2 使用TableLayout的属性", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/16-2 使用TableLayout的属性 (10'14) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/16-2 使用TableLayout的属性 (10'14) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,44,"16-3 实现计算机简单布局", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/16-3 实现计算机简单布局 (06'45) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/16-3 实现计算机简单布局 (06'45) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,45,"17-1 重新认识Activity ", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/17-1 重新认识Activity (04'28) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/17-1 重新认识Activity (04'28) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,46,"17-2 生命周期概述", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/17-2 生命周期概述 (04'07) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/17-2 生命周期概述 (04'07) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,47,"17-3 查看Activity生命周期执行过程", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/17-3 查看Activity生命周期执行过程 (14'14) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/17-3 查看Activity生命周期执行过程 (14'14) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,48,"18-1 无返回结果的页面跳转", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/18-1 无返回结果的页面跳转 (14'05) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/18-1 无返回结果的页面跳转 (14'05) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,49,"18-2 有返回结果的页面跳转", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/18-2 有返回结果的页面跳转 (15'04) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/18-2 有返回结果的页面跳转 (15'04) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,50,"19-1 App签名打包 ", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/19-1 App签名打包 (06'04) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/19-1 App签名打包 (06'04) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,51,"20-1 使用SDK开发文档", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/20-1 使用SDK开发文档 (06'04) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/20-1 使用SDK开发文档 (06'04) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,52,"21-1 设置文本框样式", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/21-1 设置文本框样式 (07'46) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/21-1 设置文本框样式 (07'46) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,53,"21-2 数字按钮布局", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/21-2 数字按钮布局 (11'46) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/21-2 数字按钮布局 (11'46) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,54,"21-3 修改按钮样式", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/21-3 修改按钮样式 (07'38) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/21-3 修改按钮样式 (07'38) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,55,"21-4 实例化控件", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/21-4 实例化控件 (07'56) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/21-4 实例化控件 (07'56) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,56,"21-5 实现业务逻辑", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/21-5 实现业务逻辑 (18'48) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/21-5 实现业务逻辑 (18'48) .mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(0,57,"21-6 排查bug ", "/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/21-6 排查bug (03'53) .mp4", new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第一门课（入门篇）(UHD)/21-6 排查bug (03'53) .mp4"))));


//        videoKnowledgeArrayList.add(new VideoKnowledge(1,58,"1-1AndroidManifest","/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第二门课/1、AndroidManifest配置文件/安卓零基础入门1-1AndroidManifest.mp4",new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第二门课/1、AndroidManifest配置文件/安卓零基础入门1-1AndroidManifest.mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,59,"1-2全局信息配置","/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第二门课/1、AndroidManifest配置文件/安卓零基础入门1-2全局信息配置.mp4",new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第二门课/1、AndroidManifest配置文件/安卓零基础入门1-2全局信息配置.mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,60,"1-3在配置文件中注册组件","/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第二门课/1、AndroidManifest配置文件/安卓零基础入门1-3在配置文件中注册组件.mp4",new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第二门课/1、AndroidManifest配置文件/安卓零基础入门1-3在配置文件中注册组件.mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,61,"1-4Android中的权限配置","/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第二门课/1、AndroidManifest配置文件/安卓零基础入门1-4Android中的权限配置.mp4",new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第二门课/1、AndroidManifest配置文件/安卓零基础入门1-4Android中的权限配置.mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,62,"1-5自定义权限","/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第二门课/1、AndroidManifest配置文件/安卓零基础入门1-5自定义权限.mp4",new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第二门课/1、AndroidManifest配置文件/安卓零基础入门1-5自定义权限.mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,63,"1-6总结","/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第二门课/1、AndroidManifest配置文件/安卓零基础入门1-6总结.mp4",new BmobFile(new File("/storage/emulated/0/BaiduNetdisk/Android攻城狮/Android攻城狮的第二门课/1、AndroidManifest配置文件/安卓零基础入门1-6总结.mp4"))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,64,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,65,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,66,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,67,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,68,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,69,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,70,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,71,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,72,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,73,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,74,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,75,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,76,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,77,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,78,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,79,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,80,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,81,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,82,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,83,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,84,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,85,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,86,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,87,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,88,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,89,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,90,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,91,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,92,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,93,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,94,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,95,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,96,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,97,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,98,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,99,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,100,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,101,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,102,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,103,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,104,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,105,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,106,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,107,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,108,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,109,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,110,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,111,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,112,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,113,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,114,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,115,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,116,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,117,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,118,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,119,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,120,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,121,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,122,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,123,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,124,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,125,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,126,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,127,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,128,"","",new BmobFile(new File(""))));
//        videoKnowledgeArrayList.add(new VideoKnowledge(1,129,"","",new BmobFile(new File(""))));

        VideoManager.getInstance().addVideoKnowledgeList(videoKnowledgeArrayList, new MBCallback.MUploadCallback() {
            @Override
            public void onUploadSuccess() {
                Log.d("VideoManager", "成功");
                kProgressHUD.dismiss();
            }

            @Override
            public void onUploadProgress(int total, int current) {
                Log.d("VideoManager", "上传中" + current + "/" + total);
            }

            @Override
            public void onUploadProgress(int current, long totalSize, long sendSize) {

            }

            @Override
            public void onUploadFail(String code) {
                Log.d("VideoManager", "失败");
                kProgressHUD.dismiss();
            }
        });
    }


    /**
     * 刷新数据
     * 根据selfid去查找这个视频集下的视频
     * @param selfid
     * @param mbValueCallBack
     */
    public void refreshVideoKnowledgeList(int selfid,final MBCallback.MBValueCallBack<List<VideoKnowledge>> mbValueCallBack){
        BmobQuery<VideoKnowledge> bmobQuery = new BmobQuery<>();
        // 根据smid字段升序显示数据
        bmobQuery.order("vid");
        //查询playerName叫“比目”的数据
        bmobQuery.addWhereEqualTo("selfid", selfid);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        bmobQuery.setLimit(10);
        bmobQuery.findObjects(new FindListener<VideoKnowledge>() {
            @Override
            public void done(List<VideoKnowledge> list, BmobException e) {
                if (e == null) {
                    mbValueCallBack.onSuccess(list);
                    //获取的时候还存储到本地
                } else if(e.getErrorCode()==9015){
                    if (BuildConfig.DEBUG) Log.d("TypeManager", "e:" + e);
                } else {
                    if (BuildConfig.DEBUG) Log.d("TypeManager", "e:" + e);
                    mbValueCallBack.onError(Constants.Error);
                }
            }
        });
    }


    /**
     * 加载更多
     * 根据selfid去查找这个视频集下的视频
     * @param selfid
     * @param mbValueCallBack
     */
    public void getMoreVideoKnowledgeList(int size,int selfid,final MBCallback.MBValueCallBack<List<VideoKnowledge>> mbValueCallBack){
        BmobQuery<VideoKnowledge> bmobQuery = new BmobQuery<>();
        // 忽略前10条数据（即第一页数据结果）
        bmobQuery.setSkip(size);
        // 根据smid字段升序显示数据
        bmobQuery.order("vid");
        //查询playerName叫“比目”的数据
        bmobQuery.addWhereEqualTo("selfid", selfid);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        bmobQuery.setLimit(10);
        bmobQuery.findObjects(new FindListener<VideoKnowledge>() {
            @Override
            public void done(List<VideoKnowledge> list, BmobException e) {
                if (e == null) {
                    mbValueCallBack.onSuccess(list);
                    //获取的时候还存储到本地
                } else if(e.getErrorCode()==9015){
                    if (BuildConfig.DEBUG) Log.d("TypeManager", "e:" + e);
                } else {
                    if (BuildConfig.DEBUG) Log.d("TypeManager", "e:" + e);
                    mbValueCallBack.onError(Constants.Error);
                }
            }
        });
    }

}
