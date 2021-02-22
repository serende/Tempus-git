package com.example.tempus.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tempus.MainActivity;
import com.example.tempus.R;
import com.example.tempus.SignupActivity;
import com.example.tempus.ui.login.LoginViewModel;
import com.example.tempus.ui.login.LoginViewModelFactory;

public class LoginActivity extends Activity {

    // UI references.

    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.

        mEmailView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button loginInButton = (Button) findViewById(R.id.login); // login button

        Button SignuppButton = (Button) findViewById(R.id.sign_up); // sign up button


        loginInButton.setOnClickListener(v -> {
            //서버 구축시 데이터 넘김


        });

        SignuppButton.setOnClickListener(v -> {
            //회원 가입 페이지로 이동
            Intent Signintent = new Intent(this, SignupActivity.class);//임시 페이지
            startActivity(Signintent);
        });


        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if(id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL){
                    return true;
                }
                return false;
            }

        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    //attemptLogin();

                    return true;
                }
                return false;
            }
        });





    }

}