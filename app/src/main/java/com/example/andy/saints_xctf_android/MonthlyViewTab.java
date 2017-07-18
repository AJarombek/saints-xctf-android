package com.example.andy.saints_xctf_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/17/2017 -
 */
public class MonthlyViewTab extends Fragment {

    private View v;
    private String username;
    private WebView monthly_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.monthly_view_tab, container, false);
        v = view;

        Bundle bundle = getArguments();
        username = bundle.getString("username", "");

        monthly_view = (WebView) v.findViewById(R.id.monthly_web_view);
        monthly_view.loadUrl("https://www.saintsxctf.com/profile.php?user=" + username);
        WebSettings webSettings = monthly_view.getSettings();
        //webSettings.setJavaScriptEnabled(true);

        return view;
    }
}
