package com.madreain.androiddream.ui.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.madreain.androiddream.Base.BaseFragmentCallback;
import com.madreain.androiddream.core.Manager.ClientUpdateManager;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.UpdateVersionModel;
import com.madreain.androiddream.ui.Fragment.ReviewFragment;
import com.madreain.androiddream.ui.Fragment.SelfStudyFragment;
import com.madreain.androiddream.ui.Fragment.SetFragment;
import com.madreain.androiddream.R;
import com.madreain.androiddream.utils.MUtil;
import com.madreain.androiddream.utils.StatusBarUtils;
import com.umeng.analytics.MobclickAgent;

import net.youmi.android.normal.spot.SpotManager;

/**
 * @author madreain
 *         主界面  activity与fragment的交互
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener, BaseFragmentCallback {

    private RadioButton RB_review;
    private RadioButton RB_selfstudy;
    private RadioButton RB_set;


    private FragmentManager fm;
    private Fragment currentFragment;//主界面中当前显示的Fragment
    ReviewFragment reviewFragment;
    SelfStudyFragment selfStudyFragment;
    SetFragment setFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //状态栏颜色的设置
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_main);
        StatusBarUtils.SetStatusBar(getWindow(), this, getResources(), "#03aaf4", linearLayout);

        initView();
        initFragment();
        initListener();
        checkMBVersion();
    }

    //设置android app 的字体大小不受系统字体大小改变的影响
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    private void initView() {
        RB_review = (RadioButton) findViewById(R.id.RB_review);
        RB_selfstudy = (RadioButton) findViewById(R.id.RB_selfstudy);
        RB_set = (RadioButton) findViewById(R.id.RB_set);
    }

    private void initFragment() {
        fm = getSupportFragmentManager();
        if (reviewFragment == null) {
            reviewFragment = new ReviewFragment();
        }
        SmartShow(reviewFragment);
        currentFragment = reviewFragment;/*第一次显示过后需要把当前显示的fragment设置成当前第一次进来的页面的fragment*/
    }

    private void initListener() {
        RB_review.setOnClickListener(this);
        RB_selfstudy.setOnClickListener(this);
        RB_set.setOnClickListener(this);
    }

    /**
     * 检查版本更新
     */
    private void checkMBVersion() {
        ClientUpdateManager.getInstance().getClientUpdateConfig(new MBCallback.MBValueCallBack<UpdateVersionModel>() {
            @Override
            public void onSuccess(final UpdateVersionModel result) {
                if (result != null && result.getCversion() != null) {
                    if (result.getState() == 0) {
                        return;
                    }
                    if (MUtil.getVersionCode(MainActivity.this).equals(result.getTcversion())) {
//                        Toast.makeText(MainActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!result.getCversion().equals(result.getTcversion())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("更新提示");
                            builder.setMessage(result.getContent());
                            builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(result.getDownloadurl()));
                                    startActivity(intent);
                                    dialog.dismiss();

                                    if (result.getState() == 2) {
                                        MainActivity.this.finish();
                                    }
                                }
                            });
                            if (result.getState() == 1) {
                                builder.setNegativeButton("暂不更新", null);
                            }

                            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    if (result.getState() == 2) {
                                        dialog.dismiss();
                                        MainActivity.this.finish();
                                    } else {
                                        dialog.dismiss();
                                    }
                                }
                            });
                            builder.show();
                        }
                    }
                }
            }

            @Override
            public void onError(String code) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.RB_review:
                if (currentFragment != reviewFragment) {
                    if (reviewFragment == null) {
                        reviewFragment = new ReviewFragment();
                    }
                    SmartShow(reviewFragment);
                    currentFragment = reviewFragment;
                } else {
                    reviewFragment.refreshFragmentData();
                }
                break;
            case R.id.RB_selfstudy:
                if (currentFragment != selfStudyFragment) {
                    if (selfStudyFragment == null) {
                        selfStudyFragment = new SelfStudyFragment();
                    }
                    SmartShow(selfStudyFragment);
                    currentFragment = selfStudyFragment;
                } else {
                    selfStudyFragment.refreshFragmentData();
                }
                break;
            case R.id.RB_set:
                if (currentFragment != setFragment) {
                    if (setFragment == null) {
                        setFragment = new SetFragment();
                    }
                    SmartShow(setFragment);
                    currentFragment = setFragment;
                } else {
                    setFragment.refreshFragmentData();
                }
                break;
        }
    }

    @Override
    public void onCallback(String tag, int eventID, Object data) {

    }

    /**
     * Activity 中Fragment之间的切换
     */
    public void SmartShow(Fragment showFrgment) {
        FragmentTransaction ft = fm.beginTransaction();
        String showName = showFrgment.getClass().getSimpleName();
        Fragment sfrgment = fm.findFragmentByTag(showName);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        if (sfrgment == null && !showFrgment.isAdded()) {
            ft.add(R.id.cotentID, showFrgment, showName);
        } else {
            ft.show(sfrgment);
        }
        ft.commitAllowingStateLoss();
        currentFragment = sfrgment;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private long currentTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - currentTime > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                currentTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpotManager.getInstance(getBaseContext()).onAppExit();
    }
}
