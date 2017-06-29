package com.madreain.androiddream.Base;

public interface BaseFragmentCallback {
	/**
	 * 当点击Fragment上组件时调用
	 *
	 * @param tag
	 *            当前事件所发生在的Fragment标签(用做区分各Fragment的标识),默认为当前Fragment类名
	 * @param eventID
	 *            当前事件的编号
	 * @param data
	 *            需携带的数据,没有数据传入null
	 *
	 * @see 在各子类Fragment中可用{@link BaseFragment#callbackToActivity(View, Object)}
	 *      来触发事件
	 */
	void onCallback(String tag, int eventID, Object data);
}
