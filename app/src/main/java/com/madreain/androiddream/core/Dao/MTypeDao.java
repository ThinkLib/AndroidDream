package com.madreain.androiddream.core.Dao;


import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.madreain.androiddream.core.DB.MHelper;
import com.madreain.androiddream.core.Model.MType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 一级分类MType的数据库操作
 *
 * @author madreain
 * @desc
 * @time 2017/4/5
 */

public class MTypeDao {

    private Dao<MType, Integer> typeDao;
    private MHelper helper;

    public MTypeDao(Context context) {
        try {
            helper = MHelper.getHelper(context);
            typeDao = helper.getDao(MType.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一级分类
     * add 没有的话直接添加 有的话执行更新方法
     *
     * @param mTypeList
     */
    public void addMType(List<MType> mTypeList) {
        try {
            if(isaddMType()){
                for(MType mType:mTypeList){
                    typeDao.update(mType);
                }
            }else
            typeDao.create(mTypeList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isaddMType(){
        List<MType> typeList=getTypeList();
        if(typeList==null||typeList.size()==0){
            return false;
        }else
            return true;
    }

    /***
     * 获取所有的
     *
     * @return
     */
    public List<MType> getTypeList() {
        List<MType> themes = new ArrayList<MType>();
        try {
            themes = typeDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return themes;
    }


}
