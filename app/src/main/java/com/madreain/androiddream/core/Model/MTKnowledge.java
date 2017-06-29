package com.madreain.androiddream.core.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import cn.bmob.v3.BmobObject;

/**
 * Created by madreain on 2017/4/1.
 * 某个分类下对应的相关知识
 * mid
 */
@DatabaseTable(tableName = "mtknowledge")
public class MTKnowledge extends BmobObject {
    @DatabaseField
    int mid;//某个一级分类下
    @DatabaseField
    int smid;//大分类下的小分类
    @DatabaseField
    int kid;//知识的id
    @DatabaseField
    String title;//知识标题
    @DatabaseField
    String desc;//项目描述
    @DatabaseField
    String author;//作者
    @DatabaseField
    String sorce;//来源
    @DatabaseField
    String url;//文章的链接
    @DatabaseField
    long ptime;//上传时间
    @DatabaseField
    long updatetime;//更新时间

    public MTKnowledge() {
    }

    public MTKnowledge(int mid, int smid, int kid, String title, String desc, String author, String sorce, String url, long ptime, long updatetime) {
        this.mid = mid;
        this.smid = smid;
        this.kid = kid;
        this.title = title;
        this.desc = desc;
        this.author = author;
        this.sorce = sorce;
        this.url = url;
        this.ptime = ptime;
        this.updatetime = updatetime;
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

    public int getKid() {
        return kid;
    }

    public void setKid(int kid) {
        this.kid = kid;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSorce() {
        return sorce;
    }

    public void setSorce(String sorce) {
        this.sorce = sorce;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getPtime() {
        return ptime;
    }

    public void setPtime(long ptime) {
        this.ptime = ptime;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }
}
