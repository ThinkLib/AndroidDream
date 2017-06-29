package com.madreain.androiddream.core.Manager;

import android.util.Log;

import com.madreain.androiddream.BuildConfig;
import com.madreain.androiddream.core.Dao.MTypeDao;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.MType;
import com.madreain.androiddream.utils.MUtil;
import com.madreain.androiddream.core.Constants;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by madreain on 2017/4/1.
 * 大分类
 */

public class TypeManager {
    private static TypeManager instance;

    private TypeManager() {
    }

    public static TypeManager getInstance() {
        if (instance == null) {
            synchronized (TypeManager.class) {
                if (instance == null) {
                    instance = new TypeManager();
                }
            }
        }
        return instance;
    }


    /**
     * 获取分类
     *
     * @param mbValueCallBack
     */
    public void refreshMType(final MBCallback.MBValueCallBack<List<MType>> mbValueCallBack) {
        BmobQuery<MType> bmobQuery = new BmobQuery<>();
        // 根据mid字段升序显示数据
        bmobQuery.order("mid");
//        bmobQuery.addWhereLessThan("mid", 6);
        bmobQuery.findObjects(new FindListener<MType>() {
            @Override
            public void done(List<MType> list, BmobException e) {
                if (e == null) {
                    mbValueCallBack.onSuccess(list);
                    //获取的时候还存储到本地
                    new MTypeDao(MUtil.getContext()).addMType(list);
                } else if(e.getErrorCode()==9015){
                    if (BuildConfig.DEBUG) Log.d("TypeManager", "e:" + e);
                } else {
                    if (BuildConfig.DEBUG) Log.d("TypeManager", "e:" + e);
                    mbValueCallBack.onError(Constants.Error);
                }
            }
        });
    }

    /***
     * 本地获取
     * @return
     */
    public List<MType> getMType() {
        return new MTypeDao(MUtil.getContext()).getTypeList();
    }

    /***
     * 默认存本地的
     * @return
     */
    public List<MType> getDefaultMType() {
        List<MType> mTypeList=new ArrayList<>();
        mTypeList.add(new MType(0, "java"));
        mTypeList.add(new MType(1, "Android基础"));
        mTypeList.add(new MType(2, "项目常用框架"));
        mTypeList.add(new MType(3, "热门/新技术"));
        mTypeList.add(new MType(4, "开源项目"));
        mTypeList.add(new MType(5, "面试"));
//        mTypeList.add(new MType(6, ""));
        return mTypeList;
    }

}
