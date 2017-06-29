package com.madreain.androiddream.core.Model;

/**
 * @author madreain
 * @desc
 * @time 2017/4/27
 * 更新提示
 */

public class UpdateVersionModel {
    private String cversion;// 当前版本

    private String tcversion;// 升级版本

    private String ctype;// 客户端类型 2 android 1 ios

    private String content;// 升级内容

    private String downloadurl;// 下载链接

    private int state;// 升级提示

    public UpdateVersionModel() {
    }

    public UpdateVersionModel(String cversion, String tcversion, String ctype, String content, String downloadurl, int state) {
        this.cversion = cversion;
        this.tcversion = tcversion;
        this.ctype = ctype;
        this.content = content;
        this.downloadurl = downloadurl;
        this.state = state;
    }

    public String getCversion() {
        return cversion;
    }

    public void setCversion(String cversion) {
        this.cversion = cversion;
    }

    public String getTcversion() {
        return tcversion;
    }

    public void setTcversion(String tcversion) {
        this.tcversion = tcversion;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDownloadurl() {
        return downloadurl;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "UpdateVersionModel{" +
                "cversion='" + cversion + '\'' +
                ", tcversion='" + tcversion + '\'' +
                ", ctype='" + ctype + '\'' +
                ", content='" + content + '\'' +
                ", downloadurl='" + downloadurl + '\'' +
                ", state=" + state +
                '}';
    }
}
