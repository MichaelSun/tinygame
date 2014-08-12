package com.michael.tinygame.touch.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by michael on 14-8-12.
 */
public class SettingManager {
    private static SettingManager ourInstance = new SettingManager();

    private SharedPreferences mSP;
    private SharedPreferences.Editor mEditor;

    public static SettingManager getInstance() {
        return ourInstance;
    }

    private SettingManager() {
    }

    public void init(Context context) {
        mSP = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSP.edit();
    }

    public void setTouchBestCount(int count) {
        mEditor.putInt("touch_count", count).commit();
    }

    public int getTouchBestCount() {
        return mSP.getInt("touch_count", 0);
    }
}
