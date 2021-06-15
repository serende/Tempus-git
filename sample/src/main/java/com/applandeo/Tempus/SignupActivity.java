package com.applandeo.Tempus;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    private EditText mNameView;
    private EditText mPhoneNumberView;
    private EditText mAddressView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPassword2View;
    String name,pnum,address,email,password,password2;
    String userjson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mNameView = (EditText) findViewById(R.id.signup_name);
        mPhoneNumberView = (EditText) findViewById(R.id.signup_phonenumber);
        mAddressView = (EditText) findViewById(R.id.signup_office);
        mEmailView = (EditText) findViewById(R.id.signup_email);
        mPasswordView = (EditText) findViewById(R.id.signup_password);
        mPassword2View = (EditText) findViewById(R.id.signup_password2);

        Button SignupButton = (Button) findViewById(R.id.signup_button); // signup button
        SignupButton.setOnClickListener(v ->{
            name = mNameView.getText().toString();
            pnum = mPhoneNumberView.getText().toString();
            address = mAddressView.getText().toString();
            email = mEmailView.getText().toString();
            password = mPasswordView.getText().toString();
            password2 = mPassword2View.getText().toString();

            



        });

    }
}