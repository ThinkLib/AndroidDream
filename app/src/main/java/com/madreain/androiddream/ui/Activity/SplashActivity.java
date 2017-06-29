package com.madreain.androiddream.ui.Activity;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.madreain.androiddream.R;
import com.madreain.androiddream.utils.SharedPreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import net.youmi.android.normal.spot.SplashViewSettings;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;

/**
 * @author madreain
 *         动画界面
 */
public class SplashActivity extends AppCompatActivity {
    private RelativeLayout mRootLayout;
    private ViewPager mViewPager;
    private int colorBg[];
    private ArgbEvaluator mArgbEvaluator;
    private int barAlpha = 0;
    private Context mContext;


    /**
     * 将小圆点的图片用数组表示
     */
    private ImageView[] imageViews;
    //包裹小圆点的LinearLayout
    private LinearLayout viewPoints;
    private ImageView imageView;

    SharedPreferencesUtil sharedPreferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_splash);
        mContext = getBaseContext();
        //判断是否是第一次进入，是否需要走引导界面
        sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
        boolean isStart = sharedPreferencesUtil.getValue("madreain", false);
        if (isStart) {
//            //有米广告的插入
//            SplashViewSettings splashViewSettings = new SplashViewSettings();
//            splashViewSettings.setAutoJumpToTargetWhenShowFailed(true);
//            splashViewSettings.setTargetClass(MainActivity.class);
//            // 使用默认布局参数
//            splashViewSettings.setSplashViewContainer(mRootLayout);
////            // 使用自定义布局参数
////            splashViewSettings.setSplashViewContainer(ViewGroup splashViewContainer,
////                    ViewGroup.LayoutParams splashViewLayoutParams);
//            SpotManager.getInstance(mContext).showSplash(mContext,
//                    splashViewSettings, new SpotListener() {
//                        @Override
//                        public void onShowSuccess() {
//                            Log.d("SplashActivity", "广告显示成功");
////                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
////                            overridePendingTransition(0, 0);
////                            finish();
//                        }
//
//                        @Override
//                        public void onShowFailed(int i) {
//                            Log.d("SplashActivity", "广告显示失败" + i);
////                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
////                            overridePendingTransition(0, 0);
////                            finish();
//                        }
//
//                        @Override
//                        public void onSpotClosed() {
//
//                        }
//
//                        @Override
//                        public void onSpotClicked(boolean b) {
//
//                        }
//                    });


            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            overridePendingTransition(0, 0);
            finish();
        } else {
            sharedPreferencesUtil.setValue("madreain", true);
        }

        mRootLayout = (RelativeLayout) findViewById(R.id.rl_root);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.light_green_500), barAlpha);
        mArgbEvaluator = new ArgbEvaluator();
        colorBg = getResources().getIntArray(R.array.splash_bg);
        IntroPager introPager = new IntroPager(R.array.splash_icon, R.array.splash_desc);
        mViewPager.setAdapter(introPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int color = (int) mArgbEvaluator.evaluate(positionOffset, colorBg[position % colorBg.length],
                        colorBg[(position + 1) % colorBg.length]);
                StatusBarUtil.setColor(SplashActivity.this, color, barAlpha);
                mRootLayout.setBackgroundColor(color);
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < imageViews.length; i++) {
                    imageViews[position].setBackgroundResource(R.mipmap.intro_icon_2);
                    //不是当前选中的page，其小圆点设置为未选中的状态
                    if (position != i) {
                        imageViews[i].setBackgroundResource(R.mipmap.intro_icon_nor_2);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });


        viewPoints = (LinearLayout) findViewById(R.id.viewGroup);
        //添加小圆点的图片
        imageViews = new ImageView[4];
        for (int i = 0; i < 4; i++) {
            imageView = new ImageView(this);
            //设置小圆点imageview的参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.setMargins(10, 0, 10, 0);
            imageView.setLayoutParams(layoutParams);//创建一个宽高均为20 的布局
            imageView.setPadding(20, 0, 20, 0);
            //默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
            if (i == 0) {
                imageView.setBackgroundResource(R.mipmap.intro_icon_2);
            } else {
                imageView.setBackgroundResource(R.mipmap.intro_icon_nor_2);
            }
            //将imageviews添加到小圆点视图组
            viewPoints.addView(imageView);
            imageViews[i] = imageView;

        }

    }


    private class IntroPager extends PagerAdapter {

        private String[] mDescs;

        private TypedArray mIcons;

        public IntroPager(int icoImage, int des) {
            mDescs = getResources().getStringArray(des);
            mIcons = getResources().obtainTypedArray(icoImage);
        }

        @Override
        public int getCount() {
            return mIcons.length();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemLayout = getLayoutInflater().inflate(R.layout.layout_app_intro, container, false);
            ImageView mImage = (ImageView) itemLayout.findViewById(R.id.iv_img);
            TextView mTextView = (TextView) itemLayout.findViewById(R.id.tv_desc);
            Button mButton = (Button) itemLayout.findViewById(R.id.btn_launch);
            mImage.setImageResource(mIcons.getResourceId(position, 0));
            mTextView.setText(mDescs[position]);
            if (position == getCount() - 1) {
                mButton.setVisibility(View.VISIBLE);
            } else {
                mButton.setVisibility(View.GONE);
            }
            container.addView(itemLayout);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
            });
            return itemLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 开屏展示界面的 onDestroy() 回调方法中调用
        SpotManager.getInstance(mContext).onDestroy();
    }

}
