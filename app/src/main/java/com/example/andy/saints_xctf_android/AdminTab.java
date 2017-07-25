package com.example.andy.saints_xctf_android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.andy.api_model.APIClient;
import com.example.andy.api_model.ActivationCode;
import com.example.andy.api_model.Group;
import com.example.andy.api_model.GroupMember;
import com.example.andy.api_model.JSONConverter;
import com.example.andy.api_model.Mail;
import com.example.andy.api_model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * @author Andrew Jarombek
 * @since 7/17/2017 -
 */
public class AdminTab extends Fragment {

    private static final String LOG_TAG = AdminTab.class.getName();
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    private View v;
    private Group group;
    private Spinner group_users_dropdown;
    private EditText email_request_input, flair_input, notification_input;
    private Button email_request_send, flair_submit, notification_submit;
    private ArrayList<String> member_names;
    private HashMap<String, String> member_map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_tab, container, false);
        v = view;

        group_users_dropdown = (Spinner) v.findViewById(R.id.group_users_dropdown);
        email_request_input = (EditText) v.findViewById(R.id.email_request_input);
        flair_input = (EditText) v.findViewById(R.id.flair_input);
        notification_input = (EditText) v.findViewById(R.id.notification_input);
        email_request_send = (Button) v.findViewById(R.id.email_request_send);
        flair_submit = (Button) v.findViewById(R.id.flair_submit);
        notification_submit = (Button) v.findViewById(R.id.notification_submit);

        Bundle bundle = getArguments();
        String groupString = bundle.getString("group", "");

        try {
            group = JSONConverter.toGroup(groupString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        member_names = new ArrayList<>();
        member_map = new HashMap<>();
        ArrayList<GroupMember> groupMembers = group.getMembers();

        for (GroupMember member : groupMembers) {
            String name = member.getFirst() + " " + member.getLast();
            member_names.add(name);
            member_map.put(name, member.getUsername());
        }

        // Populate the spinner with all the group members names
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, member_names);

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        group_users_dropdown.setAdapter(typeAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        email_request_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = email_request_input.getText().toString();

                Pattern pattern = Pattern.compile(EMAIL_REGEX);
                Matcher matcher = pattern.matcher(email);

                // Validate the Email Input
                if (email.length() == 0) {
                    Toast.makeText(getContext(), "Must Enter Email", Toast.LENGTH_SHORT).show();
                    email_request_input.requestFocus();
                } else if (!matcher.matches()) {
                    Toast.makeText(getContext(), "Invalid Email Entered", Toast.LENGTH_SHORT).show();
                    email_request_input.requestFocus();
                } else {
                    // Valid Email
                    EmailTask emailTask = new EmailTask();
                    emailTask.execute(email);
                }
            }
        });
    }

    class EmailTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                ActivationCode activationCode = APIClient.activationcodePostRequest("");

                if (activationCode == null) {
                    return "Email Failed";
                }

                // Build the Mail Object
                Mail mail = new Mail();
                mail.setEmailAddress(params[0]);
                mail.setSubject("Saintsxctf.com Forgot Password");
                mail.setBody("<html>" +
                        "<head>" +
                        "<title>HTML email</title>" +
                        "</head>" +
                        "<body>" +
                        "<h3>SaintsXCTF.com Invite</h3>" +
                        "<br><p>You Have Been Invited to SaintsXCTF.com!</p>" +
                        "<br><br><p>Use the following confirmation code to sign up:</p><br>" +
                        "<p><b>Code: </b>" + activationCode.getActivation_code() + "</p>" +
                        "</body>" +
                        "</html>");

                // Convert the mail object to JSON
                String mailString;
                try {
                    mailString = JSONConverter.fromMail(mail);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    return "Email Failed";
                }

                // Send the Mail
                APIClient.mailPostRequest(mailString);

                return "Email Sent!";

            } catch (IOException e) {
                e.printStackTrace();
                return "Email Failed";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            email_request_send.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            // display error/success toast
            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
            email_request_send.setEnabled(true);
            email_request_input.setText("");
        }
    }
}

