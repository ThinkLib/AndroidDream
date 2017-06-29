package com.madreain.androiddream.core.Dao;


import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.madreain.androiddream.core.DB.MHelper;
import com.madreain.androiddream.core.Model.MType;
import com.madreain.androiddream.core.Model.SelfStudyModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 自学视频内容数据库操作
 *
 * @author madreain
 * @desc
 * @time 2017/4/5
 */

public class SelfStudyModelDao {

    private Dao<SelfStudyModel, Integer> selfStudyModelDao;
    private MHelper helper;

    public SelfStudyModelDao(Context context) {
        try {
            helper = MHelper.getHelper(context);
            selfStudyModelDao = helper.getDao(SelfStudyModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加视频
     * add 没有的话直接添加 有的话执行更新方法
     *
     * @param selfStudyModelList
     */
    public void addSelfStudyModel(List<SelfStudyModel> selfStudyModelList) {
        try {
            if(isaddSelfStudyModel()){
                for(SelfStudyModel selfStudyModel:selfStudyModelList){
                    selfStudyModelDao.update(selfStudyModel);
                }
            }else
                selfStudyModelDao.create(selfStudyModelList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isaddSelfStudyModel(){
        List<SelfStudyModel> selfStudyModelList=getSelfStudyModelList();
        if(selfStudyModelList==null||selfStudyModelList.size()==0){
            return false;
        }else
            return true;
    }

    /***
     * 获取所有的
     *
     * @return
     */
    public List<SelfStudyModel> getSelfStudyModelList() {
        List<SelfStudyModel> themes = new ArrayList<SelfStudyModel>();
        try {
            themes = selfStudyModelDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return themes;
    }


}
