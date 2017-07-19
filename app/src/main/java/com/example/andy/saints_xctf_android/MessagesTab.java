package com.example.andy.saints_xctf_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/18/2017 -
 */
public class MessagesTab extends Fragment {

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.monthly_view_tab, container, false);
        v = view;

        // Bundle bundle = getArguments();
        // username = bundle.getString("username", "");

        // monthly_view = (WebView) v.findViewById(R.id.monthly_web_view);

        return view;
    }
}
