package com.madreain.androiddream.core.Manager;

import android.util.Log;

import com.madreain.androiddream.BuildConfig;
import com.madreain.androiddream.core.Constants;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.FeedBackModel;
import com.madreain.androiddream.core.Model.ShareKnowledge;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;

/**
 * @author madreain
 * @desc
 * @time 2017/4/21
 */

public class FeedBackManager {
    private static FeedBackManager instance;

    private FeedBackManager() {
    }

    public static FeedBackManager getInstance() {
        if (instance == null) {
            synchronized (FeedBackManager.class) {
                if (instance == null) {
                    instance = new FeedBackManager();
                }
            }
        }
        return instance;
    }

    /**
     * 意见反馈
     * @param feedBackModel
     * @param mbValueCallBack
     */
    public void addFeedBack(FeedBackModel feedBackModel, final MBCallback.MBDataCallback mbValueCallBack) {
        //需要用bomb的对象进行存储
        List<BmobObject> bmobObjectList = new ArrayList<BmobObject>();
        bmobObjectList.add(feedBackModel);
        new BmobBatch().insertBatch(bmobObjectList).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if (e == null) {
                    mbValueCallBack.onSuccess();
                } else {
                    if (BuildConfig.DEBUG) Log.d("ShareKnowledgeManager", "e:" + e);
                    mbValueCallBack.onError(Constants.Error);
                }
            }
        });

    }
}
