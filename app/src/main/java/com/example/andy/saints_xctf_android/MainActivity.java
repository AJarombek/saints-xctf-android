package com.example.andy.saints_xctf_android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity: ";

    public static final String COLOR_TERRIBLE = "#64ea9999";
    public static final String COLOR_VERYBAD = "#64ffad99";
    public static final String COLOR_BAD = "#64eac199";
    public static final String COLOR_PRETTYBAD = "#64ffd699";
    public static final String COLOR_MEDIOCRE = "#64ffffad";
    public static final String COLOR_AVERAGE = "#64e3e3e3";
    public static final String COLOR_FAIRLYGOOD = "#64c7f599";
    public static final String COLOR_GOOD = "#6499d699";
    public static final String COLOR_GREAT = "#6499c199";
    public static final String COLOR_FANTASTIC = "#64a3a3ff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Inside onCreate.");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_main);

        // If the user is signed in, forward them to the main page
        if (savedInstanceState != null) {
            if (fragment == null) {
                fragment = new MainFragment();
                fm.beginTransaction().
                        add(R.id.activity_main, fragment).
                        commit();
            }
        } else {
            // Otherwise they need to sign in or sign up
            if (fragment == null) {
                fragment = new HomeFragment();
                fm.beginTransaction().
                        add(R.id.activity_main, fragment).
                        commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.signed_in_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Change fragments from the MainFragment to the HomeFragment
     */
    protected void signIn() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new MainFragment();
        fm.beginTransaction().
                replace(R.id.activity_main, fragment).
                commit();
    }

    protected void noInternet() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new NoInternetFragment();
        fm.beginTransaction().
                replace(R.id.activity_main, fragment).
                addToBackStack(null).
                commit();
    }
}
