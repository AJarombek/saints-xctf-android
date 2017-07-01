package com.example.andy.api_model;

/**
 * Utility Functions for Sending Email
 * http://www.jondev.net/articles/Sending_Emails_without_User_Intervention_(no_Intents)_in_Android
 * @author Andrew Jarombek
 * @since 7/1/2017
 */

public class MailUtils {

    private static final String LOG_TAG = MailUtils.class.getName();

    public static String sendForgotPasswordEmail(String email, String username) {

        Mail mail = new Mail("admin@saintsxctf.com", MailCred.getSMTPPassword());

        String[] toArray = {email};
        mail.setTo(toArray);
        mail.setFrom("admin@saintsxctf.com");
        mail.setSubject("Saintsxctf.com Forgot Password");
        mail.setBody("Testing 123 " + username);
        try {
            mail.send();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
