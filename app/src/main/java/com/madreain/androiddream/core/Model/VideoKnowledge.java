package com.madreain.androiddream.core.Model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 自学视频
 * @author madreain
 * @desc
 * @time 2017/4/20
 */

public class VideoKnowledge extends BmobObject implements Serializable {
    int selfid;//视频集合下的id
    int vid;//视频对应的id
    String title;//知识标题
    String file;//本地的路径
    BmobFile bmobFile;//文件

    public VideoKnowledge() {
    }

    public VideoKnowledge(int selfid, int vid, String title, String file, BmobFile bmobFile) {
        this.selfid = selfid;
        this.vid = vid;
        this.title = title;
        this.file = file;
        this.bmobFile = bmobFile;
    }

    public int getSelfid() {
        return selfid;
    }

    public void setSelfid(int selfid) {
        this.selfid = selfid;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public BmobFile getBmobFile() {
        return bmobFile;
    }

    public void setBmobFile(BmobFile bmobFile) {
        this.bmobFile = bmobFile;
    }
}
