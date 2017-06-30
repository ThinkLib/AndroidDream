package com.madreain.androiddream.core.Manager;

import android.util.Log;

import com.madreain.androiddream.BuildConfig;
import com.madreain.androiddream.core.Constants;
import com.madreain.androiddream.core.Dao.MTypeDao;
import com.madreain.androiddream.core.Dao.SelfStudyModelDao;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.MType;
import com.madreain.androiddream.core.Model.SelfStudyModel;
import com.madreain.androiddream.core.Model.VideoKnowledge;
import com.madreain.androiddream.utils.MUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 自学栏目下的视频集   视频集—>video
 * @author madreain
 * @desc
 * @time 2017/4/25
 */

public class SelfStudyManager {
    private static SelfStudyManager instance;

    private SelfStudyManager() {
    }

    public static SelfStudyManager getInstance() {
        if (instance == null) {
            synchronized (SelfStudyManager.class) {
                if (instance == null) {
                    instance = new SelfStudyManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取到自学的所有视频集
     * @param mbValueCallBack
     */
    public void refreshSelfStudyModelList(final MBCallback.MBValueCallBack<List<SelfStudyModel>> mbValueCallBack){
        BmobQuery<SelfStudyModel> bmobQuery = new BmobQuery<>();
        // 根据smid字段升序显示数据
        bmobQuery.order("selfid");
        bmobQuery.findObjects(new FindListener<SelfStudyModel>() {
            @Override
            public void done(List<SelfStudyModel> list, BmobException e) {
                if (e == null) {
                    mbValueCallBack.onSuccess(list);
                    //获取的时候还存储到本地
                    new SelfStudyModelDao(MUtil.getContext()).addSelfStudyModel(list);
                    //获取的时候还存储到本地
                } else if(e.getErrorCode()==9015){
                    if (BuildConfig.DEBUG) Log.d("SelfStudyManager", "e:" + e);
                } else {
                    if (BuildConfig.DEBUG) Log.d("SelfStudyManager", "e:" + e);
                    mbValueCallBack.onError(Constants.Error);
                }
            }
        });
    }

    /***
     * 默认存本地的
     * @return
     */
    public List<SelfStudyModel> getDefaultSelfStudyModel() {
        List<SelfStudyModel> selfStudyModellist=new ArrayList<>();
        selfStudyModellist.add(new SelfStudyModel("Android攻城狮的第一门课（入门篇）","本课程涵盖全部Android应用开发的基础，根据技能点的作用分为5个篇章，包括环境篇、控件篇、布局篇、组件篇和通用篇，本课程的目标就是“看得懂、学得会、做得出”，为后续的学习打下夯实的基础。",0, "http://bmob-cdn-10899.b0.upaiyun.com/2017/04/27/f3fe70c7402514fd80aeacc10aa2d067.jpg"));
//        selfStudyModellist.add(new SelfStudyModel("Android攻城狮的第二门课","本课程涵盖全部Android应用开发的基础，根据技能点的作用分为5个篇章，包括环境篇、控件篇、布局篇、组件篇和通用篇，本课程的目标就是“看得懂、学得会、做得出”，为后续的学习打下夯实的基础。",1, "http://bmob-cdn-10899.b0.upaiyun.com/2017/04/27/cf385293408080c88094128512a8ba88.jpg"));

        return selfStudyModellist;
    }

    /***
     * 本地获取
     * @return
     */
    public List<SelfStudyModel> getSelfStudyModel() {
        return new SelfStudyModelDao(MUtil.getContext()).getSelfStudyModelList();
    }

}
