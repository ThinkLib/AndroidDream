package com.madreain.androiddream.core.Manager.callback;


/**
 * Callback
 * Created by madreain on 2017/3/30.
 */
public interface MBCallback {

    interface MBDataCallback {

        void onSuccess();

        void onError(String code);

        void onFinished();
    }

    interface MBSyncCallback<T> {

        void onSuccess(T model);

        void onError(T model);

        void onFinished();
    }

    interface MBValueCallBack<T> {

        void onSuccess(T result);

        void onError(String code);

        void onFinished();
    }

    interface MBResultCallBack<T> {

        void onSuccess(T result);

        void onError(String code);

        void onFinished();
    }

    interface MBDownloadCallback<T> {

        void onSuccess(T result);

        void onLoading(long total, long current, boolean isDownloading);

        void onError();

        void onFinished();
    }

    interface MBUploadCallback {

        void onUploadSuccess();

        void onUploadProgress(int total, int current);

        void onUploadProgress(int current, long totalSize, long sendSize);

        void onUploadFail(int current, String code);
    }

    interface MUploadCallback {

        void onUploadSuccess();

        void onUploadProgress(int total, int current);

        void onUploadProgress(int current, long totalSize, long sendSize);

        void onUploadFail(String code);
    }

    interface MBPhotoUploadCallback<T> {

        void onUploadSucceed(T result);

        void onUploadFailed(String codemsg);

        void onUploadProgress(long totalSize, long sendSize);

        void onUploadStateChange();
    }

}
