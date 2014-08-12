package com.michael.tinygame.touch;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.michael.tinygame.touch.fragment.TouchFragment;
import com.michael.tinygame.touch.setting.SettingManager;


public class MainActivity extends Activity implements TouchFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingManager.getInstance().init(getApplicationContext());

        setContentView(R.layout.main_activity);

        initView("touch");
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = TouchFragment.newInstance();
        }
        fragmentManager.beginTransaction().replace(R.id.container, fragment, tag).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
