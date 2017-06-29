package com.madreain.androiddream.Base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment封装
 * 
 * @ Activity与Fragment的attach时,接口的绑定 {@link BaseFragmentCallback}
 * @ 提供了Fragment对Attach的Activity回调方法
 *       {@link #callbackToActivity(int, Object)}
 * @ 重新封装了当前Fragment视图创建及数据加载、重置
 *       {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
 * 
 * @author wujun
 */
public abstract class BaseFragment extends Fragment {

	private BaseFragmentCallback callback;
	private String defTag;

	public BaseFragment() {
		defTag = getClass().getSimpleName();
	}

	public void setTag(String tag) {
		this.defTag = tag;
	}

	/** 将当前Fragment中的事件及数据反馈到其所依附的Activity */
	public void callbackToActivity(int eventID, Object data) {
		if (callback != null) {
			callback.onCallback(defTag, eventID, data);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof BaseFragmentCallback) {
			callback = (BaseFragmentCallback) activity;
		}
	}

	// -------------------------------------------------------------------------
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = onInitloadView(inflater, container, savedInstanceState);
			onInitloadData();
		}
		onResetData();
		return rootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (rootView != null) {
			ViewGroup viewGroup = (ViewGroup) rootView.getParent();
			if (viewGroup != null) {
				viewGroup.removeView(rootView);
			}
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (hidden) {
			onPause();
		} else {
			onResume();
			onResetData();
		}
	}

	/** 当前Fragment的布局, 只有在第一次createView时才会触发(一般会在此方法中也对各控件进行初始加载工作) */
	public abstract View onInitloadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	/** 当前Fragment的数据加载, 只有在第一次createView时才会触发 */
	public abstract void onInitloadData();

	/** 当前Fragment的布局数据重置, 每次onCreateView和onHiddenChanged(false)都会触发 */
	public abstract void onResetData();
}
