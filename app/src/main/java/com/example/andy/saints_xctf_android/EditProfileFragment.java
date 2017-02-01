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
import android.os.AsyncTask;
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
import android.widget.TextView;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    private static final String TAG = EditProfileFragment.class.getName();
    public static final String PREFS_NAME = "SaintsxctfUserPrefs";
    private static final int RESULT_LOAD_IMG = 0;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static final String NAME_REGEX = "^[a-zA-Z\\-']+$";
    private static final String YEAR_REGEX = "^[0-9]+$";

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
    private Bitmap profile_picture_bitmap;

    private Button edit_profile_mensxc;
    private Button edit_profile_wmensxc;
    private Button edit_profile_menstf;
    private Button edit_profile_wmenstf;
    private Button edit_profile_alumni;
    private boolean mensxc, wmensxc, menstf, wmenstf, alumni;

    private TextView edit_profile_error_message;
    private Button edit_profile_cancel;
    private Button edit_profile_submit;
    private User user;
    private String username, first, last, year, email, location, favorite_event, description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);

        String userJSON = prefs.getString("user", "");
        user = null;

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

        edit_profile_error_message = (TextView) v.findViewById(R.id.edit_profile_error_message);
        edit_profile_cancel = (Button) v.findViewById(R.id.edit_profile_cancel);
        edit_profile_submit = (Button) v.findViewById(R.id.edit_profile_submit);

        edit_profile_first.setText(user.getFirst());
        edit_profile_last.setText(user.getLast());

        username = user.getUsername();

        String email = user.getEmail();
        if (email != null)
            edit_profile_email.setText(email);

        String class_year = String.valueOf(user.getClass_year());
        if (!class_year.equals("null"))
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
            profile_picture_bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            edit_profile_picture.setImageBitmap(profile_picture_bitmap);
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

        edit_profile_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        edit_profile_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    updateUser();

                    String userString = "";
                    try {
                        userString = JSONConverter.fromUser(user);
                    } catch (Throwable throwable) {
                        Log.e(TAG, "User object JSON conversion failed.");
                        Log.e(TAG, throwable.getMessage());
                        edit_profile_error_message.setText(R.string.internal_error);
                        return;
                    }

                    UpdateUserTask updateUserTask = new UpdateUserTask();
                    updateUserTask.execute(username, userString);
                }
            }
        });

        return v;
    }

    private boolean validateFields() {
        first = edit_profile_first.getText().toString();
        last = edit_profile_last.getText().toString();
        email = edit_profile_email.getText().toString();
        year = edit_profile_class_year.getText().toString();

        Pattern email_pattern = Pattern.compile(EMAIL_REGEX);
        Matcher email_matcher = email_pattern.matcher(email);
        Pattern name_pattern = Pattern.compile(NAME_REGEX);
        Matcher first_matcher = name_pattern.matcher(first);
        Matcher last_matcher = name_pattern.matcher(last);
        Pattern year_pattern = Pattern.compile(YEAR_REGEX);
        Matcher year_matcher = year_pattern.matcher(year);

        if (!first_matcher.matches()) {
            edit_profile_error_message.setText(R.string.invalid_first_name);
            edit_profile_first.requestFocus();
            return false;
        }
        if (!last_matcher.matches()) {
            edit_profile_error_message.setText(R.string.invalid_last_name);
            edit_profile_last.requestFocus();
            return false;
        }
        if (!email.equals("") && !email_matcher.matches()) {
            edit_profile_error_message.setText(R.string.invalid_email);
            edit_profile_email.requestFocus();
            return false;
        }
        if (!year.equals("") && !year_matcher.matches()) {
            edit_profile_error_message.setText(R.string.invalid_class_year);
            edit_profile_class_year.requestFocus();
            return false;
        }
        return true;
    }

    private void updateUser() {
        year = edit_profile_class_year.getText().toString();
        location = edit_profile_location.getText().toString();
        favorite_event = edit_profile_favorite_event.getText().toString();
        description = edit_profile_description.getText().toString();

        user.setFirst(first);
        user.setLast(last);
        if (email.equals(""))
            user.setEmail(null);
        else
            user.setEmail(email);

        if (year.equals(""))
            user.setClass_year(null);
        else
            user.setClass_year(Integer.parseInt(year));

        if (location.equals(""))
            user.setLocation(null);
        else
            user.setLocation(location);

        if (favorite_event.equals(""))
            user.setFavorite_event(null);
        else
            user.setFavorite_event(favorite_event);

        if (description.equals(""))
            user.setDescription(null);
        else
            user.setDescription(description);

        // convert from bitmap to base 64 encoded string
        if (profile_picture_bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            profile_picture_bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            user.setProfilepic(encoded);
        }

        Map<String,String> groups = new HashMap<>();
        if (mensxc)
            groups.put("mensxc", "Men's Cross Country");
        if (wmensxc)
            groups.put("wmensxc", "Women's Cross Country");
        if (menstf)
            groups.put("menstf", "Men's Track & Field");
        if (wmenstf)
            groups.put("wmenstf", "Women's Track & Field");
        if (alumni)
            groups.put("alumni", "Alumni");
        user.setGroups(groups);
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
                        profile_picture_bitmap = BitmapFactory.decodeStream(is);
                        edit_profile_picture.setImageBitmap(profile_picture_bitmap);
                        edit_profile_picture.setBackground(null);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
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

    class UpdateUserTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... strings) {
            User user = null;
            try {
                user = APIClient.userPutRequest(strings[0], strings[1]);

                if (user == null)
                    return "no_internet";

            } catch (Exception e) {
                Log.e(TAG, "User object server JSON conversion failed.");
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
                return "no_internet";
            }

            return user;
        }

        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);

            if (response != null) {
                if (response.equals("no_internet")) {
                    ((MainActivity) getActivity()).noInternet();
                } else if (response instanceof User) {
                    user = (User) response;

                    String userString = "";
                    try {
                        userString = JSONConverter.fromUser(user);
                    } catch (Throwable throwable) {
                        Log.e(TAG, "User object received JSON conversion failed.");
                        Log.e(TAG, throwable.getMessage());
                    }

                    SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("user", userString);
                    editor.putString("first", user.getFirst());
                    editor.putString("last", user.getLast());
                    editor.apply();

                    getFragmentManager().popBackStackImmediate();
                }
            }
        }
    }

}
