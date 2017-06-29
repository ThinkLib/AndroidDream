package com.madreain.androiddream.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class SharedPreferencesUtil {
	private Context mContext;
	private Editor mEditor;
	private SharedPreferences mSp = null;

	public SharedPreferencesUtil(Context context, SharedPreferences sp) {
		this.mContext = context;
		this.mSp = sp;
		this.mEditor = mSp.edit();
	}

	public SharedPreferencesUtil(Context context, String fileName) {
		this(context, context.getSharedPreferences(fileName,
				Context.MODE_WORLD_WRITEABLE));
	}

	public SharedPreferencesUtil(Context context) {
		this(context, PreferenceManager.getDefaultSharedPreferences(context));
	}

	public void setValue(String key, boolean value) {
		mEditor.putBoolean(key, value);
		mEditor.commit();
	}

	public boolean getValue(String key, boolean defaultValue) {
		return mSp.getBoolean(key, defaultValue);
	}

	public void setValue(String key, int value) {
		mEditor.putInt(key, value);
		mEditor.commit();
	}

	public int getValue(String key, int defaultValue) {
		return mSp.getInt(key, defaultValue);
	}

	public void setValue(String key, long value) {
		mEditor.putLong(key, value);
		mEditor.commit();
	}

	public long getValue(String key, long defaultValue) {
		return mSp.getLong(key, defaultValue);
	}

	public void setValue(String key, String value) {
		mEditor.putString(key, value);
		mEditor.commit();
	}

	public String getValue(String key, String defaultValue) {

		return mSp.getString(key, defaultValue);
	}

	/**
	 * 存用户类
	 * @param key
	 * @param obj
     */
	public void setValue(String key, Object obj) {
		if (obj instanceof Serializable) {// obj必须实现Serializable接口，否则会出问题
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(obj);
				String string64 = new String(Base64.encode(baos.toByteArray(),
						0));
			//	Editor editor = getSharedPreferences(context).edit();
				mEditor.putString(key, string64).commit();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException(
					"the obj must implement Serializble");
		}
	}
	public  Object getBean(String key) {
		Object obj = null;
		try {
			String base64 = mSp.getString(key, "");
			//String base64 = getSharedPreferences(context).getString(key, "");
			if (base64.equals("")) {
				return null;
			}
			byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			obj = ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	public void remove(String key) {
		mEditor.remove(key);
		mEditor.commit();
	}

	public void clear() {
		mEditor.clear();
		mEditor.commit();
	}

}
