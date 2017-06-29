package com.madreain.androiddream.core.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * @author madreain
 * @desc
 * @time 2017/4/25
 */

@DatabaseTable(tableName = "tb_mselfstudy")
public class SelfStudyModel extends BmobObject implements Serializable{
    @DatabaseField
    String title;//自学视频的课程名
    @DatabaseField
    String desc;
    @DatabaseField
    int selfid;//视频集合下的id
    @DatabaseField
    String pic;//照片

    public SelfStudyModel() {
    }

    public SelfStudyModel(String title, String desc, int selfid, String pic) {
        this.title = title;
        this.desc = desc;
        this.selfid = selfid;
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSelfid() {
        return selfid;
    }

    public void setSelfid(int selfid) {
        this.selfid = selfid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
