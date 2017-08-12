package com.jarombek.andy.api_model.pojos;
/**
 * POJO representing an email to be sent from the REST API
 * @author Andrew Jarombek
 * @since 7/1/2017
 */

public class Mail {

    private String emailAddress;
    private String subject;
    private String body;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
