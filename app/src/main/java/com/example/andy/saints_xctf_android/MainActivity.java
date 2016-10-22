package com.example.andy.saints_xctf_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Android Activity method for taking an action when an item is clicked in the
     * app bar
     * @param item menu item selected
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log:
                return true;
            case R.id.action_home:
                return true;
            case R.id.action_profile:
                return true;
            case R.id.action_group:
                return true;
            case R.id.action_settings:
                return true;
            default:
                // The user's action was not recognized, invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
