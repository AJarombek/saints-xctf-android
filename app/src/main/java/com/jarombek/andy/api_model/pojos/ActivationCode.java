package com.jarombek.andy.api_model.pojos;

/**
 * POJO representing an activation code for a user from the REST API
 * @author Andrew Jarombek
 * @since 6/10/2017
 */

public class ActivationCode {

    private String activation_code;

    @Override
    public String toString() {
        return "ActivationCode: [ activation_code: " + activation_code  + "]";
    }

    public String getActivation_code() {
        return activation_code;
    }

    public void setActivation_code(String activation_code) {
        this.activation_code = activation_code;
    }
}

