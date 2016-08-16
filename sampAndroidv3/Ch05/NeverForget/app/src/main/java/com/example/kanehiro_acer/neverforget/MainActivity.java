package com.example.kanehiro_acer.neverforget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    // Fragmentクラスの配列 完全修飾で。
    final String[] fragments ={
            "com.example.kanehiro_acer.neverforget.MysizeFragment",
            "com.example.kanehiro_acer.neverforget.PropertyFragment",
            "com.example.kanehiro_acer.neverforget.MemorialFragment",
    };
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        //mTitle = getTitle();

        // Set up the drawer.
        // 第一引数 アクティビティレイアウトのid
        // 第二引数 フラグメントUIを含むDrawerLayout
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container,Fragment.instantiate(MainActivity.this,fragments[position]))
                .commit();
    }

}
