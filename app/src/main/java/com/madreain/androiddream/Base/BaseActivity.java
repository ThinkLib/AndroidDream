package com.madreain.androiddream.Base;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;


public abstract class BaseActivity extends FragmentActivity {
	/** 上个组件传入的数据 */
	public abstract void preComponentData(Bundle extra);

	/** 设置ActionBar导航信息 */
	public abstract void setActionBar(ActionBar actionBar);

	// 生命周期日志管理------------------------------------------------
	private static final String TAG = "BaseActivity";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		preComponentData(getIntent().getExtras());
		setActionBar(getActionBar());
		onLineActivitys.add(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		currentFragment = null;
		onLineActivitys.remove(this);
		System.gc();
	}

	// Activity跳转---------------------------------------------------
	protected void openActivity(Class<?> cls, Bundle extras) {
		Intent intent = new Intent(this, cls);
		intent.putExtras(extras);
		startActivity(intent);
	}

	protected void openActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}

	// "在线"Activity管理---------------------------------------------
	private static ArrayList<BaseActivity> onLineActivitys = new ArrayList<BaseActivity>();

	protected void finishAll() {
		int len = onLineActivitys.size();
		for (int i = 0; i < len; i++) {
			onLineActivitys.get(i).finish();
		}
	}

	// Fragment跳转---------------------------------------------------
	private static BaseFragment currentFragment;

	public void replaceFragment(int contentID, BaseFragment toFragment, boolean isAdd2Back) {
		String tag = toFragment.getClass().getSimpleName();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(contentID, toFragment);
		if (isAdd2Back) {
			ft.addToBackStack(tag);
		}
		ft.commit();
		currentFragment = toFragment;
	}

	public void showFragment(int contentID, BaseFragment toFragment) {
		showFragment(contentID, toFragment, false);
	}

	public void showFragment(int contentID, BaseFragment toFragment, boolean isToBackStack) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if (currentFragment != null) {
			ft.hide(currentFragment);
		}
		String tag = toFragment.getClass().getSimpleName();
		Fragment fragment = fm.findFragmentByTag(tag);
		if (fragment == null) {
			ft.add(contentID, toFragment, tag);
		} else {
			ft.show(toFragment);
		}
		if (isToBackStack) {
			ft.addToBackStack(tag);
		}
		ft.commit();
		currentFragment = toFragment;
	}
}
