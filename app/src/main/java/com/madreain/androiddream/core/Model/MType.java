package com.madreain.androiddream.core.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by madreain on 2017/4/1.
 * 第一级分类
 */
@DatabaseTable(tableName = "tb_mtype")
public class MType extends BmobObject implements Serializable{
    @DatabaseField(generatedId = true)
    int mid;//id
    @DatabaseField
    String title;//对应的某个分类的标题

    public MType(int mid, String title) {
        this.mid = mid;
        this.title = title;
    }

    public MType() {
    }

    public int getMid() {
        return mid;
    }


    public String getTitle() {
        return title;
    }

}
