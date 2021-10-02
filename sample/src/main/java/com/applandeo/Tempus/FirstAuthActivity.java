package com.applandeo.Tempus;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.tempus.ui.boards.BoardMainActivity;

public class FirstAuthActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_first_auth);

        if(SaveSharedPreference.getUserName().length() == 0) {
            // call Login Activity
            intent = new Intent(FirstAuthActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            // Call BoardMainActivity
            intent = new Intent(FirstAuthActivity.this, BoardMainActivity.class);
            intent.putExtra("EMAIL", SaveSharedPreference.getUserName());
            startActivity(intent);
            this.finish();
        }
    }
}