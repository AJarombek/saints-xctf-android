package com.jarombek.andy.saints_xctf_android.shared;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jarombek.andy.saints_xctf_android.R;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 1/20/2017 -
 */
public class NoInternetFragment extends Fragment {

    private View v;
    private ImageButton refresh_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_no_internet, container, false);
        this.v = view;

        refresh_button = (ImageButton) v.findViewById(R.id.refresh_button);

        // When the refresh button is clicked, just go back to previous fragment
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        return v;
    }

}
