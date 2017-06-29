package com.madreain.androiddream.core.Dao;


import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.madreain.androiddream.core.DB.MHelper;
import com.madreain.androiddream.core.Model.MSecondType;
import com.madreain.androiddream.core.Model.MType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 二级分类MType的数据库操作
 *
 * @author madreain
 * @desc
 * @time 2017/4/5
 */

public class MSecondTypeDao {

    private Dao<MSecondType, Integer> mSecondTypeDao;
    private MHelper helper;

    public MSecondTypeDao(Context context) {
        try {
            helper = MHelper.getHelper(context);
            mSecondTypeDao = helper.getDao(MSecondType.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加二级分类
     * add 没有的话直接添加 有的话执行更新方法
     *
     * @param mSecondTypeList
     */
    public void addMSecondType(List<MSecondType> mSecondTypeList) {
        try {
            if(isaddMSecondType()){
                for(MSecondType mType:mSecondTypeList){
                    mSecondTypeDao.update(mType);
                }
            }else
                mSecondTypeDao.create(mSecondTypeList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isaddMSecondType(){
        List<MSecondType> mSecondtypeList=getSecondTypeList();
        if(mSecondtypeList==null||mSecondtypeList.size()==0){
            return false;
        }else
            return true;
    }

    /***
     * 获取所有的
     *
     * @return
     */
    public List<MSecondType> getSecondTypeList() {
        List<MSecondType> themes = new ArrayList<MSecondType>();
        try {
            themes = mSecondTypeDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return themes;
    }


}
