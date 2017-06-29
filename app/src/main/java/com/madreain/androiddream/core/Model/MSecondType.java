package com.madreain.androiddream.core.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import cn.bmob.v3.BmobObject;

/**
 * Created by madreain on 2017/4/1.
 * 二级分类
 */
@DatabaseTable(tableName = "msecondtype")
public class MSecondType extends BmobObject {
    @DatabaseField
    int mid;//某个一级分类下
    @DatabaseField
    int smid;//大分类下的小分类
    @DatabaseField
    String title;//对应的小分类的标题

    public MSecondType(int mid, int smid, String title) {
        this.mid = mid;
        this.smid = smid;
        this.title = title;
    }

    public MSecondType() {
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getSmid() {
        return smid;
    }

    public void setSmid(int smid) {
        this.smid = smid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
