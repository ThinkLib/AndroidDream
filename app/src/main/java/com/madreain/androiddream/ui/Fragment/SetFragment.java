package com.madreain.androiddream.ui.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.madreain.androiddream.R;
import com.madreain.androiddream.core.Constants;
import com.madreain.androiddream.core.Manager.ClientUpdateManager;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.UpdateVersionModel;
import com.madreain.androiddream.library.kprogresshud.KProgressHUD;
import com.madreain.androiddream.ui.Activity.AboutActivity;
import com.madreain.androiddream.ui.Activity.FeedBackActivity;
import com.madreain.androiddream.ui.Activity.GithubActivity;
import com.madreain.androiddream.utils.DataCleanManager;
import com.madreain.androiddream.utils.MUtil;
import com.madreain.androiddream.utils.UmengUtil;
import com.madreain.androiddream.views.RoundImageView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;


/**
 * 设置界面
 *
 * @author madreain
 * @time 2017/4/5
 */

public class SetFragment extends Fragment {
    private View rootview = null;
    private Context mContext;
    private TextView txt_title;
    private ImageView img_return;
    private ImageView img_add;
    private RoundImageView roundimageview;
    private TextView txt_github;
    private TextView txt_message_clean;
    private TextView txt_datachace;
    private TextView txt_faq;
    private TextView txt_about;
    private TextView txt_share;
    private TextView txt_update;
    private TextView txt_musang;
    private TextView txt_qq;
    //缓存
    private long chacheSize = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_set, null);
        mContext = getContext();
        initView(rootview);
        initData();
        initListener();
        return rootview;
    }

    private void initView(View rootview) {
        txt_title = (TextView) rootview.findViewById(R.id.txt_title);
        img_return = (ImageView) rootview.findViewById(R.id.img_return);
        img_add = (ImageView) rootview.findViewById(R.id.img_add);
        roundimageview = (RoundImageView) rootview.findViewById(R.id.roundimageview);
        txt_github = (TextView) rootview.findViewById(R.id.txt_github);
        txt_message_clean = (TextView) rootview.findViewById(R.id.txt_message_clean);
        txt_datachace = (TextView) rootview.findViewById(R.id.txt_datachace);
        txt_faq = (TextView) rootview.findViewById(R.id.txt_faq);
        txt_about = (TextView) rootview.findViewById(R.id.txt_about);
        txt_share = (TextView) rootview.findViewById(R.id.txt_share);
        txt_update = (TextView) rootview.findViewById(R.id.txt_update);
        txt_musang = (TextView) rootview.findViewById(R.id.txt_musang);
        txt_qq = (TextView) rootview.findViewById(R.id.txt_qq);
        //actionbar的设置
        img_return.setVisibility(View.GONE);
        img_add.setVisibility(View.GONE);

    }

    private void initData() {
        //缓存
        try {
            chacheSize = DataCleanManager.getFolderSize(mContext.getExternalCacheDir())
                    + DataCleanManager.getFolderSize(mContext.getCacheDir());//+ FileUtils.getUserDataSize();
            //FileUtils.getUserDataSize()  视频   图片  骑行数据
            txt_datachace.setText(DataCleanManager.getFormatSize(chacheSize));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initListener() {
        //点击github地址进入github个人主页
        txt_github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GithubActivity.class);
                intent.putExtra("Github", Constants.Github);
                startActivity(intent);
            }
        });
        //缓存的清理
        txt_message_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("提示");
                builder.setMessage("确定要清除缓存？");

                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (chacheSize > 0) {
                            DataCleanManager.cleanExternalCache(mContext);
//                        DataCleanManager.cleanDatabases(mContext);//数据库
                            DataCleanManager.cleanInternalCache(mContext);
//                    DataCleanManager.cleanSharedPreference(mContext);
//                        DataCleanManager.cleanApplicationData(mContext);
                            DataCleanManager.clearAllCache(mContext);


                            try {
                                chacheSize = DataCleanManager.getFolderSize(mContext.getExternalCacheDir())
                                        + DataCleanManager.getFolderSize(mContext.getCacheDir());
                                txt_datachace.setText(DataCleanManager.getFormatSize(chacheSize));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            dialog.dismiss();
                        }


                    }
                });
                builder.show();
            }
        });
        //意见反馈
        txt_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 用户反馈
                startActivity(new Intent(mContext, FeedBackActivity.class));

            }
        });
        //关于掘梦
        txt_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AboutActivity.class));
            }
        });
        // 推荐掘梦
        txt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMWeb web = new UMWeb(Constants.Madreain);
                web.setTitle("掘梦");//标题
                web.setThumb(new UMImage(getActivity(),Constants.Madreain_icon));  //缩略图
                web.setDescription("下载掘梦，了解更多Android前端知识");//描述
                UmengUtil.share(getActivity(), web);
            }
        });
        // 更新版本
        txt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 友盟版本更新
                final KProgressHUD hud = KProgressHUD.create(mContext)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setCancellable(false)
                        .setAnimationSpeed(1)
                        .setDimAmount(0.5f).show();

                ClientUpdateManager.getInstance().getClientUpdateConfig(new MBCallback.MBValueCallBack<UpdateVersionModel>() {
                    @Override
                    public void onSuccess(final UpdateVersionModel result) {
                        hud.dismiss();
                        if (result != null && result.getCversion() != null) {
                            if (result.getState() == 0) {
                                Toast.makeText(mContext, "已是最新版本", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (MUtil.getVersionCode(mContext).equals(result.getTcversion())) {
                                Toast.makeText(mContext, "已是最新版本", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!result.getCversion().equals(result.getTcversion())) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                    builder.setTitle("更新提示");
                                    builder.setMessage(result.getContent());
                                    builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse(result.getDownloadurl()));
                                            startActivity(intent);
                                            dialog.dismiss();
                                        }
                                    });
                                    if (result.getState() == 1) {
                                        builder.setNegativeButton("暂不更新", null);
                                    }
                                    builder.show();
                                } else {
                                    Toast.makeText(mContext, "已是最新版本", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(mContext, "已是最新版本", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String code) {
                        hud.dismiss();
                        Toast.makeText(mContext, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFinished() {

                    }
                });

            }
        });
        //打赏
        txt_musang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //支付宝  微信打赏
                // TODO: 2017/4/21
                // https://juejin.im/post/5833975961ff4b006c249067
//                https://github.com/mayubao/Android-Pay
//                http://www.jianshu.com/p/2aa2e8748476
                final Dialog mDialog = new Dialog(getContext(), R.style.MyDialogStyle);
                mDialog.setContentView(R.layout.dialog_pay);
                mDialog.setCanceledOnTouchOutside(true);
                mDialog.findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //点击外部取消
                        mDialog.cancel();
                    }
                });
                mDialog.findViewById(R.id.dialog_cancel)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //点击取消按钮取消
                                mDialog.cancel();
                            }
                        });
                //支付宝支付
                mDialog.findViewById(R.id.alipay)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                mDialog.dismiss();
                            }
                        });
                //微信支付
                mDialog.findViewById(R.id.weixin)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                mDialog.dismiss();
                            }
                        });
                mDialog.show();


            }
        });
        // 添加qq群
        txt_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加qq群
                joinQQGroup(Constants.QQKey);
            }
        });

    }


    /****************
     * 发起添加群流程。群号：2022.8.8(143565027) 的 key 为： ZErriuIkbZfBDaT7Jnl05CjNbnZGeeKR
     * 调用 joinQQGroup(ZErriuIkbZfBDaT7Jnl05CjNbnZGeeKR) 即可发起手Q客户端申请加群 2022.8.8(143565027)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(Constants.QQUrl + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            Toast.makeText(mContext, "未安装手Q或安装的版本不支持", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void refreshFragmentData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);

    }
}
