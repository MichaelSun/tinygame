package com.michael.tinygame.touch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import cn.domob.android.ads.DomobAdView;
import com.michael.tinygame.touch.Config.AppConfig;
import com.michael.tinygame.touch.fragment.TouchFragment;
import com.michael.tinygame.touch.setting.SettingManager;
import com.umeng.analytics.MobclickAgent;


public class MainActivity extends Activity implements TouchFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingManager.getInstance().init(getApplicationContext());

        setContentView(R.layout.main_activity);

        initView("touch");
        initBannerAd();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showQutiDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeView("touch");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_reopen) {
            showReopenDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showReopenDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                                 .setTitle("重新开始")
                                 .setMessage("确定要重新开始么？")
                                 .setNegativeButton("不玩了", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                     }
                                 })
                                 .setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         retTouchView("touch");
                                     }
                                 })
                                 .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private void showQutiDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                                 .setTitle("退出")
                                 .setMessage("确定要退出么？")
                                 .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                     }
                                 })
                                 .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         retTouchView("touch");
                                         finish();
                                     }
                                 })
                                 .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void retTouchView(String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = TouchFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.container, fragment, tag).commit();
        } else {
            if (fragment instanceof TouchFragment) {
                ((TouchFragment) fragment).resetView();
            }
        }
    }

    private void initView(String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = TouchFragment.newInstance();
        }
        fragmentManager.beginTransaction().replace(R.id.container, fragment, tag).commit();
    }

    private void removeView(String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void initBannerAd() {
        DomobAdView adview = new DomobAdView(this, AppConfig.DOMOD_PUBLISH_KEY, AppConfig.DOMOD_PLACEMENT_KEY);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.ad_content);
        layout.addView(adview, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                                                  RelativeLayout.LayoutParams.WRAP_CONTENT));
    }
}