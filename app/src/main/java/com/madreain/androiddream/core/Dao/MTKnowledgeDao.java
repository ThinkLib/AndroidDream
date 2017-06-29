package com.madreain.androiddream.core.Dao;


import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.madreain.androiddream.core.DB.MHelper;
import com.madreain.androiddream.core.Model.MTKnowledge;
import com.madreain.androiddream.core.Model.MType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 知识详情MTKnowledge的数据库操作
 *
 * @author madreain
 * @desc
 * @time 2017/4/5
 */

public class MTKnowledgeDao {

    private Dao<MTKnowledge, Integer> mtKnowledgeDao;
    private MHelper helper;

    public MTKnowledgeDao(Context context) {
        try {
            helper = MHelper.getHelper(context);
            mtKnowledgeDao = helper.getDao(MTKnowledge.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加知识详情MTKnowledge
     * add 没有的话直接添加 有的话执行更新方法
     *
     * @param mtKnowledgeList
     */
    public void addMTKnowledge(List<MTKnowledge> mtKnowledgeList) {
        try {
            if(isaddMTKnowledge()){
                for(MTKnowledge mtKnowledge:mtKnowledgeList){
                    mtKnowledgeDao.update(mtKnowledge);
                }
            }else
                mtKnowledgeDao.create(mtKnowledgeList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isaddMTKnowledge(){
        List<MTKnowledge> mtKnowledgeList=getMTKnowledgeList();
        if(mtKnowledgeList==null||mtKnowledgeList.size()==0){
            return false;
        }else
            return true;
    }

    /***
     * 获取所有的
     *
     * @return
     */
    public List<MTKnowledge> getMTKnowledgeList() {
        List<MTKnowledge> themes = new ArrayList<MTKnowledge>();
        try {
            themes = mtKnowledgeDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return themes;
    }


}
