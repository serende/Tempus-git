package com.example.tempus.ui.friends;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

import com.applandeo.Tempus.R;

public class AddFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        Button registerWithPhoneNumberButton = (Button) findViewById(R.id.registerWithPhoneNumberButton);
        registerWithPhoneNumberButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), EnteringInformationOfFriendActivity.class);
                startActivity(intent);
            }
        });

        Button registerWithAddressBookButton = (Button) findViewById(R.id.registerWithAddressBookButton);
        registerWithAddressBookButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // 연락처와 연동
            }
        });

        Button registerWithKakaoButton = (Button) findViewById(R.id.registerWithKakaoButton);
        registerWithKakaoButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // 카카오톡과 연동
            }
        });
    }
}