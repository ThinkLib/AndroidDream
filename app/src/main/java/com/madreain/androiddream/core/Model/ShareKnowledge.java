package com.madreain.androiddream.core.Model;


import cn.bmob.v3.BmobObject;

/**
 * 提交分享知识给我
 * @author madreain
 * @desc
 * @time 2017/4/20
 */

public class ShareKnowledge extends BmobObject {
    String title;//知识标题
    String desc;//项目描述
    String author;//作者
    String sorce;//来源
    String url;//文章的链接
    String email;//邮箱

    public ShareKnowledge() {
    }

    public ShareKnowledge(String title, String desc, String author, String sorce, String url, String email) {
        this.title = title;
        this.desc = desc;
        this.author = author;
        this.sorce = sorce;
        this.url = url;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ShareKnowledge{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", author='" + author + '\'' +
                ", sorce='" + sorce + '\'' +
                ", url='" + url + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
