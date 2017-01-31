package com.example.andy.saints_xctf_android;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    private static final String TAG = EditProfileFragment.class.getName();
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";
    private static final int RESULT_LOAD_IMG = 0;

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

    private Button edit_profile_mensxc;
    private Button edit_profile_wmensxc;
    private Button edit_profile_menstf;
    private Button edit_profile_wmenstf;
    private Button edit_profile_alumni;
    private boolean mensxc, wmensxc, menstf, wmenstf, alumni;

    private Button edit_profile_cancel;
    private Button edit_profile_submit;

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
        edit_profile_picture_button = (Button) v.findViewById(R.id.edit_profile_picture_button);

        edit_profile_mensxc = (Button) v.findViewById(R.id.edit_profile_mensxc);
        edit_profile_wmensxc = (Button) v.findViewById(R.id.edit_profile_wmensxc);
        edit_profile_menstf = (Button) v.findViewById(R.id.edit_profile_menstf);
        edit_profile_wmenstf = (Button) v.findViewById(R.id.edit_profile_wmenstf);
        edit_profile_alumni = (Button) v.findViewById(R.id.edit_profile_alumni);

        edit_profile_cancel = (Button) v.findViewById(R.id.edit_profile_cancel);
        edit_profile_submit = (Button) v.findViewById(R.id.edit_profile_submit);

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
            edit_profile_picture.setBackground(null);
        }

        edit_profile_mensxc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mensxc) {
                    mensxc = false;
                    edit_profile_mensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    edit_profile_mensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    if (!menstf) {
                        // Enable Women's XC
                        edit_profile_wmensxc.setClickable(true);
                        edit_profile_wmensxc.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        edit_profile_wmensxc.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Women's TF
                        edit_profile_wmenstf.setClickable(true);
                        edit_profile_wmenstf.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        edit_profile_wmenstf.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Alumni
                        edit_profile_alumni.setClickable(true);
                        edit_profile_alumni.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        edit_profile_alumni.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));
                    }
                } else {
                    mensxc = true;
                    edit_profile_mensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.stlawuRed));
                    edit_profile_mensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.white));

                    // Disable Women's XC
                    edit_profile_wmensxc.setClickable(false);
                    edit_profile_wmensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_wmensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Women's TF
                    edit_profile_wmenstf.setClickable(false);
                    edit_profile_wmenstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_wmenstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Alumni
                    edit_profile_alumni.setClickable(false);
                    edit_profile_alumni.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_alumni.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));
                }
            }
        });

        edit_profile_menstf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menstf) {
                    menstf = false;
                    edit_profile_menstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    edit_profile_menstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    if (!mensxc) {
                        // Enable Women's XC
                        edit_profile_wmensxc.setClickable(true);
                        edit_profile_wmensxc.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        edit_profile_wmensxc.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Women's TF
                        edit_profile_wmenstf.setClickable(true);
                        edit_profile_wmenstf.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        edit_profile_wmenstf.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Alumni
                        edit_profile_alumni.setClickable(true);
                        edit_profile_alumni.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        edit_profile_alumni.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));
                    }
                } else {
                    menstf = true;
                    edit_profile_menstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.stlawuRed));
                    edit_profile_menstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.white));

                    // Disable Women's XC
                    edit_profile_wmensxc.setClickable(false);
                    edit_profile_wmensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_wmensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Women's TF
                    edit_profile_wmenstf.setClickable(false);
                    edit_profile_wmenstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_wmenstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Alumni
                    edit_profile_alumni.setClickable(false);
                    edit_profile_alumni.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_alumni.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));
                }
            }
        });

        edit_profile_wmensxc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wmensxc) {
                    wmensxc = false;
                    edit_profile_wmensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    edit_profile_wmensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    if (!wmenstf) {
                        // Enable Men's XC
                        edit_profile_mensxc.setClickable(true);
                        edit_profile_mensxc.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        edit_profile_mensxc.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Men's TF
                        edit_profile_menstf.setClickable(true);
                        edit_profile_menstf.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        edit_profile_menstf.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Alumni
                        edit_profile_alumni.setClickable(true);
                        edit_profile_alumni.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        edit_profile_alumni.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));
                    }
                } else {
                    wmensxc = true;
                    edit_profile_wmensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.stlawuRed));
                    edit_profile_wmensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.white));

                    // Disable Men's XC
                    edit_profile_mensxc.setClickable(false);
                    edit_profile_mensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_mensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Men's TF
                    edit_profile_menstf.setClickable(false);
                    edit_profile_menstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_menstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Alumni
                    edit_profile_alumni.setClickable(false);
                    edit_profile_alumni.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_alumni.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));
                }
            }
        });

        edit_profile_wmenstf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wmenstf) {
                    wmenstf = false;
                    edit_profile_wmenstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    edit_profile_wmenstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    if (!wmensxc) {
                        // Enable Men's XC
                        edit_profile_mensxc.setClickable(true);
                        edit_profile_mensxc.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        edit_profile_mensxc.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Men's TF
                        edit_profile_menstf.setClickable(true);
                        edit_profile_menstf.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        edit_profile_menstf.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));

                        // Enable Alumni
                        edit_profile_alumni.setClickable(true);
                        edit_profile_alumni.setBackgroundColor(ContextCompat.getColor(
                                getContext(), R.color.lightGrey));
                        edit_profile_alumni.setTextColor(ContextCompat.getColor(
                                getContext(), R.color.black));
                    }
                } else {
                    wmenstf = true;
                    edit_profile_wmenstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.stlawuRed));
                    edit_profile_wmenstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.white));

                    // Disable Men's XC
                    edit_profile_mensxc.setClickable(false);
                    edit_profile_mensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_mensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Men's TF
                    edit_profile_menstf.setClickable(false);
                    edit_profile_menstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_menstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Alumni
                    edit_profile_alumni.setClickable(false);
                    edit_profile_alumni.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_alumni.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));
                }
            }
        });

        edit_profile_alumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alumni) {
                    alumni = false;
                    edit_profile_alumni.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    edit_profile_alumni.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    // Enable Men's XC
                    edit_profile_mensxc.setClickable(true);
                    edit_profile_mensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    edit_profile_mensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    // Enable Men's TF
                    edit_profile_menstf.setClickable(true);
                    edit_profile_menstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    edit_profile_menstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    // Enable Women's XC
                    edit_profile_wmensxc.setClickable(true);
                    edit_profile_wmensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    edit_profile_wmensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));

                    // Enable Women's TF
                    edit_profile_wmenstf.setClickable(true);
                    edit_profile_wmenstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.lightGrey));
                    edit_profile_wmenstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.black));
                } else {
                    alumni = true;
                    edit_profile_alumni.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.stlawuRed));
                    edit_profile_alumni.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.white));

                    // Disable Men's XC
                    edit_profile_mensxc.setClickable(false);
                    edit_profile_mensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_mensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Men's TF
                    edit_profile_menstf.setClickable(false);
                    edit_profile_menstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_menstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Women's XC
                    edit_profile_wmensxc.setClickable(false);
                    edit_profile_wmensxc.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_wmensxc.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));

                    // Disable Women's TF
                    edit_profile_wmenstf.setClickable(false);
                    edit_profile_wmenstf.setBackgroundColor(ContextCompat.getColor(
                            getContext(), R.color.white));
                    edit_profile_wmenstf.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.lighterGrey));
                }
            }
        });

        Map<String,String> groups = user.getGroups();
        for (Map.Entry<String,String> entry : groups.entrySet()) {
            String group = entry.getKey();
            clickGroup(group);
        }

        // Load a picture from google pictures
        edit_profile_picture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK
                && null != data) {

            Uri selectedImageUri = data.getData();
            String url = data.getData().toString();
            if (url.startsWith("content://com.google.android.apps.photos.content")){
                try {
                    InputStream is = getActivity().getContentResolver().openInputStream(selectedImageUri);
                    if (is != null) {
                        Bitmap pictureBitmap = BitmapFactory.decodeStream(is);
                        edit_profile_picture.setImageBitmap(pictureBitmap);
                        edit_profile_picture.setBackground(null);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getPath(Uri uri, Activity activity) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.MediaColumns.DATA};
            cursor = activity.getContentResolver().query(uri, projection, null, null, null);
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return "";
    }

    private void clickGroup(String group) {
        switch (group) {
            case "mensxc":
                edit_profile_mensxc.performClick();
                break;
            case "wmensxc":
                edit_profile_wmensxc.performClick();
                break;
            case "menstf":
                edit_profile_menstf.performClick();
                break;
            case "wmenstf":
                edit_profile_wmenstf.performClick();
                break;
            case "alumni":
                edit_profile_alumni.performClick();
                break;
        }
    }

}
