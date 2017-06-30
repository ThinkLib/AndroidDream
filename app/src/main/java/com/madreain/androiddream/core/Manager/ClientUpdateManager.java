package com.madreain.androiddream.core.Manager;

import android.util.Log;
import android.widget.Toast;

import com.madreain.androiddream.BuildConfig;
import com.madreain.androiddream.core.Constants;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.MType;
import com.madreain.androiddream.core.Model.UpdateVersionModel;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 版本更新检查
 *
 * @author madreain
 * @desc
 * @time 2017/4/27
 */

public class ClientUpdateManager {
    private static ClientUpdateManager instance;

    private ClientUpdateManager() {
    }

    public static ClientUpdateManager getInstance() {
        if (instance == null) {
            synchronized (ClientUpdateManager.class) {
                if (instance == null) {
                    instance = new ClientUpdateManager();
                }
            }
        }
        return instance;
    }


    /**
     * 版本更新
     * @param mbValueCallBack
     */
    public void getClientUpdateConfig(final MBCallback.MBValueCallBack<UpdateVersionModel> mbValueCallBack) {
        BmobQuery<UpdateVersionModel> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<UpdateVersionModel>() {
            @Override
            public void done(List<UpdateVersionModel> list, BmobException e) {
                if (e == null) {
                    UpdateVersionModel updateVersionModel = list.get(0);
                    if (updateVersionModel != null) {
                        mbValueCallBack.onSuccess(updateVersionModel);
                    }
                } else if (e.getErrorCode() == 9015) {
                    if (BuildConfig.DEBUG) Log.d("TypeManager", "e:" + e);
                } else {
                    if (BuildConfig.DEBUG) Log.d("TypeManager", "e:" + e);
                    mbValueCallBack.onError(Constants.Error);
                }
            }
        });
    }

}
