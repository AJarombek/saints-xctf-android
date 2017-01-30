package com.example.andy.saints_xctf_android;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.User;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    private static final String TAG = EditProfileFragment.class.getName();
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";

    private View v;
    private EditText edit_profile_first;
    private EditText edit_profile_last;
    private EditText edit_profile_email;
    private EditText edit_profile_class_year;
    private EditText edit_profile_location;
    private EditText edit_profile_favorite_event;
    private EditText edit_profile_description;
    private ImageView edit_profile_picture;
    private Button edit_profile_picture_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);

        String userJSON = prefs.getString("user", "");
        User user = null;

        try {
            user = JSONConverter.toUser(userJSON);
        } catch (IOException e) {
            Log.e(TAG, "User object JSON conversion failed.");
            Log.e(TAG, e.getMessage());
        }

        edit_profile_first = (EditText) v.findViewById(R.id.edit_profile_first);
        edit_profile_last = (EditText) v.findViewById(R.id.edit_profile_last);
        edit_profile_email = (EditText) v.findViewById(R.id.edit_profile_email);
        edit_profile_class_year = (EditText) v.findViewById(R.id.edit_profile_class_year);
        edit_profile_location = (EditText) v.findViewById(R.id.edit_profile_location);
        edit_profile_favorite_event = (EditText) v.findViewById(R.id.edit_profile_favorite_event);
        edit_profile_description = (EditText) v.findViewById(R.id.edit_profile_description);

        edit_profile_picture = (ImageView) v.findViewById(R.id.edit_profile_picture);

        edit_profile_first.setText(user.getFirst());
        edit_profile_last.setText(user.getLast());

        String email = user.getEmail();
        if (email != null)
            edit_profile_email.setText(email);

        String class_year = String.valueOf(user.getClass_year());
        if (!class_year.equals(""))
            edit_profile_class_year.setText(class_year);

        String location = user.getLocation();
        if (location != null)
            edit_profile_location.setText(location);

        String favorite_event = user.getFavorite_event();
        if (favorite_event != null)
            edit_profile_favorite_event.setText(favorite_event);

        String description = user.getDescription();
        if (description != null)
            edit_profile_description.setText(description);

        // Display the profile picture
        String base64encoding = user.getProfilepic();
        if (base64encoding != null) {
            base64encoding = base64encoding.replace("data:image/jpeg;base64,", "");

            byte[] decodedString = Base64.decode(base64encoding, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            edit_profile_picture.setImageBitmap(decodedByte);
        }

        return v;
    }

}
