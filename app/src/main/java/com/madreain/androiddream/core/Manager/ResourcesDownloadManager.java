package com.madreain.androiddream.core.Manager;

/**
 * 资源下载
 * @author madreain
 * @desc
 * @time 2017/7/4
 */

public class ResourcesDownloadManager {
    private static ResourcesDownloadManager instance;

    private ResourcesDownloadManager() {
    }

    public static ResourcesDownloadManager getInstance() {
        if (instance == null) {
            synchronized (ResourcesDownloadManager.class) {
                if (instance == null) {
                    instance = new ResourcesDownloadManager();
                }
            }
        }
        return instance;
    }



}
