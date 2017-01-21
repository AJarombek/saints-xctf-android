package com.example.andy.saints_xctf_android;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 11/7/2016 -
 */
public class HomeFragment extends Fragment {

    private View v;
    private Button login_button, signup_button;
    private static final String TAG = "HomeFragment: ";
    private boolean dialogOnScreen;

    /**
     * Android onCreateView method
     * @param inflater --
     * @param container --
     * @param savedInstanceState --
     * @return fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.home, container, false);
        Log.d(TAG, "Inside onCreateView.");

        setHasOptionsMenu(false);
        this.v = view;

        login_button = (Button) v.findViewById(R.id.login_button);
        signup_button = (Button) v.findViewById(R.id.signup_button);

        // Click listener for the login button
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialogFragment loginDialog = new LoginDialogFragment();
                loginDialog.show(getFragmentManager(), "login dialog");
            }
        });

        // Click listener for the signup button
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupDialogFragment signupDialog = new SignupDialogFragment();
                signupDialog.show(getFragmentManager(), "signup dialog");
            }
        });

        return v;
    }

    /**
     * Android onCreate method
     * @param savedInstanceState --
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Inside onCreate.");
        // Save on rotation
        setRetainInstance(true);
    }

    // indicates whether a dialog is displayed
    public void setDialogOnScreen(boolean visible) {
        dialogOnScreen = visible;
    }
}
