package com.madreain.androiddream.core.Model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * @author madreain
 * @desc
 * @time 2017/7/4
 */

public class ResourcesModel  extends BmobObject implements Serializable {
    public int rid;//资源id
    public BmobFile bmobFile;//文件资源下载地址
    public String desc;//描述
    public String tag;//标签   关键字

}
