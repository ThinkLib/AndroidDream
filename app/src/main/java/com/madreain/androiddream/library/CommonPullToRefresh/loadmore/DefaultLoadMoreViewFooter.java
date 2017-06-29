/*
Copyright 2015 Chanven

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.madreain.androiddream.library.CommonPullToRefresh.loadmore;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.madreain.androiddream.R;


/**
 * default load more view
 */
public class DefaultLoadMoreViewFooter implements ILoadMoreViewFactory {

    @Override
    public ILoadMoreView madeLoadMoreView() {
        return new LoadMoreHelper();
    }

    private class LoadMoreHelper implements ILoadMoreView {

        protected View footerView;
        protected TextView footerTv;
        //        protected ProgressBar footerBar;
        private ImageView img_rotate;
        private Animation animation;
        protected OnClickListener onClickRefreshListener;

        @Override
        public void init(FootViewAdder footViewHolder, OnClickListener onClickRefreshListener) {
            footerView = footViewHolder.addFootView(R.layout.loadmore_default_footer);
            footerTv = (TextView) footerView.findViewById(R.id.loadmore_default_footer_tv);
//            footerBar = (ProgressBar) footerView.findViewById(R.id.loadmore_default_footer_progressbar);
            img_rotate= (ImageView) footerView.findViewById(R.id.img_rotate);
            this.onClickRefreshListener = onClickRefreshListener;
            showNormal();
        }

        @Override
        public void showNormal() {
            footerTv.setText("点击加载更多");
//            footerBar.setVisibility(View.GONE);
            img_rotate.setVisibility(View.GONE);
            cleainRateAnimation();
            footerView.setOnClickListener(onClickRefreshListener);
        }

        @Override
        public void showLoading() {
            footerTv.setText("正在加载中...");
//            footerBar.setVisibility(View.VISIBLE);
            img_rotate.setVisibility(View.VISIBLE);
            startRateAnimation();
            footerView.setOnClickListener(null);
        }

        @Override
        public void showFail(Exception exception) {
            footerTv.setText("加载失败，点击重新");
//            footerBar.setVisibility(View.GONE);
            img_rotate.setVisibility(View.GONE);
            cleainRateAnimation();
            footerView.setOnClickListener(onClickRefreshListener);
        }

        @Override
        public void showNomore() {
            footerTv.setText("已经加载完毕");
//            footerBar.setVisibility(View.GONE);
            img_rotate.setVisibility(View.GONE);
            cleainRateAnimation();
            footerView.setOnClickListener(null);
        }

        @Override
        public void showFinishmore() {
            footerTv.setText("已经到底了");
//            footerBar.setVisibility(View.GONE);
            img_rotate.setVisibility(View.GONE);
            cleainRateAnimation();
            footerView.setOnClickListener(null);
        }

        @Override
        public void showNetWorkError() {
            footerTv.setText("网络异常");
//            footerBar.setVisibility(View.GONE);
            img_rotate.setVisibility(View.GONE);
            cleainRateAnimation();
            footerView.setOnClickListener(null);
        }

        @Override
        public void setFooterVisibility(boolean isVisible) {
            footerView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }


        //设置旋转
        private void startRateAnimation(){
            if(animation==null){
                //设置一个匀速的旋转动画
                animation = new RotateAnimation(0, 25199, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                animation.setInterpolator(new LinearInterpolator());
                animation.setDuration(50000);
                animation.setFillAfter(true);
                LinearInterpolator lin = new LinearInterpolator();
                animation.setInterpolator(lin);
                img_rotate.setAnimation(animation);

            }
        }

        private void cleainRateAnimation(){
            if(animation!=null){
                img_rotate.clearAnimation();
                animation=null;
            }
        }

    }

}
