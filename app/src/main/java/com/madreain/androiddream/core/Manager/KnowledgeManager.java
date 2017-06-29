package com.madreain.androiddream.core.Manager;

import android.util.Log;

import com.madreain.androiddream.BuildConfig;
import com.madreain.androiddream.core.Dao.MTKnowledgeDao;
import com.madreain.androiddream.core.Dao.MTypeDao;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.MTKnowledge;
import com.madreain.androiddream.core.Model.MType;
import com.madreain.androiddream.core.Constants;
import com.madreain.androiddream.utils.MUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;

/**
 * Created by madreain on 2017/4/1.
 * 大分类  小分类对应的知识
 */

public class KnowledgeManager {
    private static KnowledgeManager instance;

    private KnowledgeManager() {
    }

    public static KnowledgeManager getInstance() {
        if (instance == null) {
            synchronized (KnowledgeManager.class) {
                if (instance == null) {
                    instance = new KnowledgeManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加多条数据
     * 限制一次性50条
     *
     * @param mtKnowledgeList
     * @param mbValueCallBack
     */
    public void addKnowledge(List<MTKnowledge> mtKnowledgeList, final MBCallback.MBDataCallback mbValueCallBack) {
        //需要用bomb的对象进行存储
        List<BmobObject> bmobObjectList = new ArrayList<BmobObject>();
        //循环插入
        for (MTKnowledge mtKnowledge : mtKnowledgeList) {
            bmobObjectList.add(mtKnowledge);
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


    /***
     * 获取相对应的分类下的知识
     * 刷新
     *
     * @param mid             分类的id
     * @param mbValueCallBack
     */
    public void refreshKnowledgeByMid(int mid, final MBCallback.MBValueCallBack<List<MTKnowledge>> mbValueCallBack) {
        BmobQuery<MTKnowledge> bmobQuery = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        bmobQuery.addWhereEqualTo("mid", mid);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        bmobQuery.setLimit(10);
        bmobQuery.findObjects(new FindListener<MTKnowledge>() {
            @Override
            public void done(List<MTKnowledge> list, BmobException e) {
                if (e == null) {
////                    if (BuildConfig.DEBUG) Log.d("TypeManager", "list:" + list);
//                    for (MType mType : list) {
//                        if (BuildConfig.DEBUG)
//                            Log.d("TypeManager", "mType.getMid():" + mType.getMid());
//                        Log.d("TypeManager", mType.getTitle());
//                    }

                    mbValueCallBack.onSuccess(list);
                    //获取的时候还存储到本地
                    new MTKnowledgeDao(MUtil.getContext()).addMTKnowledge(list);
                } else {
                    if (BuildConfig.DEBUG) Log.d("KnowledgeManager", "e:" + e);
                    mbValueCallBack.onError(Constants.Error);
                }
            }
        });
    }


    /***
     * 获取相对应的分类下的知识
     * 获取更多
     *
     * @param size
     * @param mid             分类的id
     * @param mbValueCallBack
     */
    public void getMoreKnowledgeByMid(int size, int mid, final MBCallback.MBValueCallBack<List<MTKnowledge>> mbValueCallBack) {
        BmobQuery<MTKnowledge> bmobQuery = new BmobQuery<>();
        // 忽略前10条数据（即第一页数据结果）
        bmobQuery.setSkip(size);
        // 根据kid字段升序显示数据
        bmobQuery.order("kid");
        //查询playerName叫“比目”的数据
        bmobQuery.addWhereEqualTo("mid", mid);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        bmobQuery.setLimit(10);
        bmobQuery.findObjects(new FindListener<MTKnowledge>() {
            @Override
            public void done(List<MTKnowledge> list, BmobException e) {
                if (e == null) {
////                    if (BuildConfig.DEBUG) Log.d("TypeManager", "list:" + list);
//                    for (MType mType : list) {
//                        if (BuildConfig.DEBUG)
//                            Log.d("TypeManager", "mType.getMid():" + mType.getMid());
//                        Log.d("TypeManager", mType.getTitle());
//                    }

                    mbValueCallBack.onSuccess(list);
                    //获取的时候还存储到本地
                    new MTKnowledgeDao(MUtil.getContext()).addMTKnowledge(list);
                } else {
                    if (BuildConfig.DEBUG) Log.d("KnowledgeManager", "e:" + e);
                    mbValueCallBack.onError(Constants.Error);
                }
            }
        });
    }


    /***
     * 获取相对应的分类下的知识
     * 刷新
     *
     * @param smid            分类的id
     * @param mbValueCallBack
     */
    public void refreshKnowledgeBySmid(int smid, final MBCallback.MBValueCallBack<List<MTKnowledge>> mbValueCallBack) {
        BmobQuery<MTKnowledge> bmobQuery = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        bmobQuery.addWhereEqualTo("smid", smid);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        bmobQuery.setLimit(10);
        bmobQuery.findObjects(new FindListener<MTKnowledge>() {
            @Override
            public void done(List<MTKnowledge> list, BmobException e) {
                if (e == null) {
////                    if (BuildConfig.DEBUG) Log.d("TypeManager", "list:" + list);
//                    for (MType mType : list) {
//                        if (BuildConfig.DEBUG)
//                            Log.d("TypeManager", "mType.getMid():" + mType.getMid());
//                        Log.d("TypeManager", mType.getTitle());
//                    }

                    mbValueCallBack.onSuccess(list);
                    //获取的时候还存储到本地
                    new MTKnowledgeDao(MUtil.getContext()).addMTKnowledge(list);
                } else {
                    if (BuildConfig.DEBUG) Log.d("KnowledgeManager", "e:" + e);
                    mbValueCallBack.onError(Constants.Error);
                }
            }
        });
    }


    /***
     * 获取相对应的分类下的知识
     * 获取更多
     *
     * @param size
     * @param smid            分类的id
     * @param mbValueCallBack
     */
    public void getMoreKnowledgeBySmid(int size, int smid, final MBCallback.MBValueCallBack<List<MTKnowledge>> mbValueCallBack) {
        BmobQuery<MTKnowledge> bmobQuery = new BmobQuery<>();
        // 忽略前10条数据（即第一页数据结果）
        bmobQuery.setSkip(size);
        // 根据kid字段升序显示数据
        bmobQuery.order("kid");
        //查询playerName叫“比目”的数据
        bmobQuery.addWhereEqualTo("smid", smid);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        bmobQuery.setLimit(10);
        bmobQuery.findObjects(new FindListener<MTKnowledge>() {
            @Override
            public void done(List<MTKnowledge> list, BmobException e) {
                if (e == null) {
////                    if (BuildConfig.DEBUG) Log.d("TypeManager", "list:" + list);
//                    for (MType mType : list) {
//                        if (BuildConfig.DEBUG)
//                            Log.d("TypeManager", "mType.getMid():" + mType.getMid());
//                        Log.d("TypeManager", mType.getTitle());
//                    }

                    mbValueCallBack.onSuccess(list);
                    //获取的时候还存储到本地
                    new MTKnowledgeDao(MUtil.getContext()).addMTKnowledge(list);
                } else {
                    if (BuildConfig.DEBUG) Log.d("KnowledgeManager", "e:" + e);
                    mbValueCallBack.onError(Constants.Error);
                }
            }
        });
    }

    /***
     * 开源项目数据的添加
     */
    public void addOpenSourceData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        开源项目数据的添加
        mtKnowledgeList = getOpenSourceData();

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //开源项目的添加
    public List<MTKnowledge> getOpenSourceData() {
        //        int mid;//某个一级分类下
//        int smid;//大分类下的小分类
//        int kid;//知识的id
//        String title;//知识标题
//        String desc;//项目描述
//        String author;//作者
//        String sorce;//来源
//        String url;//文章的链接
//        long ptime;//上传时间
//        long updatetime;//更新时间
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();

        //开源项目 mid=4 smid=-1 kid=0以40000开头（分别依据mid=4来的）
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40000, "ZoomHeader", "神交互。模仿饿了么详情页可以跟随手指移动 viewpager变详情页", "androidwing", "github", "https://github.com/githubwing/ZoomHeader", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40001, "lottie-android", "动画效果", "Airbnb", "github", "https://github.com/airbnb/lottie-android", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40002, "Material-Animations", "演示View的平移、缩放动画，activity进入和退出动画，界面间元素共享，并且开发者在README中，对动画原理进行了精讲，是学习动画很好的项目，项目代码量比较少，也很适合新手学习。", "lgvalle", "github", "https://github.com/lgvalle/Material-Animations", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40003, "Meizhi", "每天推送一张妹子图、一个小视频和一系列程序员精选文章，数据来源于代码家的干货集中营。", "drakeet", "github", "https://github.com/drakeet/Meizhi", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40004, "LookLook", "可以阅读知乎日报，网易头条，每日推送一张妹子图片和视频，是一个精美的阅读软件。遵循Google Meterial 设计风格，加入了一些5.0以上的新特性，阅读体验绝不逊色于官方的app。", "xinghongfei", "github", "https://github.com/xinghongfei/LookLook", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40005, "android-UniversalMusicPlayer", "这个开源项目展示了如何实现一个横跨各种Android平台的音乐播放器，包括手机，平板，汽车，手表，电视等。Google官方推出，跨平台开发必看项目。", "googlesamples", "github", "https://github.com/googlesamples/android-UniversalMusicPlayer", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40006, "plaid", "由谷歌工程师开发，展示Google Material风格设计，项目代码量大，但是结构清晰，还是很好理解的。", "nickbutcher", "github", "https://github.com/nickbutcher/plaid", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40007, "LoadingLayoutDemo", "项目里都会遇到几种页面，分别为加载中、无网络、无数据、出错四种情况", "weavey", "github", "https://github.com/weavey/LoadingLayoutDemo", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40008, "FanChat", "本项目是即时通讯的示例项目，使用了MVP模式，集成了环信SDK和Bmob后端云，展示了即时通讯基本功能的实现，包括注册登录，退出登录，联系人列表，添加好友，删除好友，收发消息，消息提醒等功能。", "uncleleonfan", "github", "https://github.com/uncleleonfan/FanChat", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40009, "ShareAndroidResource", "安卓库", "Lafree317", "github", "https://github.com/Lafree317/ShareAndroidResource", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40010, "LeafPic", "LeafPic是你可以尝试和学习的最佳开源相册程序之一。", "HoraApps", "github", "https://github.com/HoraApps/LeafPic", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40011, "Simple-Calendar", "一个完全用Kotlin实现的简单易用的日历app。", "SimpleMobileTools", "github", "https://github.com/SimpleMobileTools/Simple-Calendar", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40012, "AmazeFileManager", "安卓设备上另一个极其常见的应用是文件管理器。", "arpitkh96", "github", "https://github.com/arpitkh96/AmazeFileManager", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40013, "SoundRecorder", "一个简单，易用，漂亮的音频录制app。", "dkim0419", "github", "https://github.com/dkim0419/SoundRecorder", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40014, "MLManager", "MLManager是一个简单的app管理器。", "javiersantos", "github", "https://github.com/javiersantos/MLManager", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40015, "photo-affix", "一个非常简单，设计简约的app，用来垂直或者水平拼接图片", "afollestad", "github", "https://github.com/afollestad/photo-affix", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40016, "MovieGuide", "这个app的目的很简单，就是列出流行的电影以及它们的预告和影评。但是让这个项目有趣的东西是它实现的方式。", "esoxjem", "github", "https://github.com/esoxjem/MovieGuide", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40017, "AnExplorer", "一个简单的，轻量的文件管理器，为手机和平板而设计。", "1hakr", "github", "https://github.com/1hakr/AnExplorer", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40018, "Minimal-Todo", "安卓开发的绝大多数基础知识", "avjinder", "github", "https://github.com/avjinder/Minimal-Todo", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40019, "Timber", "Timber是一个设计漂亮，功能完善的音乐播放器", "naman14", "github", "https://github.com/naman14/Timber", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40020, "AnotherMonitor", "学习安卓进程，内存，CPU等系统管理方面的知识", "AntonioRedondo", "github", "https://github.com/AntonioRedondo/AnotherMonitor", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40021, "InstaMaterial", "学习与提高Material Design技术的项目", "frogermcs", "github", "https://github.com/frogermcs/InstaMaterial", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40022, "CoCoin", "CoCoin是一个功能完善的个人财物与机长工具，UI简单干净。", "Nightonke", "github", "https://github.com/Nightonke/CoCoin", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40023, "Omni-Notes", "功能全面的笔记类应用", "federicoiosue", "github", "https://github.com/federicoiosue/Omni-Notes", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40024, "Clip-Stack", "一个简单，干净，漂亮的剪切版管理app。这个项目非常小巧，简单，也很容易看懂。", "heruoxin", "github", "https://github.com/heruoxin/Clip-Stack", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40025, "superCleanMaster", "一键清理 开源版，包括内存加速，缓存清理，自启管理，软件管理等。", "joyoyao", "github", "https://github.com/joyoyao/superCleanMaster", 0, 0));


        mtKnowledgeList.add(new MTKnowledge(4, -1, 40026, "Travel-Mate", "一个旅途类，对地图和位置依赖很强的应用", "Swati4star", "github", "https://github.com/Swati4star/Travel-Mate", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40027, "KISS", "一个简单，快速，轻量的launcher应用。", "Neamar", "github", "https://github.com/Neamar/KISS", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40028, "turbo-editor", "一个简单但是强大的文字编辑应用。你还可以用这个app来写代码，支持不同语言的语法高亮。", "vmihalachi", "github", "https://github.com/vmihalachi/turbo-editor", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40029, "wally", "一个快速，简单，高效的wallpaper app。", "Musenkishi", "github", "https://github.com/Musenkishi/wally", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40030, "Pedometer", "一个简单，轻量的计步器app，使用硬件传感器计算步数，而且对电池的消耗非常小。", "j4velin", "github", "https://github.com/j4velin/Pedometer", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40031, "Colorful", "Colorful是一个简单又好用的帮助库，相对比为不同Activity设置不同主题（或者其他更复杂的情况），它允许你在任何时候用代码动态修改APP的基础颜色配置。", "garretyoder", "github", "https://github.com/garretyoder/Colorful", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40032, "WaveLoading", "想象一下阳光、微风、海浪和沙子在你指尖静静流淌。WaveLoading是一个能让你为任何drawable添加波浪动画的库。", "race604", "github", "https://github.com/race604/WaveLoading", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40033, "BufferTextInputLayout", "用户资料页，用来做密码或用户名的输入长度控制再适合不过，只需简单地指定字数和字数显示的增减性。", "bufferapp", "github", "https://github.com/bufferapp/BufferTextInputLayout", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40034, "pageloader", "为了减少网络的不稳定性给我们的APP可能带来的负面影响和糟糕体验，这个库是个不错的选择，样式时髦，使用简便。", "arieridwan8", "github", "https://github.com/arieridwan8/pageloader", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40035, "android-architecture", "Android官方MVP架构示例项目解析", "googlesamples", "github", "https://github.com/googlesamples/android-architecture", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40036, " Toasty", "一个创建自定义Toast的库。", "GrenderG", "github", "https://github.com/GrenderG/Toasty", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40037, "Store", "Store是一个异步加载和缓存库。", "NYTimes", "github", "https://github.com/NYTimes/Store", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40038, "PreviewSeekBar", "如果你使用Google Play Movies，你可能注意到了这个动画效果很棒，可以预览电影的SeekBar。 Rúben Sousa 实现了这种效果并开源。", "rubensousa", "github", "https://github.com/rubensousa/PreviewSeekBar", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40039, "chuck", "如果你使用 OkHttp的话，这个库可以帮助你拦截并记录所有的HTTP请求与响应。它还提供了一个来显示内容。", "jgilfelt", "github", "https://github.com/jgilfelt/chuck", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40040, "CoordinatorTabLayout", "CoordinatorTabLayout是一个自定义的组合控件，帮助你快速实现TabLayout与CoordinatorLayout相结合的样式。（微博个人主页的展示效果）", "hugeterry", "github", "https://github.com/hugeterry/CoordinatorTabLayout", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40041, "boxing", "Boxing是一个基于MVP模式的Android多媒体选择器，你可以图片选择（单／多选），预览或者剪裁图片。它还支持gif，视图选择，图片压缩以及自定义UI：", "Bilibili", "github", "https://github.com/Bilibili/boxing", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40042, "excelPanel", "RecyclerView实现的二维表格，不仅可以加载历史数据，还能加载新数据。", "zhouchaoyuan", "github", "https://github.com/zhouchaoyuan/excelPanel", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40043, "CameraFragment", "一个集成了拍照功能的Fragment ", "florent37", "github", "https://github.com/florent37/CameraFragment", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40044, "ArcNavigationView", "一个弧形的抽屉导航。", "rom4ek", "github", "https://github.com/rom4ek/ArcNavigationView", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40045, "ShimmerRecyclerView", "一个可以在加载数据的时候显示闪烁（Shimmer）的RecyclerView。这个RecyclerView内置一个adapter，控制shimmer的外观。", "sharish", "github", "https://github.com/sharish/ShimmerRecyclerView", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40046, "Android-SwitchIcon", "谷歌启动器风格的开关图标", "zagum", "github", "https://github.com/zagum/Android-SwitchIcon", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40047, "FlowLayout", "一个让子view在空间不够的情况下自动跳到下一行的布局。子view之间的间隔由FlowLayout计算出来，以便让view是均匀分布的。", "nex3z", "github", "https://github.com/nex3z/FlowLayout", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40048, "超过1000个精选的android代码", "攻城狮们寻找的开源项目", "泡在网上的日子", "github", "http://www.jcodecraeer.com/plus/list.php?tid=31", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(4, -1, 40049, "github排名开源项目", "github排名开源项目，时时刻刻去关注全球最新动态", "github", "github", "https://github.com/trending", 0, 0));

        return mtKnowledgeList;

//        mtKnowledgeList.add(new MTKnowledge(4, -1, 40050, "", "", "", "github", "", 0, 0));

    }

    /***
     * 面试数据的添加
     */
    public void addInterviewData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        面试数据的添加
        mtKnowledgeList = getInterviewData();

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //面试的添加
    private List<MTKnowledge> getInterviewData() {
        //        int mid;//某个一级分类下
//        int smid;//大分类下的小分类
//        int kid;//知识的id
//        String title;//知识标题
//        String desc;//项目描述
//        String author;//作者
//        String sorce;//来源
//        String url;//文章的链接
//        long ptime;//上传时间
//        long updatetime;//更新时间
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //面试 mid=5 smid=-1 kid=0以50000开头（分别依据mid=5来的）
        mtKnowledgeList.add(new MTKnowledge(5, -1, 50000, "国内一线互联网公司内部面试题库", "国内一线互联网公司内部面试题库", "墨镜猫", "csdn", "http://blog.csdn.net/rain_butterfly/article/details/51946197", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(5, -1, 50001, "LearningNotes", "Android 面试知识库", "GeniusVJR", "github", "https://github.com/GeniusVJR/LearningNotes", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(5, -1, 50002, "面试感悟", "一名3年工作经验的程序员应该具备的技能", "五月的仓颉", "博客园", "http://www.cnblogs.com/xrq730/p/5260294.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(5, -1, 50003, "Android面试重难点", "［Android基础］Android总结篇\"", "陶程", "csdn", "http://blog.csdn.net/codeemperor/article/details/51004189", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(5, -1, 50004, "亲爱的面试官，这个我可没看过！（Android部分）", "Android面试中高频率出现的题", "Maat红飞", "简书", "http://www.jianshu.com/p/89f19d67b348", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(5, -1, 50005, "Android 知识梳理", "知识梳理", "墨香", "github", "https://juejin.im/post/587dbaf9570c3522010e400e", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(5, -1, 50006, "2017已来，最全面试总结", "这些Android面试题你一定需要", " Java和Android架构", "weixin", "http://mp.weixin.qq.com/s?__biz=MzI0MjE3OTYwMg==&mid=2649548612&idx=1&sn=8e46b6dd47bd8577a5f7098aa0889098&chksm=f1180c39c66f852fd955a29a9cb4ffa9dc4d528cab524059bcabaf37954fa3f04bc52c41dae8&scene=21#wechat_redirect", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(5, -1, 50007, "Java面试题集", "java", "江湖人称小白哥", "csdn", "http://blog.csdn.net/dd864140130/article/details/55833087", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(5, -1, 50008, "Android面试题收集", "Android是一种基于Linux的自由及开放源代码的操作系统，主要使用于移动设备，如智能手机和平板电脑，由Google公司和开放手机联盟领导及开发。", "小路飞", "掘金", "https://juejin.im/post/58a6c38861ff4b0062ae4c25", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(5, -1, 50009, "史上最全 Android 面试资料集合", "面试技巧及简历", "G军仔", "简书", "http://www.jianshu.com/p/d1efe2f31b6d", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(5, -1, 50010, "40个Android面试题", "Android基础面试题", "切Dicky", "devstore", "https://juejin.im/timeline/android?sort=hottest", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(5, -1, 50011, "", "", "", "github", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(5, -1, 50012, "", "", "", "github", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(5, -1, 50013, "", "", "", "github", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(5, -1, 50014, "", "", "", "github", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(5, -1, 50015, "", "", "", "github", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(5, -1, 50016, "", "", "", "github", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(5, -1, 50017, "", "", "", "github", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(5, -1, 50018, "", "", "", "github", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(5, -1, 50019, "", "", "", "github", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(5, -1, 50020, "", "", "", "github", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(5, -1, 50021, "", "", "", "github", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(5, -1, 50022, "", "", "", "github", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(5, -1, 50023, "", "", "", "github", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(5, -1, 50024, "", "", "", "github", "", 0, 0));

        return mtKnowledgeList;
    }

    /***
     * java语法基础数据的添加
     */
    public void addJavaData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        java语法基础数据的添加
        mtKnowledgeList = getJavaData();

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    private List<MTKnowledge> getJavaData() {
        //        int mid;//某个一级分类下
//        int smid;//大分类下的小分类
//        int kid;//知识的id
//        String title;//知识标题
//        String desc;//项目描述
//        String author;//作者
//        String sorce;//来源
//        String url;//文章的链接
//        long ptime;//上传时间
//        long updatetime;//更新时间
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //java mid=0 smid=0 kid=0以0(mid)0(smid)000开头（分别依据mid=0来的）
        mtKnowledgeList.add(new MTKnowledge(0, 0, 00000, "java基础语法", "java基础语法", "runoob", "runoob", "http://www.runoob.com/java/java-basic-syntax.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(0, 0, 00001, "Java基础知识总结", "Java基础知识总结", "BYRans", "cnblogs", "http://www.cnblogs.com/BYRans/p/Java.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(0, 0, 00002, "面试总结-Java基础(一)", "java基础", "小爱_小世界", "简书", "http://www.jianshu.com/p/f3fdbcaf7e83", 0, 0));
        return mtKnowledgeList;
    }

    //    面向对象
    public void addOOData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        java语法基础数据的添加
        mtKnowledgeList = getOOData();

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    面向对象
    private List<MTKnowledge> getOOData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //java mid=0 smid=1 kid=0以0(mid)1(smid)000开头（分别依据mid=0来的）
        mtKnowledgeList.add(new MTKnowledge(0, 1, 00100, "面向对象", "面向对象", "百度百科", "百度百科", "http://baike.baidu.com/link?url=zMJRIsiYHctiQkWxs8hPzOTqVdb0_58gBqlapedqppxt9x8KcV1h0vZwb-h7lhTx67J7ZPMVZD_lwsrtyPgD4_4CWeF5YUWj0MnehHKVt1EEERWBcR9J4JNEsRqSsZOY", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(0, 1, 00101, "Java 继承", "Java 继承", "runoob", "runoob", "http://www.runoob.com/java/java-inheritance.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(0, 1, 00102, "Java 重写(Override)与重载(Overload)", "Java 重写(Override)与重载(Overload)", "runoob", "runoob", "http://www.runoob.com/java/java-override-overload.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(0, 1, 00103, "Java 多态", "Java 多态", "runoob", "runoob", "http://www.runoob.com/java/java-polymorphism.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(0, 1, 00104, "Java 抽象类", "Java 抽象类", "runoob", "runoob", "http://www.runoob.com/java/java-abstraction.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(0, 1, 00105, "Java 封装", "Java 封装", "runoob", "runoob", "http://www.runoob.com/java/java-encapsulation.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(0, 1, 00106, "Java 接口", "Java 接口", "runoob", "runoob", "http://www.runoob.com/java/java-interfaces.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(0, 1, 00107, "Java 包(package)", "Java 包(package)", "runoob", "runoob", "http://www.runoob.com/java/java-package.html", 0, 0));
        return mtKnowledgeList;
    }

    // 面向接口编程
    public void addOOPData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        java语法基础数据的添加
        mtKnowledgeList = getOOPData();

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    private List<MTKnowledge> getOOPData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //java mid=0 smid=2 kid=0以0(mid)2(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(0, 2, 00200, "面向接口编程", "面向接口编程", "百度百科", "百度百科", "http://baike.baidu.com/link?url=x8EuDvsoEWFqVYn8YgUq5VNEQoLQX8_Zcc8GvupfKehJNtowl5Rm94iJXho4tbMdg8cDXuFU7RYoTR5C3OGAOH6zj0CTrz9SKp6C9ax0vp-q3n4jmor1WosHbBZK66-dCnAupxfNGOVizYSz1ZWiwa", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(0, 2, 00201, "面向接口编程详解（一）", "思想基础", "CodingLabs", "博客园", "http://www.cnblogs.com/leoo2sk/archive/2008/04/10/1146447.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(0, 2, 00202, "面向接口编程详解（二）", "编程实例", "CodingLabs", "博客园", "http://kb.cnblogs.com/page/145704/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(0, 2, 00203, "一篇非常经典的文章（面向接口编程） ", "面向对象设计里有一点大家已基本形成共识，就是面向接口编程", "liugao_0614", "chinaunix", "http://blog.chinaunix.net/uid-20478213-id-1942005.html", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(0, 0, 02004, "", "", "", "", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(0, 0, 02005, "", "", "", "", "", 0, 0));
        return mtKnowledgeList;
    }

    //    开发环境
    public void addDevelopmentEnvironmentData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//       mid=1 smid=3 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10300, "Windows下快速搭建安卓开发环境android-studio", "Windows下快速搭建安卓开发环境android-studio", "百度经验", "百度经验", "http://jingyan.baidu.com/article/7082dc1c707976e40b89bd5b.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10301, "配置JAVA的环境变量", "配置JAVA的环境变量", "百度经验", "百度经验", "http://jingyan.baidu.com/article/f96699bb8b38e0894e3c1bef.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10303, "Android Studio系列教程一", "下载与安装", "stormzhang", "stormzhang.com", "http://stormzhang.com/devtools/2014/11/25/android-studio-tutorial1/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10304, "Android Studio系列教程二", "基本设置与运行", "stormzhang", "stormzhang.com", "http://stormzhang.com/devtools/2014/11/28/android-studio-tutorial2/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10305, "Android Studio系列教程三", "快捷键", "stormzhang", "stormzhang.com", "http://stormzhang.com/devtools/2014/12/09/android-studio-tutorial3/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10306, "Android Studio系列教程四", "Gradle基础", "stormzhang", "stormzhang.com", "http://stormzhang.com/devtools/2014/12/18/android-studio-tutorial4/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10307, "Android Studio系列教程五", "Gradle命令详解与导入第三方包", "stormzhang", "stormzhang.com", "http://stormzhang.com/devtools/2015/01/05/android-studio-tutorial5/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10308, "Android Studio系列教程六", "Gradle多渠道打包", "stormzhang", "stormzhang.com", "http://stormzhang.com/devtools/2015/01/15/android-studio-tutorial6/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10309, "Gradle自定义你的BuildConfig", "Gradle自定义你的BuildConfig", "stormzhang", "stormzhang.com", "http://stormzhang.com/android/2015/01/25/gradle-build-field/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10310, "Gradle依赖的统一管理", "Gradle依赖的统一管理", "stormzhang", "stormzhang.com", "http://stormzhang.com/android/2016/03/13/gradle-config/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10311, "学会编写Android Studio插件 别停留在用的程度了", "学会编写Android Studio插件 别停留在用的程度了", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/51548272", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10312, "神奇的Android Studio Template", "神奇的Android Studio Template", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/51592043", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10313, " Android Studio自定义模板 写页面竟然可以如此轻松", " Android Studio自定义模板 写页面竟然可以如此轻松", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/51635533", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10314, " Android Studio新功能解析，你真的了解Instant Run吗？", " Android Studio新功能解析，你真的了解Instant Run吗？", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/51271369", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10315, "从零开始的Android新项目2 - Gradle篇", "从零开始的Android新项目2 - Gradle篇", "MarkZhai", "简书", "http://www.jianshu.com/p/edd495d8efc8", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10316, "Gradle插件用户指南", "Gradle插件用户指南", "Rinvay Tang", "Rinvay Tang blog", "http://rinvay.github.io/android/2015/03/26/Gradle-Plugin-User-Guide%28Translation%29/#102", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10317, "解决AndroidStudio导入项目在 Building gradle project info 一直卡住", "解决AndroidStudio导入项目在 Building gradle project info 一直卡住", "LeBron_Six", "csdn", "http://blog.csdn.net/yyh352091626/article/details/51490976", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10318, "gradle 详解——你真的了解Gradle吗？", "gradle 详解——你真的了解Gradle吗？", "紫雾凌寒", "csdn", "http://blog.csdn.net/u013132758/article/details/52355915", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10319, "浅谈Instan Run中的热替换", "浅谈Instan Run中的热替换", "半栈工程师", "简书", "http://www.jianshu.com/p/5b7a1542ae47", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10321, "从Eclipse到AndroidStudio（一）初次启动", "从Eclipse到AndroidStudio（一）初次启动", "applixy", "简书", "http://www.jianshu.com/p/cc31cda29b79", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10322, "从Eclipse到AndroidStudio（二）界面介绍", "从Eclipse到AndroidStudio（二）界面介绍", "applixy", "简书", "http://www.jianshu.com/p/8498d4232dce", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10323, "从Eclipse到AndroidStudio（三）配置成你熟悉的操作", "从Eclipse到AndroidStudio（三）配置成你熟悉的操作", "applixy", "简书", "http://www.jianshu.com/p/1322452fbf12", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10324, "从Eclipse到AndroidStudio（四）Gradle基本配置", "从Eclipse到AndroidStudio（四）Gradle基本配置", "applixy", "简书", "http://www.jianshu.com/p/cd8fe9b16369", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10325, "从Eclipse到AndroidStudio（五）迁移一个eclipse工程到AndroidStudio有哪些坑", "从Eclipse到AndroidStudio（五）迁移一个eclipse工程到AndroidStudio有哪些坑", "applixy", "简书", "http://www.jianshu.com/p/a0fd23afe39f", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 3, 10326, "在AndroidStudio中自定义Gradle插件", "在AndroidStudio中自定义Gradle插件", "huachao1001", "csdn", "http://blog.csdn.net/huachao1001/article/details/51810328", 0, 0));


        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            基础知识
    public void addAndroidData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        mid=1 smid=4 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(1, 4, 10400, "Android样式的开发:shape篇", "一个应用，应该保持一套统一的样式，包括Button、EditText、ProgressBar、Toast、Checkbox等各种控件的样式，还包括控件间隔、文字大小和颜色、阴影等等。web的样式用css来定义，而android的样式主要则是通过shape、selector、layer-list、level-list、style、theme等组合实现。我将用一系列文章，循序渐进地讲解样式的每个方面该如何实现。第一个要讲的就是shape，最基础的形状定义工具。", "keeganlee", "http://keeganlee.me/", "http://keeganlee.me/post/android/20150830", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 4, 10401, "Android ADB命令?这一次我再也不死记了!【简单说】", "Android ADB命令?这一次我再也不死记了!【简单说】", "香脆的大鸡排", "简书", "http://www.jianshu.com/p/56fd03f1aaae", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 4, 10402, " JAVA虚拟机、Dalvik虚拟机和ART虚拟机简要对比", " JAVA虚拟机、Dalvik虚拟机和ART虚拟机简要对比", "炸斯特", "csdn", "http://blog.csdn.net/jason0539/article/details/50440669", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(0, 4, 10400, "", "", "", "", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(0, 4, 10400, "", "", "", "", "", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(0, 4, 10400, "", "", "", "", "", 0, 0));

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    四大组件
    public void add4UnitData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        mid=1 smid=5 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10500, "Android开发 - ActivityLifecycleCallbacks使用方法初探", "Android开发 - ActivityLifecycleCallbacks使用方法初探", "tongcpp", "csdn", "http://blog.csdn.net/tongcpp/article/details/40344871", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10501, "基础总结篇之一：Activity生命周期", "基础总结篇之一：Activity生命周期", "liuhe688", "csdn", "http://blog.csdn.net/liuhe688/article/details/6733407", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10502, "基础总结篇之二：Activity的四种launchMode", "基础总结篇之二：Activity的四种launchMode", "liuhe688", "csdn", "http://blog.csdn.net/liuhe688/article/details/6754323", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10503, "基础总结篇之三：Activity的task相关", "基础总结篇之三：Activity的task相关", "liuhe688", "csdn", "http://blog.csdn.net/liuhe688/article/details/6761337", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10504, "Android实训案例（七）——四大组件之一Service初步了解，实现通话录音功能，抽调接口", "Android实训案例（七）——四大组件之一Service初步了解，实现通话录音功能，抽调接口", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/51114434", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10505, "Android IntentService完全解析 当Service遇到Handler", "Android IntentService完全解析 当Service遇到Handler", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/47143563", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10506, "基础总结篇之四：Service完全解析", "基础总结篇之四：Service完全解析", "liuhe688", "csdn", "http://blog.csdn.net/liuhe688/article/details/6874378", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10507, "Android实训案例（六）——四大组件之一BroadcastReceiver的基本使用，拨号，短信，SD卡，开机，应用安装卸载监听", "Android实训案例（六）——四大组件之一BroadcastReceiver的基本使用，拨号，短信，SD卡，开机，应用安装卸载监听", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/51113053", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10508, "基础总结篇之五：BroadcastReceiver应用详解", "基础总结篇之五：BroadcastReceiver应用详解", "liuhe688", "csdn", "http://blog.csdn.net/liuhe688/article/details/6955668", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10509, "Android实训案例（五）——四大组件之一ContentProvider的使用，通讯录的实现以及ListView的优化", "Android实训案例（五）——四大组件之一ContentProvider的使用，通讯录的实现以及ListView的优化", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50573432", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10510, " 基础总结篇之六：ContentProvider之读写联系人", " 基础总结篇之六：ContentProvider之读写联系人", "liuhe688", "csdn", "http://blog.csdn.net/liuhe688/article/details/7006556", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10511, " 基础总结篇之七：ContentProvider之读写短消息", " 基础总结篇之七：ContentProvider之读写短消息", "liuhe688", "csdn", "http://blog.csdn.net/liuhe688/article/details/7020612", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10512, " 基础总结篇之八：创建及调用自己的ContentProvider", " 基础总结篇之八：创建及调用自己的ContentProvider", "liuhe688", "csdn", "http://blog.csdn.net/liuhe688/article/details/7050868", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10513, "你必须弄懂的Intent Filter匹配规则", "你必须弄懂的Intent Filter匹配规则", "猴子搬来的救兵Castiel", "csdn", "http://blog.csdn.net/mynameishuangshuai/article/details/51673273", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10514, "基础总结篇之九：Intent应用详解", "基础总结篇之九：Intent应用详解", "liuhe688", "csdn", "http://blog.csdn.net/liuhe688/article/details/7162988", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10515, "Android Context 上下文 你必须知道的一切", "Android Context 上下文 你必须知道的一切", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/40481055", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 5, 10516, " Android Context完全解析，你所不知道的Context的各种细节", " Android Context完全解析，你所不知道的Context的各种细节", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/47028975", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(0, 5, 10500, "", "", "", "", "", 0, 0));

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    常用控件/用户交互
    public void addAWTData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        mid=1 smid=6 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10600, "用SpannableString打造绚丽多彩的文本显示效果", "用SpannableString打造绚丽多彩的文本显示效果", "码农小阿飞", "简书", "http://www.jianshu.com/p/84067ad289d2", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10601, "android之LinearLayout", "android之LinearLayout", "jzp12", "csdn", "http://blog.csdn.net/jzp12/article/details/7590591", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10602, "Android：LinearLayout布局中Layout_weight的深刻理解", "Android：LinearLayout布局中Layout_weight的深刻理解", "※WYF※", "cnblogs", "http://www.cnblogs.com/w-y-f/p/4123056.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10603, "Android基本界面控件", "Android基本界面控件", "limingnihao", "iteye", "http://limingnihao.iteye.com/blog/851369", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10604, "【Android进阶】Listview分页加载数据的实现", "【Android进阶】Listview分页加载数据的实现", "赵凯强", "csdn", "http://blog.csdn.net/zhaokaiqiang1992/article/details/19417107", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10605, "【Android基础】listview控件的使用(3)------Map与SimpleAdapter组成的多显示条目的Listview", "【Android基础】listview控件的使用(3)------Map与SimpleAdapter组成的多显示条目的Listview", "赵凯强", "csdn", "http://blog.csdn.net/zhaokaiqiang1992/article/details/19540445", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10606, "【Android基础】listview控件的使用(1)------最简单的listview的使用", "【Android基础】listview控件的使用(1)------最简单的listview的使用", "赵凯强", "csdn", "http://blog.csdn.net/zhaokaiqiang1992/article/details/19493981", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10607, "Android ListView功能扩展，实现高性能的瀑布流布局", "Android ListView功能扩展，实现高性能的瀑布流布局", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/46361889", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10608, "Android ListView异步加载图片乱序问题，原因分析及解决方案", "Android ListView异步加载图片乱序问题，原因分析及解决方案", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/45586553", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10609, "Android ListView工作原理完全解析，带你从源码的角度彻底理解", "Android ListView工作原理完全解析，带你从源码的角度彻底理解", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/44996879", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10610, "Android中ListView分页加载数据", "Android中ListView分页加载数据", "liuhe688", "csdn", "http://blog.csdn.net/liuhe688/article/details/6852523", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10611, "Android高级控件（一）——ListView绑定CheckBox实现全选，增加和删除等功能", "Android高级控件（一）——ListView绑定CheckBox实现全选，增加和删除等功能", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50609604", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10612, "Android高级控件（五）——如何打造一个企业级应用对话列表,以QQ，微信为例", "Android高级控件（五）——如何打造一个企业级应用对话列表,以QQ，微信为例", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/51338613", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10613, "Android GridView如何适配不同屏幕", "Android GridView如何适配不同屏幕", "madreain", "csdn", "http://blog.csdn.net/madreain/article/details/52204862", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10614, "背景图片跟随手势滑动的ViewPager", "背景图片跟随手势滑动的ViewPager", "哲匠", "简书", "http://www.jianshu.com/p/0e6b1d785e4f", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10615, "开发绕不过的坑：你的 Bitmap 究竟占多大内存？", "开发绕不过的坑：你的 Bitmap 究竟占多大内存？", "bugly", "bugly", "http://bugly.qq.com/bbs/forum.php?mod=viewthread&tid=498d", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10616, "Fragment全解析系列（一）：那些年踩过的坑", "Fragment全解析系列（一）：那些年踩过的坑", "YoKey", "简书", "http://www.jianshu.com/p/d9143a92ad94", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10617, "Fragment全解析系列（二）：正确的使用姿势", "Fragment全解析系列（二）：正确的使用姿势", "YoKey", "简书", "http://www.jianshu.com/p/fd71d65f0ec6", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10618, "Fragment之我的解决方案：Fragmentation", "Fragment之我的解决方案：Fragmentation", "YoKey", "简书", "http://www.jianshu.com/p/38f7994faa6b", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10619, "实现 滑动退出 Fragment + Activity 二合一", "实现 滑动退出 Fragment + Activity 二合一", "YoKey", "简书", "http://www.jianshu.com/p/626229ca4dc2", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10620, "Fragment防手抖 另类实践", "Fragment防手抖 另类实践", "YoKey", "简书", "http://www.jianshu.com/p/9dbb03203fbc", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10621, "从源码角度分析，为什么会发生Fragment重叠？", "从源码角度分析，为什么会发生Fragment重叠？", "YoKey", "简书", "http://www.jianshu.com/p/78ec81b42f92", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10622, "9行代码让你App内的Fragment对重叠说再见", "9行代码让你App内的Fragment对重叠说再见", "YoKey", "简书", "http://www.jianshu.com/p/c12a98a36b2b", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10623, " Android Fragment 真正的完全解析（上）", " Android Fragment 真正的完全解析（上）", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/37970961", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10624, "Android Fragment 真正的完全解析（下）", "Android Fragment 真正的完全解析（下）", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/37992017", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10625, "Android Fragment 你应该知道的一切", "Android Fragment 你应该知道的一切", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/42628537", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10626, "android 实现截取 内容超过屏幕大小的长图", "android 实现截取 内容超过屏幕大小的长图", "madreain", "csdn", "http://blog.csdn.net/madreain/article/details/51778685", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10627, "「Android Tips」Toast 的一些使用技巧", "「Android Tips」Toast 的一些使用技巧", "_Ryeeeeee", "简书", "http://www.jianshu.com/p/2088216e65fb", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10628, "Android自定义Toast", "Android自定义Toast", "roykfw", "csdn", "http://blog.csdn.net/zhangweiwtmdbf/article/details/30031015", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10629, " Android中的Toast重复显示的问题", " Android中的Toast重复显示的问题", "尼古拉斯_赵四", "csdn", "http://blog.csdn.net/jiangwei0910410003/article/details/17096699", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10630, "【Android】当关闭通知消息权限后无法显示系统Toast的解决方案", "【Android】当关闭通知消息权限后无法显示系统Toast的解决方案", "BlinCheng", "csdn", "http://blog.csdn.net/qq_25867141/article/details/52807705", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10631, "Android 官方推荐 : DialogFragment 创建对话框", "Android 官方推荐 : DialogFragment 创建对话框", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/37815413/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10632, "Android之Dialog详解", "Android之Dialog详解", "溺水行舟", "csdn", "http://blog.csdn.net/liang5630/article/details/44098899", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10633, "Android PopupWindow详解", "Android PopupWindow详解", "泡在网上的日子", "泡在网上的日子", "http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2014/0702/1627.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10634, "Android-自定义PopupWindow", "Android-自定义PopupWindow", "IT_xiao小巫", "csdn", "http://blog.csdn.net/wwj_748/article/details/25653409", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10635, "PopupWindow的使用以及ArrayAdatper.notifyDataSetChanged()无效详解", "PopupWindow的使用以及ArrayAdatper.notifyDataSetChanged()无效详解", "xiaanming", "csdn", "http://blog.csdn.net/xiaanming/article/details/9121383", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10636, " 安卓 Notification 使用方法小结", " 安卓 Notification 使用方法小结", "zhaizu", "csdn", "http://blog.csdn.net/zhaizu/article/details/50550815", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10637, "Android通知栏微技巧，那些你所没关注过的小细节", "Android通知栏微技巧，那些你所没关注过的小细节", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/50945228", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10638, "Android 通知栏Notification的整合 全面学习 （一个DEMO让你完全了解它）", " Android 通知栏Notification的整合 全面学习 （一个DEMO让你完全了解它）", "vipra", "csdn", "http://blog.csdn.net/vipzjyno1/article/details/25248021", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10639, "Android HapticFeedback（震动反馈）介绍", "Android HapticFeedback（震动反馈）介绍", "HarryWeasley", "csdn", "http://blog.csdn.net/harryweasley/article/details/52806516", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 6, 10640, "Android 不得不说的VideoView的一些坑及其解决方案", "Android 不得不说的VideoView的一些坑及其解决方案", "madreain", "csdn", "http://blog.csdn.net/madreain/article/details/52143787", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(0, 6, 10600, "", "", "", "csdn", "", 0, 0));

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            网络访问
    public void addNetworkAccessData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        mid=1 smid=7 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10700, " HTTPS理论基础及其在Android中的最佳实践", " HTTPS理论基础及其在Android中的最佳实践", "孙群", "csdn", "http://blog.csdn.net/iispring/article/details/51615631", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10701, "Android访问网络，使用HttpURLConnection还是HttpClient？", "Android访问网络，使用HttpURLConnection还是HttpClient？", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/12452307", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10702, "Java / Android 基于Http的多线程下载的实现", "Java / Android 基于Http的多线程下载的实现", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/26994463", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10703, "深入理解HTTP协议、HTTP协议原理分析", "深入理解HTTP协议、HTTP协议原理分析", "Franck_LeeMH", "csdn", "http://blog.csdn.net/lmh12506/article/details/7794512", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10704, "GET和POST，有什么区别？", "GET和POST，有什么区别？", "木-叶", "cnblogs", "http://www.cnblogs.com/nankezhishi/archive/2012/06/09/getandpost.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10705, "JDK中的URLConnection参数详解", "JDK中的URLConnection参数详解", "supercrsky", "blogjava", "http://www.blogjava.net/supercrsky/articles/247449.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10706, "你应该知道的HTTP基础知识", "你应该知道的HTTP基础知识", "怪盗kidou", "简书", "http://www.jianshu.com/p/e544b7a76dac", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10707, "HTTP协议详解", "HTTP协议详解", "MIN飞翔", "cnblogs", "http://www.cnblogs.com/EricaMIN1987_IT/p/3837436.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10708, "Android OkHttp完全解析 是时候来了解OkHttp了", "Android OkHttp完全解析 是时候来了解OkHttp了", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/47911083", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10709, "一些你可能需要的okhttp实现", "一些你可能需要的okhttp实现", "唯鹿", "csdn", "http://blog.csdn.net/qq_17766199/article/details/53186874", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10710, "okhhtp", "okhhtp源码", "square", "github", "https://github.com/square/okhttp", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10711, "快速Android开发系列网络篇之Retrofit", "快速Android开发系列网络篇之Retrofit", "AngelDevil", "cnblogs", "http://www.cnblogs.com/angeldevil/p/3757335.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10712, "Retrofit2 完全解析 探索与okhttp之间的关系", "Retrofit2 完全解析 探索与okhttp之间的关系", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/51304204", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10713, "深入浅出 Retrofit，这么牛逼的框架你们还不来看看？", "深入浅出 Retrofit，这么牛逼的框架你们还不来看看？", "bugly", "bugly", "http://bugly.qq.com/bbs/forum.php?mod=viewthread&tid=1117#rd", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10714, "Android Retrofit 2.0 使用-补充篇", "Android Retrofit 2.0 使用-补充篇", "wuxiaolong", "wuxiaolong.me", "http://wuxiaolong.me/2016/06/18/retrofits/#rd?nsukey=5369oC0I0ZYWRBzDr%2B0c0xQpdauYhwDn%2BwYRcC4kNRqYeJg0u1ClVVDT25%2F5%2F2CfO4FaPQfUR93apZVMUYAhKw%3D%3D", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10715, "android:json解析的两个工具：Gson和Jackson的使用小例子", "android:json解析的两个工具：Gson和Jackson的使用小例子", "郑海波", "csdn", "http://blog.csdn.net/nupt123456789/article/details/25654669", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10716, "你真的会用Gson吗?Gson使用指南（一）", "你真的会用Gson吗?Gson使用指南（一）", "怪盗kidou", "简书", "http://www.jianshu.com/p/e740196225a4", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10717, "你真的会用Gson吗?Gson使用指南（二）", "你真的会用Gson吗?Gson使用指南（二）", "怪盗kidou", "简书", "http://www.jianshu.com/p/c88260adaf5e", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10718, "你真的会用Gson吗?Gson使用指南（三）", "你真的会用Gson吗?Gson使用指南（三）", "怪盗kidou", "简书", "http://www.jianshu.com/p/0e40a52c0063", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 7, 10719, "你真的会用Gson吗?Gson使用指南（四）", "你真的会用Gson吗?Gson使用指南（四）", "怪盗kidou", "简书", "http://www.jianshu.com/p/3108f1e44155", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(0, 7, 10700, "", "", "", "csdn", "", 0, 0));

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    图片加载
    public void addPhotoData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        mid=1 smid=8 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(1, 8, 10800, "Android高效加载大图、多图解决方案，有效避免程序OOM", "Android高效加载大图、多图解决方案，有效避免程序OOM", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/9316683", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 8, 10801, "Android 高清加载巨图方案 拒绝压缩图片", "Android 高清加载巨图方案 拒绝压缩图片", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/49300989/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 8, 10802, "Android BitmapShader 实战 实现圆形、圆角图片", "Android BitmapShader 实战 实现圆形、圆角图片", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/41967509", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 8, 10803, "Android Xfermode 实战 实现圆形、圆角图片", "Android Xfermode 实战 实现圆形、圆角图片", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/42094215", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 8, 10804, "glide", "glide源码及其使用方法", "bumptech", "github", "https://github.com/bumptech/glide", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 8, 10805, "fresco", "fresco源码及其使用方法", "facebook", "github", "https://github.com/facebook/fresco", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 8, 10806, "基于七牛和fresco的一整套安卓图片解决方案", "基于七牛和fresco的一整套安卓图片解决方案", "hss01248", "csdn", "http://blog.csdn.net/hss01248/article/details/52163969", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(0, 8, 10800, "", "", "", "csdn", "", 0, 0));

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            数据存储
    public void addDataStorageData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        mid=1 smid=9 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(1, 9, 10900, "Android DiskLruCache完全解析，硬盘缓存的最佳方案", "Android DiskLruCache完全解析，硬盘缓存的最佳方案", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/28863651", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 9, 10901, "Android照片墙完整版，完美结合LruCache和DiskLruCache", "Android照片墙完整版，完美结合LruCache和DiskLruCache", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/34093441", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 9, 10902, "Android DiskLruCache 源码解析 硬盘缓存的绝佳方案", "Android DiskLruCache 源码解析 硬盘缓存的绝佳方案", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/47251585", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 9, 10903, "Android中SQLite应用详解", "Android中SQLite应用详解", "liuhe688", "csdn", "http://blog.csdn.net/liuhe688/article/details/6715983/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 9, 10904, "Realm for Android详细教程", "Realm for Android详细教程", "RaphetS", "csdn", "http://www.jianshu.com/p/28912c2f31db#", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 9, 10905, "ormlite-android", "ormlite-android", "j256", "github", "https://github.com/j256/ormlite-android", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 9, 10906, "Android 快速开发系列 ORMLite 框架最佳实践", "Android 快速开发系列 ORMLite 框架最佳实践", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/39122981", 0, 0));

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    动画效果
    public void addAnimationData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        mid=1 smid=10 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(1, 10, 11000, "Android 中的转场动画及兼容处理", "Android 中的转场动画及兼容处理", "湫水", "csdn", "http://blog.csdn.net/wl9739/article/details/52833668", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 10, 11001, "Android 属性动画（Property Animation） 完全解析 （上）", "Android 属性动画（Property Animation） 完全解析 （上）", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/38067475", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 10, 11002, "Android 属性动画（Property Animation） 完全解析 （下）", "Android 属性动画（Property Animation） 完全解析 （下）", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/38092093", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 10, 11003, "Android动画效果之Tween Animation（补间动画）", "Android动画效果之Tween Animation（补间动画）", "总李写代码", "cnblogs", "http://www.cnblogs.com/whoislcj/p/5730520.html", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(0, 10, 11000, "", "", "", "csdn", "", 0, 0));

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            自定义控件
    public void addCustomerData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        mid=1 smid=11 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11100, "自定义View系列教程01--常用工具介绍", "自定义View系列教程01--常用工具介绍", "lfdfhl", "csdn", "http://blog.csdn.net/lfdfhl/article/details/51324275", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11101, "自定义View系列教程02--onMeasure源码详尽分析", "自定义View系列教程02--onMeasure源码详尽分析", "lfdfhl", "csdn", "http://blog.csdn.net/lfdfhl/article/details/51347818", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11102, "自定义View系列教程03--onLayout源码详尽分析", "自定义View系列教程03--onLayout源码详尽分析", "lfdfhl", "csdn", "http://blog.csdn.net/lfdfhl/article/details/51393131", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11103, "自定义View系列教程04--Draw源码分析及其实践", "自定义View系列教程04--Draw源码分析及其实践", "lfdfhl", "csdn", "http://blog.csdn.net/lfdfhl/article/details/51435968", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11104, "MeasureSpec的理解和详尽源码分析", "MeasureSpec的理解和详尽源码分析", "lfdfhl", "csdn", "http://blog.csdn.net/lfdfhl/article/details/50880382", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11105, "Android 自定义View属性相关细节", "Android 自定义View属性相关细节", "鸿洋", "weixin", "http://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650820236&idx=1&sn=6dec4ff1efeda3224b5a40fdad862404&scene=0#wechat_redirect", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11106, "Android绘图机制（一）——自定义View的基础属性和方法", "Android绘图机制（一）——自定义View的基础属性和方法", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50457413", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11107, "Android中自定义样式与View的构造函数中的第三个参数defStyle的意义", "Android中自定义样式与View的构造函数中的第三个参数defStyle的意义", "AngelDevil", "cnblogs", "http://www.cnblogs.com/angeldevil/p/3479431.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11108, "安卓自定义View基础-坐标系", "安卓自定义View基础-坐标系", "GcsSloop", "csdn", "http://blog.csdn.net/u013831257/article/details/50464152", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11109, "安卓自定义View基础-颜色", "安卓自定义View基础-颜色", "GcsSloop", "csdn", "http://blog.csdn.net/u013831257/article/details/50494832", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11110, "Android 深入理解Android中的自定义属性", "Android 深入理解Android中的自定义属性", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/45022631/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11111, "Android中attrs.xml文件的使用详解", "Android中attrs.xml文件的使用详解", "程序猿大雄", "csdn", "http://blog.csdn.net/jiangwei0910410003/article/details/17006087", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11112, "Android FrameWork——Touch事件派发过程详解", "Android FrameWork——Touch事件派发过程详解", "stonecao", "csdn", "http://blog.csdn.net/stonecao/article/details/6759189", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11113, "自定义View系列教程06--详解View的Touch事件处理", "自定义View系列教程06--详解View的Touch事件处理", "lfdfhl", "csdn", "http://blog.csdn.net/lfdfhl/article/details/51559847", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11114, "自定义View系列教程07--详解ViewGroup分发Touch事件", "自定义View系列教程07--详解ViewGroup分发Touch事件", "lfdfhl", "csdn", "http://blog.csdn.net/lfdfhl/article/details/51603088", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11115, "自定义View系列教程08--滑动冲突的产生及其处理", "自定义View系列教程08--滑动冲突的产生及其处理", "lfdfhl", "csdn", "http://blog.csdn.net/lfdfhl/article/details/51656492", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11116, "Android绘图机制（二）——自定义View绘制形, 圆形, 三角形, 扇形, 椭圆, 曲线,文字和图片的坐标讲解", "Android绘图机制（二）——自定义View绘制形, 圆形, 三角形, 扇形, 椭圆, 曲线,文字和图片的坐标讲解", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50466655", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11117, "安卓自定义View进阶 － Path之完结篇(伪)", "安卓自定义View进阶 － Path之完结篇(伪)", "GcsSloop", "csdn", "http://blog.csdn.net/u013831257/article/details/51477575", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11118, "安卓自定义View进阶-Canvas之绘制基本形状", "安卓自定义View进阶-Canvas之绘制基本形状", "GcsSloop", "csdn", "http://blog.csdn.net/u013831257/article/details/50556098", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11119, "安卓自定义View进阶-Canvas之画布操作", "安卓自定义View进阶-Canvas之画布操作", "GcsSloop", "csdn", "http://blog.csdn.net/u013831257/article/details/50599912", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11120, "安卓自定义View进阶-Canvas之图片文字", "安卓自定义View进阶-Canvas之图片文字", "GcsSloop", "csdn", "http://blog.csdn.net/u013831257/article/details/50654494", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11121, "安卓自定义View进阶-Path基本操作", "安卓自定义View进阶-Path基本操作", "GcsSloop", "csdn", "http://blog.csdn.net/u013831257/article/details/50784565", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11122, "安卓自定义View进阶 - 贝塞尔曲线", "安卓自定义View进阶 - 贝塞尔曲线", "GcsSloop", "csdn", "http://blog.csdn.net/u013831257/article/details/51281136", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11123, "安卓自定义View进阶-Path之玩出花样(PathMeasure)", "安卓自定义View进阶-Path之玩出花样(PathMeasure)", "GcsSloop", "csdn", "http://blog.csdn.net/u013831257/article/details/51565591", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11124, " Android高级控件（二）——SurfaceView实现GIF动画架包，播放GIF动画，自己实现功能的初体现", " Android高级控件（二）——SurfaceView实现GIF动画架包，播放GIF动画，自己实现功能的初体现", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50662307", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11125, "自定义View系列教程05--示例分析", "自定义View系列教程05--示例分析", "lfdfhl", "csdn", "http://blog.csdn.net/lfdfhl/article/details/51508727", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11126, "自定义View实例(二)----一步一步教你实现QQ健康界面", "自定义View实例(二)----一步一步教你实现QQ健康界面", "Young_Kai", "csdn", "http://blog.csdn.net/tyk0910/article/details/51594479", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11127, "Android自定义控件3——边缘凹凸的卡劵效果View", "Android自定义控件3——边缘凹凸的卡劵效果View", "yissan", "csdn", "http://blog.csdn.net/yissan/article/details/51429281#rd", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11128, "http://blog.csdn.net/qq_26787115/article/details/50439020", "http://blog.csdn.net/qq_26787115/article/details/50439020", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50439020", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11129, "Android特效专辑(五)——自定义圆形头像和仿MIUI卸载动画—粒子爆炸", "Android特效专辑(五)——自定义圆形头像和仿MIUI卸载动画—粒子爆炸", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50539538", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11130, "Android特效专辑（六）——仿QQ聊天撒花特效，无形装逼，最为致命", "Android特效专辑（六）——仿QQ聊天撒花特效，无形装逼，最为致命", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50544180", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11131, "Android特效专辑（七）——飞机升空特效，一键清理缓存，灵活运用动画会有不一样的感受", "Android特效专辑（七）——飞机升空特效，一键清理缓存，灵活运用动画会有不一样的感受", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50549377", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11132, "Android特效专辑（八）——实现心型起泡飞舞的特效，让你的APP瞬间暖心", "Android特效专辑（八）——实现心型起泡飞舞的特效，让你的APP瞬间暖心", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50609167", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11133, "Android特效专辑（九）——仿微信雷达搜索好友特效，逻辑清晰实现简单", "Android特效专辑（九）——仿微信雷达搜索好友特效，逻辑清晰实现简单", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50662038", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11134, "Android特效专辑（十）——点击水波纹效果实现，逻辑清晰实现简单", "Android特效专辑（十）——点击水波纹效果实现，逻辑清晰实现简单", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50670291", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11135, "Android特效专辑（十一）——仿水波纹流量球进度条控制器，实现高端大气的主流特效", "Android特效专辑（十一）——仿水波纹流量球进度条控制器，实现高端大气的主流特效", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50688463", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 11, 11136, "Android特效专辑（十二）——仿支付宝咻一咻功能实现波纹扩散特效，精细小巧的View", "Android特效专辑（十二）——仿支付宝咻一咻功能实现波纹扩散特效，精细小巧的View", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50706529", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(0, 11, 11100, "", "", "", "csdn", "", 0, 0));

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    多媒体技术/资源文件/硬件模块
    public void addResData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        mid=1 smid=12 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(1, 12, 11200, "你需要知道的Android拍照适配方案", "你需要知道的Android拍照适配方案", "D_clock爱吃葱花", "简书", "http://www.jianshu.com/p/f269bcda335f", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 12, 11201, "android调用系统相机拍照", "android调用系统相机拍照", "YougaKing", "oschina", "https://my.oschina.net/u/2438532/blog/743160", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 12, 11202, "Android Camera开发系列（上）——Camera的基本调用与实现拍照功能以及获取拍照图片加载大图片", "Android Camera开发系列（上）——Camera的基本调用与实现拍照功能以及获取拍照图片加载大图片", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50583482", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 12, 11203, "Android Camera开发系列（下）——自定义Camera实现拍照查看图片等功能", "Android Camera开发系列（下）——自定义Camera实现拍照查看图片等功能", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50586554", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 12, 11204, "Android高级控件（四）——VideoView 实现引导页播放视频欢迎效果，超级简单却十分的炫酷", "Android高级控件（四）——VideoView 实现引导页播放视频欢迎效果，超级简单却十分的炫酷", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50688984", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 12, 11205, "Android闹钟设置的解决方案", "Android设置闹钟并不像IOS那样这么简单，做过Android设置闹钟的开发者都知道里面的坑有多深。下面记录一下，我解决Android闹钟设置的解决方案。", "HanWen", "简书", "http://www.jianshu.com/p/1f919c6eeff6", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 12, 11206, "Android蓝牙自动配对Demo，亲测好使！！！", "Android蓝牙自动配对Demo，亲测好使！！！", "温柔狠角色", "csdn", "http://blog.csdn.net/qq_25827845/article/details/52400782", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 12, 11207, "Android BLE与终端通信（一）——Android Bluetooth基础API以及简单使用获取本地蓝牙名称地址", "Android BLE与终端通信（一）——Android Bluetooth基础API以及简单使用获取本地蓝牙名称地址", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50551197", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 12, 11208, "Android BLE与终端通信（二）——Android Bluetooth基础科普以及搜索蓝牙设备显示列表", "Android BLE与终端通信（二）——Android Bluetooth基础科普以及搜索蓝牙设备显示列表", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50551231", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 12, 11209, "Android BLE与终端通信（三）——客户端与服务端通信过程以及实现数据通信", "Android BLE与终端通信（三）——客户端与服务端通信过程以及实现数据通信", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50557505", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 12, 11210, "Android BLE与终端通信（四）——实现服务器与客户端即时通讯功能", "Android BLE与终端通信（四）——实现服务器与客户端即时通讯功能", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50596299", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 12, 11211, "Android BLE与终端通信(五)——Google API BLE4.0低功耗蓝牙文档解读之案例初探", "Android BLE与终端通信(五)——Google API BLE4.0低功耗蓝牙文档解读之案例初探", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/51397898", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(0, 12, 11200, "", "", "", "csdn", "", 0, 0));

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            JNI
    public void addJNIData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        mid=1 smid=13 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(1, 13, 11300, "关于Android的.so文件你所需要知道的", "关于Android的.so文件你所需要知道的", "asce1885", "简书", "http://www.jianshu.com/p/cb05698a1968", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 13, 11301, "让APK只包含指定的ABI", "让APK只包含指定的ABI", "justFWD", "csdn", "http://blog.csdn.net/justfwd/article/details/49308199", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 13, 11302, "Android JNI原理分析", "Android JNI原理分析", "gityuan", "gityuan.com", "http://gityuan.com/2016/05/28/android-jni/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 13, 11303, "Android NDK学习笔记4-Android.mk篇", "Android NDK学习笔记4-Android.mk篇", "猴子搬来的救兵Castiel", "csdn", "http://blog.csdn.net/mynameishuangshuai/article/details/52577228", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 13, 11304, "Android NDK学习笔记5-JNI数据类型总结", "Android NDK学习笔记5-JNI数据类型总结", "猴子搬来的救兵Castiel", "csdn", "http://blog.csdn.net/mynameishuangshuai/article/details/52584713", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(0, 13, 11300, "", "", "", "csdn", "", 0, 0));

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    常用第三方库/常用第三方sdk
    public void addSDKData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        mid=1 smid=14 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(1, 14, 11400, "Android高级控件（三）—— 使用Google ZXing实现二维码的扫描和生成相关功能体系", "Android高级控件（三）—— 使用Google ZXing实现二维码的扫描和生成相关功能体系", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/50677143", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 14, 11401, "Github项目解析（十）-->几行代码快速集成二维码扫描库", "Github项目解析（十）-->几行代码快速集成二维码扫描库", "一片枫叶_刘超", "csdn", "http://blog.csdn.net/qq_23547831/article/details/52037710", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 14, 11402, "dagger 2 详解", "dagger 2 详解", "梅魂竹梦", "简书", "http://www.jianshu.com/p/269c3f70ec1e", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 14, 11403, "Android通用流行框架大全", "Android通用流行框架大全", "lavor", "https://segmentfault.com/", "https://segmentfault.com/a/1190000005073746", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 14, 11404, "Android开源特效常用链接大集合", "Android开源特效常用链接大集合", "开发者的乐趣JRT", "csdn", "http://blog.csdn.net/jiang_rong_tao/article/details/51436991", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 14, 11405, "Andoird Crash的跟踪方法，使用腾讯Bugly来捕捉一些疑难杂症，让我们APP稳定上线", "Andoird Crash的跟踪方法，使用腾讯Bugly来捕捉一些疑难杂症，让我们APP稳定上线", "刘某人程序员", "csdn", "http://blog.csdn.net/qq_26787115/article/details/51615578", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(0, 14, 11400, "", "", "", "csdn", "", 0, 0));

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            常用工具类
    public void addUtilsData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        mid=1 smid=15 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(1, 15, 11500, "AndroidUtilCode", "Android开发人员不得不收集的代码(持续更新中)", "Blankj", "github", "https://github.com/Blankj/AndroidUtilCode", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 15, 11501, "android-utils", "Android工具类库", "jingle1267", "github", "https://github.com/jingle1267/android-utils", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(0, 15, 11500, "", "", "", "csdn", "", 0, 0));

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    APP优化（性能优化）
    public void addAPPData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
//        mid=1 smid=16 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(1, 16, 11600, "其实你不知道MultiDex到底有多坑", "其实你不知道MultiDex到底有多坑", "总悟君", "http://www.open-open.com/", "http://www.open-open.com/lib/view/open1452264136714.html%20%E2%80%9C%E5%85%B6%E5%AE%9E%E4%BD%A0%E4%B8%8D%E7%9F%A5%E9%81%93MultiDex%E5%88%B0%E5%BA%95%E6%9C%89%E5%A4%9A%E5%9D%91%E2%80%9D", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 16, 11601, "美团Android DEX自动拆包及动态加载简介", "美团Android DEX自动拆包及动态加载简介", "美团点评技术团队", "http://tech.meituan.com/", "http://tech.meituan.com/mt-android-auto-split-dex.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 16, 11602, "配置方法数超过 64K 的应用", "配置方法数超过 64K 的应用", "developer", "https://developer.android.com", "https://developer.android.com/studio/build/multidex.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 16, 11603, "Android MultiDex初次启动APP优化", "Android MultiDex初次启动APP优化", "Synaric", "csdn", "http://blog.csdn.net/synaric/article/details/53540760", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 16, 11604, "Android最佳性能实践(一)——合理管理内存", "Android最佳性能实践(一)——合理管理内存", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/42238627", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 16, 11605, "Android最佳性能实践(二)——分析内存的使用情况", "Android最佳性能实践(二)——分析内存的使用情况", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/42238633", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 16, 11606, "Android最佳性能实践(三)——高性能编码优化", "Android最佳性能实践(三)——高性能编码优化", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/42318689", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 16, 11607, "Android最佳性能实践(四)——布局优化技巧", "Android最佳性能实践(四)——布局优化技巧", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/43376527", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 16, 11608, "Android客户端性能优化（魅族资深工程师毫无保留奉献）", "Android客户端性能优化（魅族资深工程师毫无保留奉献）", "degao", "tingyun", "http://blog.tingyun.com/web/article/detail/155", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 16, 11609, "Android 清除无用资源", "Android 清除无用资源", "madreain", "csdn", "http://blog.csdn.net/madreain/article/details/52661617", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 16, 11610, "如何准确评测Android应用的流畅度？", "如何准确评测Android应用的流畅度？", "bugly", "bugly", "http://bugly.qq.com/bbs/forum.php?mod=viewthread&tid=33&extra=page%3D5", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 16, 11611, "使用新版Android Studio检测内存泄露和性能", "使用新版Android Studio检测内存泄露和性能", "于连林520wcf", "简书", "http://www.jianshu.com/p/216b03c22bb8", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 16, 11612, "Android性能调优利器StrictMode", "Android性能调优利器StrictMode", "技术小黑屋", "技术小黑屋", "http://droidyue.com/blog/2015/09/26/android-tuning-tool-strictmode/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 16, 11613, "App瘦身最佳实践", "App瘦身最佳实践", "天之界线2010", "简书", "http://www.jianshu.com/p/8f14679809b3#", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(1, 16, 11614, "Android 中如何计算 App 的启动时间？", "Android 中如何计算 App 的启动时间？", "Gracker", "http://androidperformance.com/", "http://androidperformance.com/2015/12/31/How-to-calculation-android-app-lunch-time.html", 0, 0));


        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }


    //    网络请求
    public void addNetWorkRequestData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=2 smid=17 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(2, 17, 21700, "okhttp", "okhttp源码", "square", "github", "https://github.com/square/okhttp/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 17, 21701, "okhttputils", "okhttp的辅助类 ", "hongyangAndroid", "github", "https://github.com/hongyangAndroid/okhttputils", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 17, 21702, "retrofit", "retrofit源码", "square", "github", "https://github.com/square/retrofit", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 17, 21703, "Retrofit用法详解", "Retrofit用法详解", "蓝田大营", "csdn", "http://blog.csdn.net/duanyy1990/article/details/52139294", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 17, 21704, "RxVolley", "RxVolley = Volley + RxJava(RxJava2.0) + OkHttp(OkHttp3) ", "kymjs", "github", "https://github.com/kymjs/RxVolley", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 17, 21705, "android-volley", "android-volley", "mcxiaoke", "github", "https://github.com/mcxiaoke/android-volley", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 17, 21706, "android-async-http", "android-async-http", "loopj", "github", "https://github.com/loopj/android-async-http", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(2, 17, 11700, "", "", "", "csdn", "", 0, 0));

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            图片加载
    public void addPhotoLoadData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=2 smid=18 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(2, 18, 21800, "glide", "glide源码", "bumptech", "github", "https://github.com/bumptech/glide", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 18, 21801, "picasso", "picasso源码", "square", "github", "https://github.com/square/picasso", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 18, 21802, "fresco", "fresco源码", "facebook", "github", "https://github.com/facebook/fresco", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 18, 21803, "Android-Universal-Image-Loader", "Android-Universal-Image-Loader源码", "nostra13", "github", "https://github.com/nostra13/Android-Universal-Image-Loader", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(2, 18, 11800, "", "", "", "csdn", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    数据库
    public void addSQLData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=2 smid=19 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(2, 19, 21900, "ormlite-android", "ormlite-android源码", "j256", "github", "https://github.com/j256/ormlite-android", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 19, 21901, "Android 快速开发系列 ORMLite 框架最佳实践", "Android 快速开发系列 ORMLite 框架最佳实践", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/39122981", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 19, 21902, "android-lite-orm", "android-lite-orm", "litesuits", "github", "https://github.com/litesuits/android-lite-orm", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 19, 21903, "greenDAO", "greenDAO源码", "greenrobot", "github", "https://github.com/greenrobot/greenDAO", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 19, 21904, "LitePal", "LitePal源码", "LitePalFramework", "github", "https://github.com/LitePalFramework/LitePal", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 19, 21905, "realm-java", "realm-java源码", "realm", "github", "https://github.com/realm/realm-java", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 19, 21906, "ActiveAndroid", "ActiveAndroid源码", "pardom", "github", "https://github.com/pardom/ActiveAndroid", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 19, 21907, "sqlbrite", "sqlbrite源码", "square", "github", "https://github.com/square/sqlbrite", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(2, 19, 11900, "", "", "", "csdn", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            数据解析
    public void addJSONData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=2 smid=20 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(2, 20, 22000, "gson", "gson", "google", "github", "https://github.com/google/gson", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 20, 22001, "fastjson", "fastjson", "alibaba", "github", "https://github.com/alibaba/fastjson", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 20, 22002, "jsoup", "jsoup", "jhy", "github", "https://github.com/jhy/jsoup", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(2, 20, 12000, "", "", "", "csdn", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    下拉刷新
    public void addRefreshData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=2 smid=21 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(2, 21, 22100, "Android-PullToRefresh", "Android-PullToRefresh", "chrisbanes", "github", "https://github.com/chrisbanes/Android-PullToRefresh", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 21, 22101, "android-Ultra-Pull-To-Refresh", "android-Ultra-Pull-To-Refresh", "liaohuqiu", "github", "https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 21, 22102, "android-pulltorefresh", "android-pulltorefresh", "johannilsson", "github", "https://github.com/johannilsson/android-pulltorefresh", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 21, 22103, "CommonPullToRefresh", "CommonPullToRefresh", "Chanven", "github", "https://github.com/Chanven/CommonPullToRefresh", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 21, 22104, "XListView-Android", "XListView-Android", "Maxwin-z", "github", "https://github.com/Maxwin-z/XListView-Android", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 21, 22105, "Android-PullToRefresh-SwipeMenuListView-Sample", "Android-PullToRefresh-SwipeMenuListView-Sample", "licaomeng", "github", "https://github.com/licaomeng/Android-PullToRefresh-SwipeMenuListView-Sample", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 21, 22106, "RecyclerRefreshLayout", "RecyclerRefreshLayout", "dinuscxj", "github", "https://github.com/dinuscxj/RecyclerRefreshLayout", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 21, 22107, "", "", "", "github", "https://github.com/AndreiD/UltimateAndroidAppTemplate", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(2, 21, 12100, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            导航栏
    public void addBarData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=2 smid=22 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(2, 22, 22200, "SwipeBackFragment", "SwipeBackFragment", "YoKeyword", "github", "https://github.com/YoKeyword/SwipeBackFragment", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 22, 22201, "SwipeBackLayout", "SwipeBackLayout", "ikew0ng", "github", "https://github.com/ikew0ng/SwipeBackLayout", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 22, 22202, "SwipeBackLayout的一个让人揪心的问题", "SwipeBackLayout的一个让人揪心的问题", "ownwell", "github", "http://ownwell.github.io/2015/09/30/swipeback-windowIsTranslucent/", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(2, 22, 12203, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    对话框
    public void addDialoData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=2 smid=23 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(2, 23, 22300, "SwipeAwayDialog", "SwipeAwayDialog", "kakajika", "github", "https://github.com/kakajika/SwipeAwayDialog", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 23, 22301, "dialogplus", "dialogplus", "orhanobut", "github", "https://github.com/orhanobut/dialogplus", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 23, 22302, "MaterialDialog", "MaterialDialog", "drakeet", "github", "https://github.com/drakeet/MaterialDialog", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 23, 22303, "android-FingerprintDialog", "android-FingerprintDialog", "googlesamples", "github", "https://github.com/googlesamples/android-FingerprintDialog", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 23, 22304, "android-styled-dialogs", "android-styled-dialogs", "avast", "github", "https://github.com/avast/android-styled-dialogs", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 23, 22305, "FlycoDialog_Master", "FlycoDialog_Master", "H07000223", "github", "https://github.com/H07000223/FlycoDialog_Master", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 23, 22306, "sweet-alert-dialog", "sweet-alert-dialog", "pedant", "github", "https://github.com/pedant/sweet-alert-dialog", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 23, 22307, "TimePickerDialog", "TimePickerDialog", "JZXiang", "github", "https://github.com/JZXiang/TimePickerDialog", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(2, 23, 12300, "", "", "", "github", "", 0, 0));


        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            依赖注入
    public void addDIData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=2 smid=24 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(2, 24, 22400, "butterknife", "butterknife", "JakeWharton", "github", "https://github.com/JakeWharton/butterknife", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 24, 22401, "dagger", "dagger", "google", "github", "https://github.com/google/dagger", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 24, 22402, "androidannotations", "androidannotations", "androidannotations", "github", "https://github.com/androidannotations/androidannotations", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 24, 22403, "roboguice", "roboguice", "roboguice", "github", "https://github.com/roboguice/roboguice", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(2, 24, 12400, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    事件传递
    public void addEventData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=2 smid=25 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(2, 25, 22500, "EventBus", "EventBus", "greenrobot", "github", "https://github.com/greenrobot/EventBus", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 25, 22501, "otto", "otto", "square", "github", "https://github.com/square/otto", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(2, 25, 12500, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            日志框架
    public void addLogData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=2 smid=26 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(2, 26, 22600, "logger", "logger", "orhanobut", "github", "https://github.com/orhanobut/logger", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 26, 22601, "hugo", "hugo", "JakeWharton", "github", "https://github.com/JakeWharton/hugo", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 26, 22602, "timber", "timber", "JakeWharton", "github", "https://github.com/JakeWharton/timber", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(2, 26, 12600, "", "", "", "github", "", 0, 0));

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    指示器
    public void addIndicatorData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=2 smid=27 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(2, 27, 22700, "MagicIndicator", "MagicIndicator", "hackware1993", "github", "https://github.com/hackware1993/MagicIndicator", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 27, 22701, "FlycoTabLayout", "FlycoTabLayout", "H07000223", "github", "https://github.com/H07000223/FlycoTabLayout", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(2, 27, 12700, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            照片选择器
    public void addPhotoSelectData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=2 smid=28 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(2, 28, 22800, "PhotoPicker", "PhotoPicker", "donglua", "github", "https://github.com/donglua/PhotoPicker", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 28, 22801, "MultiImageSelector", "MultiImageSelector", "lovetuzitong", "github", "https://github.com/lovetuzitong/MultiImageSelector", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 28, 22802, "GalleryFinal", "GalleryFinal", "pengjianbo", "github", "https://github.com/pengjianbo/GalleryFinal", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(2, 28, 12800, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    照片阅览器
    public void addPhotoReadData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=2 smid=29 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(2, 29, 22900, "PhotoView", "PhotoView", "chrisbanes", "github", "https://github.com/chrisbanes/PhotoView", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 29, 22901, "PhotoView", "PhotoView", "bm-x", "github", "https://github.com/bm-x/PhotoView", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 29, 22902, "PhotoDraweeView", "PhotoDraweeView", "ongakuer", "github", "https://github.com/ongakuer/PhotoDraweeView", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 29, 22903, "DragPhotoView", "DragPhotoView", "githubwing", "github", "https://github.com/githubwing/DragPhotoView", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 29, 22904, "PhotoDraweeView", "PhotoDraweeView", "ongakuer", "github", "https://github.com/ongakuer/PhotoDraweeView", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(2, 29, 12900, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            BAT公司各种高仿效果
    public void addBATData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=2 smid=30 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(2, 30, 23000, "ZoomHeader", "神交互。模仿饿了么详情页可以跟随手指移动 viewpager变详情页", "githubwing", "github", "https://github.com/githubwing/ZoomHeader", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 30, 23001, "【Java编码规范】《阿里巴巴Java开发手册（正式版）》更新（v1.1.1版）", "【Java编码规范】《阿里巴巴Java开发手册（正式版）》更新（v1.1.1版）", "云木西 ", "云栖社区", "https://yq.aliyun.com/articles/69327?utm_campaign=javac&utm_medium=images&utm_source=renyimen&utm_content=m_10068", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 30, 23002, "QQ", "一款高仿腾讯QQ的IM软件，主UI框架采用侧滑菜单+底部导航的方式，核心聊天功能基于bmob SDK，已经实现文本，表情，图片，位置，语音等信息的发送。", "HuTianQi", "github", "https://github.com/HuTianQi/QQ", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 30, 23003, "SwipeDelMenuLayout", "SwipeDelMenuLayout", "mcxtzhang", "github", "https://github.com/mcxtzhang/SwipeDelMenuLayout", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 30, 23004, "remusic", "remusic", "aa112901", "github", "https://github.com/aa112901/remusic", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 30, 23005, "Google 推出的 31 套在线课程", "Google 推出的 31 套在线课程", "Guokai Han", "chinagdg", "http://chinagdg.org/2015/12/google-%E6%8E%A8%E5%87%BA%E7%9A%84-31-%E5%A5%97%E5%9C%A8%E7%BA%BF%E8%AF%BE%E7%A8%8B/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 30, 23006, "DragPhotoView", "高仿微信可拖拽返回PhotoView", "githubwing", "github", "https://github.com/githubwing/DragPhotoView", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 30, 23007, "GSYVideoPlayer", "视频播放器（IJKplayer），HTTPS支持，支持弹幕，支持基本的拖动，声音、亮度调节，支持边播边缓存，支持视频本身自带rotation的旋转（90,270之类），重力旋转与手动旋转的同步支持，支持列表播放 ，直接添加控件为封面，列表全屏动画，视频加载速度，列表小窗口支持拖动，5.0的过场效果，调整比例，多分辨率切换，支持切换播放器，进度条小窗口预览，其他一些小动画效果。", "CarGuo", "github", "https://github.com/CarGuo/GSYVideoPlayer", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 30, 23008, "Bilibili_Wuxianda", "高仿Bilibili客户端 - ( ゜- ゜)つロ 乾杯~", "MichaelHuyp", "github", "https://github.com/MichaelHuyp/Bilibili_Wuxianda", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 30, 23009, "SimplifyReader", "一款基于Google Material Design设计开发的Android客户端，包括新闻简读，图片浏览，视频爽看 ，音乐轻听以及二维码扫描五个子模块。", "chentao0707", "github", "https://github.com/chentao0707/SimplifyReader", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 30, 23010, "Douya", "开源的 Material Design 豆瓣客户端（A Material Design app for douban.com）", "DreaminginCodeZH", "github", "https://github.com/DreaminginCodeZH/Douya", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 30, 23011, "sealtalk-android", "基于融云开发的 Android 版即时通讯（IM）应用程序 - 嗨豹。", "sealtalk", "github", "https://github.com/sealtalk/sealtalk-android", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 30, 23012, "LKShop", "开源商城", "Shuyun123", "github", "https://github.com/Shuyun123/LKShop", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(2, 30, 23013, "OSChina Android", "开源中国官方App客户端开源代码。", "巴拉迪维", "oschina", "http://git.oschina.net/oschina/android-app", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(2, 30, 23000, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }


    //    测试
    public void addCeshiData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=31 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 31, 33100, "Android测试之旅（一）", "Android测试之旅（一）", "sir", "kikujiang.com", "http://kikujiang.com/2016/05/07/android-test-junit-160507/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 31, 33101, "Android测试之旅（二）", "Android测试之旅（二）", "sir", "kikujiang.com", "http://kikujiang.com/2016/05/07/android-test-junit-160508/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 31, 33102, "在Android Studio中进行单元测试和UI测试", "在Android Studio中进行单元测试和UI测试", "TestDevTalk", "简书", "http://www.jianshu.com/p/03118c11c199", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 31, 33103, "解放双手——Android自动化测试", "解放双手——Android自动化测试", "eclipse_xu", "csdn", "http://blog.csdn.net/eclipsexys/article/details/45622813", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 31, 33100, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            混淆
    public void addMixData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=32 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 32, 33200, "ProGuard代码混淆技术详解", "ProGuard代码混淆技术详解", "特立独行Allen", "cnblogs", "http://www.cnblogs.com/cr330326/p/5534915.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 32, 33201, " Android ProGuard 混淆 详解", " Android ProGuard 混淆 详解", "crianzy", "csdn", "http://blog.csdn.net/chen930724/article/details/49687067", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 32, 33202, "5分钟搞定android混淆", "5分钟搞定android混淆", "wolearn", "简书", "http://www.jianshu.com/p/f3455ecaa56e", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 32, 33200, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    反编译
    public void addDecompilersData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=33 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 33, 33300, "Android安全攻防战，反编译与混淆技术完全解析（上）", "Android安全攻防战，反编译与混淆技术完全解析（上）", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/49738023", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 33, 33301, "Android安全攻防战，反编译与混淆技术完全解析（下）", "Android安全攻防战，反编译与混淆技术完全解析（下）", "guolin", "csdn", "http://blog.csdn.net/guolin_blog/article/details/50451259", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 33, 33302, "Android反编译和二次打包实战", "Android反编译和二次打包实战", "viclee108", "csdn", "http://blog.csdn.net/goodlixueyong/article/details/51126874", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 33, 33300, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            studio插件
    public void addStudioData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=34 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 34, 33400, "GsonFormat", "快速将json字符串转换成一个Java Bean，免去我们根据json字符串手写对应Java Bean的过程.", "plugin", "plugins.jetbrains.com", "https://plugins.jetbrains.com/plugin/7654-gsonformat", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 34, 33401, "Android ButterKnife Zelezny", "配合ButterKnife实现注解，从此不用写findViewById，想着就爽啊。在Activity，Fragment，Adapter中选中布局xml的资源id自动生成butterknife注解。", "plugin", "plugins.jetbrains.com", "https://plugins.jetbrains.com/plugin/7369-android-butterknife-zelezny", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 34, 33402, "Android Parcelable code generator", "JavaBean序列化，快速实现Parcelable接口。", "plugin", "plugins.jetbrains.com", "https://plugins.jetbrains.com/plugin/7332-android-parcelable-code-generator", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 34, 33403, "findBugs-IDEA", "查找bug的插件，Android Studio也提供了代码审查的功能（Analyze-Inspect Code...）", "plugin", "plugins.jetbrains.com", "https://plugins.jetbrains.com/plugin/3847-findbugs-idea", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 34, 33404, "leakcanary", "帮助你在开发阶段方便的检测出内存泄露的问题，使用起来更简单方便。", "square", "github", "https://github.com/square/leakcanary", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 34, 33405, "BorePlugin", "Android Studio 自动生成布局代码插件", "boredream", "github", "https://github.com/boredream/BorePlugin", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 34, 33400, "", "", "plugin", "plugins.jetbrains.com", "", 0, 0));

        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    项目架构
    public void addProjectFrameworkData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=35 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 35, 33500, "选择恐惧症的福音！教你认清MVC，MVP和MVVM", "选择恐惧症的福音！教你认清MVC，MVP和MVVM", "zjutkz", "zjutkz's blog", "http://zjutkz.net/2016/04/13/%E9%80%89%E6%8B%A9%E6%81%90%E6%83%A7%E7%97%87%E7%9A%84%E7%A6%8F%E9%9F%B3%EF%BC%81%E6%95%99%E4%BD%A0%E8%AE%A4%E6%B8%85MVC%EF%BC%8CMVP%E5%92%8CMVVM/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 35, 33501, "浅谈 MVP in Android", "浅谈 MVP in Android", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/46596109", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 35, 33502, "Android应用中MVP最佳实践", "Android应用中MVP最佳实践", "Jude95", "简书", "http://www.jianshu.com/p/ed2aa9546c2c", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 35, 33503, "Android官方MVP架构项目解析", "Android官方MVP架构项目解析", "spiritTalk", "简书", "http://www.jianshu.com/p/389c9ae1a82c", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 35, 33504, "Android APP架构心得", "Android APP架构心得", "RayeWang", "weixin", "http://mp.weixin.qq.com/s?__biz=MzIyNjE4NjI2Nw==&mid=2652556529&idx=1&sn=c072fe44c9f4b7f383eeb70fe4dab84b&scene=22&srcid=0612skC0mqKdNabYf2q1Bdyr#rd", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 35, 33500, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            响应式编程
    public void addReactiveProgrammingData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=36 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 36, 33600, "给 Android 开发者的 RxJava 详解", "给 Android 开发者的 RxJava 详解", "扔物线", "gank.io", "http://gank.io/post/560e15be2dca930e00da1083", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 36, 33601, "要做一个有冒险精神的人！开启漫漫的agera之旅", "要做一个有冒险精神的人！开启漫漫的agera之旅", "zjutkz", "zjutkz.net", "http://zjutkz.net/2016/04/23/%E8%A6%81%E5%81%9A%E4%B8%80%E4%B8%AA%E6%9C%89%E5%86%92%E9%99%A9%E7%B2%BE%E7%A5%9E%E7%9A%84%E4%BA%BA%EF%BC%81%E5%BC%80%E5%90%AF%E6%BC%AB%E6%BC%AB%E7%9A%84agera%E4%B9%8B%E6%97%85/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 36, 33602, "AndroidAgeraTutorial", "Agera Wiki 中文版", "captain-miao", "github", "https://github.com/captain-miao/AndroidAgeraTutorial/wiki", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 36, 33603, "RxJava简洁封装之道", "RxJava简洁封装之道", "YoKey", "简书", "http://www.jianshu.com/p/f3f0eccbcd6f", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 36, 33604, "RxJava", "RxJava", "ReactiveX", "github", "https://github.com/ReactiveX/RxJava", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 36, 33605, "RxAndroid", "RxAndroid", "ReactiveX", "github", "https://github.com/ReactiveX/RxAndroid", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 36, 33600, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    github
    public void addGithubData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=37 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 37, 33700, "从0开始学习 GitHub 系列之「初识 GitHub」", "从0开始学习 GitHub 系列之「初识 GitHub」", "stormzhang", "stormzhang.com", "http://stormzhang.com/github/2016/05/25/learn-github-from-zero1/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 37, 33701, "从0开始学习 GitHub 系列之「加入 GitHub」", "从0开始学习 GitHub 系列之「加入 GitHub」", "stormzhang", "stormzhang.com", "http://stormzhang.com/github/2016/05/26/learn-github-from-zero2/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 37, 33702, "从0开始学习 GitHub 系列之「Git 速成」", "从0开始学习 GitHub 系列之「Git 速成」", "stormzhang", "stormzhang.com", "http://stormzhang.com/github/2016/05/25/learn-github-from-zero3/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 37, 33703, "从0开始学习 GitHub 系列之「向GitHub 提交代码」", "从0开始学习 GitHub 系列之「向GitHub 提交代码」", "stormzhang", "stormzhang.com", "http://stormzhang.com/github/2016/05/25/learn-github-from-zero4/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 37, 33704, "Github征途", "Github征途", "lavor", "简书", "http://www.jianshu.com/p/a20cda198677", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 37, 33705, "推荐！手把手教你使用Git", "推荐！手把手教你使用Git", "涂根华", "博客园", "http://www.cnblogs.com/tugenhua0707/p/4050072.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 37, 33706, "搭建git服务器", "关于git本地服务器的简单搭建", "廖雪峰", "http://www.liaoxuefeng.com/", "http://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000/00137583770360579bc4b458f044ce7afed3df579123eca000", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 37, 33700, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    webview/webp
    public void addWebviewData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=38 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 38, 33800, "好好和h5沟通！几种常见的hybrid通信方式", "好好和h5沟通！几种常见的hybrid通信方式", "zjutkz", "zjutkz.net", "http://zjutkz.net/2016/04/17/%E5%A5%BD%E5%A5%BD%E5%92%8Ch5%E6%B2%9F%E9%80%9A%EF%BC%81%E5%87%A0%E7%A7%8D%E5%B8%B8%E8%A7%81%E7%9A%84hybrid%E9%80%9A%E4%BF%A1%E6%96%B9%E5%BC%8F/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 38, 33801, "Android WebView 开发详解(一)", "Android WebView 开发详解(一)", "typename", "csdn", "http://blog.csdn.net/typename/article/details/39030091", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 38, 33802, "Android WebView 开发详解(二)", "Android WebView 开发详解(二)", "typename", "csdn", "http://blog.csdn.net/typename/article/details/39495409", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 38, 33803, "Android WebView 开发详解(三)", "Android WebView 开发详解(三)", "typename", "csdn", "http://blog.csdn.net/typename/article/details/40302351", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 38, 33804, "Android 各个版本WebView", "Android 各个版本WebView", "typename", "csdn", "http://blog.csdn.net/typename/article/details/40425275", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 38, 33805, "深入讲解WebView——上", "深入讲解WebView——上", "张涛", "张涛-开源实验室", "https://www.kymjs.com/code/2015/05/03/01/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 38, 33806, "深入讲解WebView——下", "深入讲解WebView——下", "张涛", "张涛-开源实验室", "https://kymjs.com/code/2015/05/04/01/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 38, 33807, "JS与WebView交互存在的一些问题", "JS与WebView交互存在的一些问题", "隔壁的李小宝", "简书", "http://www.jianshu.com/p/93cea79a2443", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 38, 33808, "[置顶] Android WebView的Js对象注入漏洞解决方案", "[置顶] Android WebView的Js对象注入漏洞解决方案", "leehong2005", "csdn", "http://blog.csdn.net/leehong2005/article/details/11808557", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 38, 33809, "Android之WebView快速上手", "Android之WebView快速上手", "absfree", "简书", "http://www.jianshu.com/p/ac6d69ec2260", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 38, 33810, "谈谈WebView的使用", "谈谈WebView的使用", "CameloeAnthony", "简书", "http://www.jianshu.com/p/e3965d3636e7", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 38, 33811, "灵活高效的在 Android Native App 开发中显示 HTML 内容", "灵活高效的在 Android Native App 开发中显示 HTML 内容", "张谦", "IBM development文档库", "https://www.ibm.com/developerworks/cn/web/1407_zhangqian_androidhtml/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 38, 33812, "Android自定义解析Html的TextView-HtmlView", "Android自定义解析Html的TextView-HtmlView", "super_spy", "csdn", "http://blog.csdn.net/super_spy/article/details/48803215", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 38, 33813, "blog", "Android 4.4 中 WebView 使用注意事项", "cundong", "github", "https://github.com/cundong/blog/blob/master/Android%204.4%20%E4%B8%AD%20WebView%20%E4%BD%BF%E7%94%A8%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9.md", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 38, 33814, "WebView 远程代码执行漏洞浅析", "WebView 远程代码执行漏洞浅析", "阿里无线安全团队", "http://jaq.alibaba.com/", "http://jaq.alibaba.com/blog.htm?id=48", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 38, 33800, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            RecyclerView
    public void addRecyclerViewData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=28 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 39, 33900, "Android RecyclerView 使用完全解析 体验艺术般的控件", "Android RecyclerView 使用完全解析 体验艺术般的控件", "鸿洋_", "csdn", "http://blog.csdn.net/lmj623565791/article/details/45059587", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 39, 33901, "RecyclerView自定义LayoutManager,打造不规则布局", "RecyclerView自定义LayoutManager,打造不规则布局", "亓斌", "csdn", "http://blog.csdn.net/qibin0506/article/details/52676670", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 39, 33900, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    React Native
    public void addReactNativeData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=28 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 40, 34000, "React Native植入原生Android应用的流程解析", "React Native植入原生Android应用的流程解析", "ACGTOFE", "http://acgtofe.com/posts/2016/06/react-native-embedding-android", "http://acgtofe.com/posts/2016/06/react-native-embedding-android", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 40, 34001, "React Native专题", "React Native专题", "【江清清的技术专栏】", "lcode.org", "http://www.lcode.org/react-native/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 40, 34002, "react-native", "react-native", "facebook", "github", "https://github.com/facebook/react-native", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 40, 34003, "react-native-lesson", "react-native-lesson", "vczero", "github", "https://github.com/vczero/react-native-lesson", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 40, 34004, "React Native中文网", "React Native中文网", "reactnative", "http://reactnative.cn/", "http://reactnative.cn/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 40, 34005, "React-Native移植-Android", "React-Native移植-Android", "hanhailong726188", "csdn", "http://blog.csdn.net/hanhailong726188/article/details/51236285", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 40, 34000, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    OpenGL ES
    public void addOpenGLESData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=41 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 41, 34100, "《OpenGL ES 应用开发实践指南》读书笔记 No.1", "《OpenGL ES 应用开发实践指南》读书笔记 No.1", "rogerblog", "rogerblog.cn", "https://www.rogerblog.cn/2016/07/18/OpenGL-serise-No1/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 41, 34101, "OpenGL ES的开发", "OpenGL ES的开发", "51cto", "51cto", "http://mobile.51cto.com/aengine-437184.htm", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 41, 34100, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    热修复
    public void addHotfixData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=42 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 42, 34200, "Android热修复技术选型——三大流派解析", "Android热修复技术选型——三大流派解析", "阿里百川", "cnblogs", "http://www.cnblogs.com/alibaichuan/p/5863616.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 42, 34201, "AndFix使用说明", "AndFix使用说明", "seewhy", "简书", "http://www.jianshu.com/p/479b8c7ec3e3", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 42, 34202, "Android Hotfix 新方案——Amigo 源码解读", "Android Hotfix 新方案——Amigo 源码解读", "canaan", "diycode.cc", "https://www.diycode.cc/topics/280", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 42, 34200, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            推送
    public void addPushData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=43 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 43, 34300, "Android APP必备高级功能，消息推送之MQTT", "Android APP必备高级功能，消息推送之MQTT", "一口仨馍", "csdn", "http://blog.csdn.net/qq_17250009/article/details/52774472", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 43, 34300, " ", " ", "", "csdn", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    IM
    public void addIMData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=44 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 44, 34400, "Android IM即时通信开发总结及代码", "Android IM即时通信开发总结及代码", "普洛提亚", "博客园", "http://www.cnblogs.com/puluotiya/p/5697708.html", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 44, 34400, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            组件化
    public void addModularizationData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=28 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 45, 34500, "Android业务组件化之现状分析与探讨", "Android业务组件化之现状分析与探讨", "总李写代码", "cnblogs", "http://www.cnblogs.com/whoislcj/p/5853393.html", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 45, 34501, "Android 业务组件化开发实践", "Android 业务组件化开发实践", "kymjs", "diycode.cc", "https://www.diycode.cc/topics/362", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 45, 34502, "从零开始的Android新项目11 - 组件化实践（1）", "从零开始的Android新项目11 - 组件化实践（1）", "zhaiyifan", "http://blog.zhaiyifan.cn", "http://blog.zhaiyifan.cn/2016/10/20/android-new-project-from-0-p11/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 45, 34503, "项目组件化之遇到的坑", "项目组件化之遇到的坑", "Abner_泥阿布", "abner-nimengbo.cn", "http://abner-nimengbo.cn/2016/10/10/componetization/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 45, 34504, "ComponentizationApp", "组件化的示例之一", "liangzhitao", "github", "https://github.com/liangzhitao/ComponentizationApp", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 45, 34505, "Modularity", "组件化的示例之一", "kymjs", "github", "https://github.com/kymjs/Modularity", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 45, 34500, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    沉浸式
    public void addImmersiveData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=28 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 46, 34600, "Activity 全屏，沉浸式模式这一篇就够了", "Activity 全屏，沉浸式模式这一篇就够了", "小钙", "csdn", "http://blog.csdn.net/zhangqinghuazhangzhe/article/details/52935290", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 46, 34600, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            AccessibilityService
    public void addAccessibilityServiceData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=47 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 47, 34700, "使用辅助服务打造自己的智能视频监控系统", "使用辅助服务打造自己的智能视频监控系统", "HelloCsl", "简书", "http://www.jianshu.com/p/d91e2e015718", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 47, 34701, "你真的理解AccessibilityService吗", "你真的理解AccessibilityService吗", "涅槃1992", "简书", "http://www.jianshu.com/p/4cd8c109cdfb#", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 47, 34700, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    数据打点
    public void addDatManagementData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=48 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 48, 34800, "数据采集与埋点", "数据采集与埋点", "sensorsdata", "https://www.sensorsdata.cn", "https://www.sensorsdata.cn/blog/shu-ju-jie-ru-yu-mai-dian/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 48, 34801, "mixpanel的埋点统计库", "mixpanel的埋点统计库", "mixpanel", "github", "https://github.com/mixpanel/mixpanel-android", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 48, 34800, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //            异常
    public void addExceptionData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=49 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 49, 34900, "Android常见的几种RuntimeException", "Android常见的几种RuntimeException", "jaycee110905", "csdn", "http://blog.csdn.net/jaycee110905/article/details/45154039", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 49, 34900, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    Crash处理
    public void addCrashData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=28 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 50, 35000, "理解 Android Crash 处理流程", "理解 Android Crash 处理流程", "gityuan", "https://gold.xitu.io", "http://gityuan.com/2016/06/24/app-crash/", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 50, 35000, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    6.0权限
    public void add6Data() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=28 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 51, 35100, "android 6.0 权限管理的学习资料和使用例子", "android 6.0 权限管理的学习资料和使用例子", "ExceptionofMine", "csdn", "http://blog.csdn.net/yangqingqo/article/details/48371123", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 51, 35101, "安卓6.0新特性在Fragment申请运行时权限", "安卓6.0新特性在Fragment申请运行时权限", "安儿IT", "csdn", "http://blog.csdn.net/qfanmingyiq/article/details/52561658", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 51, 35102, "PermissionsDispatcher", "PermissionsDispatcher", "hotchemi", "github", "https://github.com/hotchemi/PermissionsDispatcher", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 51, 35100, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //    Android N 分屏
    public void addNData() {
        List<MTKnowledge> mtKnowledgeList = new ArrayList<>();
        //        mid=3 smid=28 kid=0以1(mid)3(smid)000开头
        mtKnowledgeList.add(new MTKnowledge(3, 52, 35200, "Android N App分屏模式完全解析（上）", "Android N App分屏模式完全解析（上）", "Uncle Chen", "http://unclechen.github.io/", "http://unclechen.github.io/2016/03/12/Android-N-App%E5%88%86%E5%B1%8F%E6%A8%A1%E5%BC%8F%E5%AE%8C%E5%85%A8%E8%A7%A3%E6%9E%90-%E4%B8%8A%E7%AF%87/", 0, 0));
        mtKnowledgeList.add(new MTKnowledge(3, 52, 35201, "Android N App分屏模式完全解析（下）", "Android N App分屏模式完全解析（下）", "Uncle Chen", "http://unclechen.github.io/", "http://unclechen.github.io/2016/03/12/Android-N-App%E5%88%86%E5%B1%8F%E6%A8%A1%E5%BC%8F%E5%AE%8C%E5%85%A8%E8%A7%A3%E6%9E%90-%E4%B8%8B%E7%AF%87/", 0, 0));
//        mtKnowledgeList.add(new MTKnowledge(3, 52, 35200, "", "", "", "github", "", 0, 0));
        addKnowledge(mtKnowledgeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("KnowledgeManager", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("KnowledgeManager", "失败");
            }

            @Override
            public void onFinished() {

            }
        });
    }


}
