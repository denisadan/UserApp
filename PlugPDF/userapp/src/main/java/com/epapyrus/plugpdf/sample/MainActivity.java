package com.epapyrus.plugpdf.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


/**
 * A login screen that offers login via email/password.
 */
public class MainActivity extends Activity {

    private TextView userField;
    private TextView passField;
    private TextView userErrorTextField;
    private TextView passErrorTextField;
    private TextView succesLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userField = (TextView) findViewById(R.id.userTextField);
        passField = (TextView) findViewById(R.id.passTextField);
        userErrorTextField = (TextView) findViewById(R.id.userErrorTextField);
        passErrorTextField = (TextView) findViewById(R.id.passErrorTextField);
        succesLogin = (TextView) findViewById(R.id.succesId);

    }

    public void loginClicked(View view) {

        boolean userOk = false, passOk = false;

        if (!verifyNullUserField(userField.getText().toString())) {
            userErrorTextField.setText("Username cannot be null");

        } else if (!verifylenghtUserField(userField.getText().toString())) {
            userErrorTextField.setText("Username  too short");

        } else if (verifyUser(userField.getText().toString())) {
            userErrorTextField.setText("");
            userOk = true;
        } else {
            userOk = false;
            userErrorTextField.setText("invalid");
        }


        if (!verifyNullPassField(passField.getText().toString())) {
            passErrorTextField.setText("Password cannot be null");

        } else if (!verifyLengthPassField(passField.getText().toString())) {
            passErrorTextField.setText("Pasword too short");

        } else if (verifyPass(passField.getText().toString())) {
            passErrorTextField.setText("");
            passOk = true;
        } else {
            passOk = false;
            passErrorTextField.setText("invalid");
        }

        if (passOk && userOk) {
            succesLogin.setText("Succes Login");
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }

    }

    public boolean verifyNullUserField(String username) {
        return username.length() != 0;
    }

    public boolean verifyNullPassField(String pass) {
        return pass.length() != 0;
    }

    public boolean verifylenghtUserField(String username) {
        return username.length() >= 4;
    }

    public boolean verifyLengthPassField(String pass) {
        return pass.length() >= 4;
    }

    public boolean verifyUser(String username) {

        return username.equals("1234");
    }

    public boolean verifyPass(String pass) {

        return pass.equals("1234");
    }
}



