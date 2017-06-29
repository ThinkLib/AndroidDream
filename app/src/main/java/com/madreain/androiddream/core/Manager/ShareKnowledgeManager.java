package com.madreain.androiddream.core.Manager;

import android.util.Log;

import com.madreain.androiddream.BuildConfig;
import com.madreain.androiddream.core.Constants;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
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
 * @time 2017/4/20
 */

public class ShareKnowledgeManager {
    private static ShareKnowledgeManager instance;

    private ShareKnowledgeManager() {
    }

    public static ShareKnowledgeManager getInstance() {
        if (instance == null) {
            synchronized (ShareKnowledgeManager.class) {
                if (instance == null) {
                    instance = new ShareKnowledgeManager();
                }
            }
        }
        return instance;
    }

    public void addShareKnowledge(ShareKnowledge shareKnowledge, final MBCallback.MBDataCallback mbValueCallBack) {
        //需要用bomb的对象进行存储
        List<BmobObject> bmobObjectList = new ArrayList<BmobObject>();
        bmobObjectList.add(shareKnowledge);
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
