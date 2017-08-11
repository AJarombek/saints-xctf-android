package com.example.andy.saints_xctf_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.andy.saints_xctf_android.group.GroupFragment;
import com.example.andy.saints_xctf_android.home.HomeFragment;
import com.example.andy.saints_xctf_android.home.PickGroupFragment;
import com.example.andy.saints_xctf_android.main.MainFragment;
import com.example.andy.saints_xctf_android.profile.EditProfileFragment;
import com.example.andy.saints_xctf_android.profile.ProfileFragment;
import com.example.andy.saints_xctf_android.shared.NoInternetFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity: ";
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";

    public static final String COLOR_TERRIBLE = "#ea9999";
    public static final String COLOR_VERYBAD = "#ffad99";
    public static final String COLOR_BAD = "#eac199";
    public static final String COLOR_PRETTYBAD = "#ffd699";
    public static final String COLOR_MEDIOCRE = "#ffffad";
    public static final String COLOR_AVERAGE = "#e3e3e3";
    public static final String COLOR_FAIRLYGOOD = "#c7f599";
    public static final String COLOR_GOOD = "#99d699";
    public static final String COLOR_GREAT = "#99c199";
    public static final String COLOR_FANTASTIC = "#a3a3ff";
    public static final String COLOR_DEFAULT = "#eeeeee";
    public static final String COLOR_DEFAULT_2 = "#ffffff";
    public static final String DESCRIPT_TERRIBLE = "Terrible";
    public static final String DESCRIPT_VERYBAD = "Very Bad";
    public static final String DESCRIPT_BAD = "Bad";
    public static final String DESCRIPT_PRETTYBAD = "Pretty Bad";
    public static final String DESCRIPT_MEDIOCRE = "Mediocre";
    public static final String DESCRIPT_AVERAGE = "Average";
    public static final String DESCRIPT_FAIRLYGOOD = "Fairly Good";
    public static final String DESCRIPT_GOOD = "Good";
    public static final String DESCRIPT_GREAT = "Great";
    public static final String DESCRIPT_FANTASTIC = "Fantastic";

    public static final String MILES_PAST_WEEK = "milespastweek";
    public static final String MILES_PAST_MONTH = "milespastmonth";
    public static final String MILES_PAST_YEAR = "milespastyear";
    public static final String MILES_ALL_TIME = "miles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Inside onCreate.");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_main);

        SharedPreferences prefs = this.getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);

        // If the user is signed in, forward them to the main page
        if (prefs.contains("username")) {
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
    public void signIn() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new MainFragment();
        fm.beginTransaction().
                replace(R.id.activity_main, fragment).
                commit();
    }

    public void signUp() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new PickGroupFragment();
        fm.beginTransaction().
                replace(R.id.activity_main, fragment).
                commit();
    }

    public void viewMainPage() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new MainFragment();
        fm.beginTransaction().
                replace(R.id.activity_main, fragment).
                addToBackStack(null).
                commit();
    }

    public void signOut() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Fragment fragment = new HomeFragment();
        fm.beginTransaction().
                replace(R.id.activity_main, fragment).
                commit();
    }

    public void noInternet() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new NoInternetFragment();
        fm.beginTransaction().
                replace(R.id.activity_main, fragment).
                addToBackStack(null).
                commit();
    }

    public void viewProfile(String username) {
        Bundle data = new Bundle();
        data.putString("username", username);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new ProfileFragment();
        fragment.setArguments(data);
        fm.beginTransaction().
                replace(R.id.activity_main, fragment).
                addToBackStack(null).
                commit();
    }

    public void viewGroup(String group) {
        Bundle data = new Bundle();
        data.putString("group", group);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new GroupFragment();
        fragment.setArguments(data);
        fm.beginTransaction().
                replace(R.id.activity_main, fragment).
                addToBackStack(null).
                commit();
    }

    public void editProfile() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new EditProfileFragment();
        fm.beginTransaction().
                replace(R.id.activity_main, fragment).
                addToBackStack(null).
                commit();
    }
}
