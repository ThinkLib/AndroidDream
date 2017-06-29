package com.madreain.androiddream.core.Manager;

import android.util.Log;

import com.madreain.androiddream.BuildConfig;
import com.madreain.androiddream.core.Dao.MSecondTypeDao;
import com.madreain.androiddream.core.Dao.MTypeDao;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.MSecondType;
import com.madreain.androiddream.core.Model.MTKnowledge;
import com.madreain.androiddream.core.Constants;
import com.madreain.androiddream.core.Model.MType;
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
 * 二级小分类
 */

public class SecondTypeManager {
    private static SecondTypeManager instance;

    private SecondTypeManager() {
    }

    public static SecondTypeManager getInstance() {
        if (instance == null) {
            synchronized (SecondTypeManager.class) {
                if (instance == null) {
                    instance = new SecondTypeManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加多条数据
     * 添加大分类下的小分类
     * 添加多条数据一次不能超过五十个
     *
     * @param mSecondTypeList
     * @param mbValueCallBack
     */
    public void addSecondType(List<MSecondType> mSecondTypeList, final MBCallback.MBDataCallback mbValueCallBack) {
        //需要用bomb的对象进行存储
        List<BmobObject> bmobObjectList = new ArrayList<BmobObject>();
        //循环插入
        for (MSecondType mSecondType : mSecondTypeList) {
            bmobObjectList.add(mSecondType);
        }
        new BmobBatch().insertBatch(bmobObjectList).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if (e == null) {
                    mbValueCallBack.onSuccess();
                } else {
                    if (BuildConfig.DEBUG) Log.d("TypeManager", "e:" + e);
                    mbValueCallBack.onError(Constants.Error);
                }
            }
        });

    }

    /***
     * 根据大分类的mid获取小分类的列表
     * 刷新
     *
     * @param mid
     * @param mbValueCallBack
     */
    public void refreshSecondTypeByMid(int mid, final MBCallback.MBValueCallBack<List<MSecondType>> mbValueCallBack) {
        BmobQuery<MSecondType> bmobQuery = new BmobQuery<>();
        // 根据smid字段升序显示数据
        bmobQuery.order("smid");
        //查询playerName叫“比目”的数据
        bmobQuery.addWhereEqualTo("mid", mid);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        bmobQuery.setLimit(10);
        bmobQuery.findObjects(new FindListener<MSecondType>() {
            @Override
            public void done(List<MSecondType> list, BmobException e) {
                if (e == null) {
                    mbValueCallBack.onSuccess(list);
                    //获取的时候还存储到本地
                    new MSecondTypeDao(MUtil.getContext()).addMSecondType(list);
                } else {
                    if (BuildConfig.DEBUG) Log.d("TypeManager", "e:" + e);
                    mbValueCallBack.onError(Constants.Error);
                }
            }
        });
    }


    /***
     * 根据大分类的mid获取小分类的列表
     * 获取更多
     * @param size
     * @param mid
     * @param mbValueCallBack
     */
    public void getMoreSecondTypeByMid(int size,int mid, final MBCallback.MBValueCallBack<List<MSecondType>> mbValueCallBack) {
        BmobQuery<MSecondType> bmobQuery = new BmobQuery<>();
        // 忽略前10条数据（即第一页数据结果）
        bmobQuery.setSkip(size);
        // 根据smid字段升序显示数据
        bmobQuery.order("smid");
        //查询playerName叫“比目”的数据
        bmobQuery.addWhereEqualTo("mid", mid);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        bmobQuery.setLimit(10);
        bmobQuery.findObjects(new FindListener<MSecondType>() {
            @Override
            public void done(List<MSecondType> list, BmobException e) {
                if (e == null) {
                    mbValueCallBack.onSuccess(list);
                    //获取的时候还存储到本地
                    new MSecondTypeDao(MUtil.getContext()).addMSecondType(list);
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
    public List<MSecondType> getMSecondType() {
        return new MSecondTypeDao(MUtil.getContext()).getSecondTypeList();
    }

}
