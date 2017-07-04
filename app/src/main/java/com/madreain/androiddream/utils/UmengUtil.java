package com.madreain.androiddream.utils;

import android.app.Activity;
import android.widget.Toast;

import com.madreain.androiddream.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.utils.Log;

/**
 * @author madreain
 * @desc
 * @time 2017/4/12
 */

public class UmengUtil {
    //  http://dev.umeng.com/social/android/operation 第三方账号的设置
    public static void share(Activity activity, UMWeb umWeb) {

//        ShareBoardConfig shareBoardConfig = new ShareBoardConfig();
//        //设置分享显示在什么位置
////        shareBoardConfig.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM);
//        shareBoardConfig.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);
//        shareBoardConfig.setCancelButtonVisibility(true);
////        shareBoardConfig.setShareboardBackgroundColor(activity.getResources().getColor(R.color.M4A4D4F));
////        shareBoardConfig.setTitleTextColor(activity.getResources().getColor(R.color.MD9D9D9));
//        shareBoardConfig.setTitleVisibility(false);
////        shareBoardConfig.setMenuItemTextColor(activity.getResources().getColor(R.color.MD9D9D9));
//        shareBoardConfig.setMenuItemIconPressedColor(activity.getResources().getColor(R.color.MFFFFFF));
//        shareBoardConfig.setCancelButtonVisibility(false);
//        shareBoardConfig.setIndicatorVisibility(false);
//        shareBoardConfig.setMenuItemBackgroundColor(activity.getResources().getColor(R.color.M4A4D4F));


        new ShareAction(activity)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
//                        SHARE_MEDIA.SINA,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .withMedia(umWeb)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
//                        Log.e("UmengUtil", "share_media:" + share_media);
//                        Toast.makeText(MadreainApplication.getContext(), share_media + "share_media", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        Toast.makeText(MadreainApplication.getContext(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        Toast.makeText(MadreainApplication.getContext(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                        if (t != null) {
                            Log.e("UmengUtil", t.getMessage().toString());
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        Toast.makeText(MadreainApplication.getContext(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                    }
                })
                .open();
    }

}
