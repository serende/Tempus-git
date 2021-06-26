package com.example.tempus.ui.friends;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.applandeo.Tempus.R;

public class EditFriendInfoActivity extends AppCompatActivity {

    Button finButton;
    EditText phoneNumberEditText, nameEditText, emailEditText, groupEditText, memoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend_info);

        // 전화번호 기입
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        // 지인의 이름 기입
        nameEditText = findViewById(R.id.nameEditText);
        // 지인의 이메일 기입
        emailEditText = findViewById(R.id.emailEditText);
        // 지인의 그룹 기입
        groupEditText = findViewById(R.id.groupEditText);
        // 기타사항을 메모로 기입
        memoEditText = findViewById(R.id.memoEditText);
    }
}