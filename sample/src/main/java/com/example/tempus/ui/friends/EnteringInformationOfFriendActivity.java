package com.example.tempus.ui.friends;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.applandeo.Tempus.R;

import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;

public class EnteringInformationOfFriendActivity extends AppCompatActivity {

    Button finButton;
    EditText phoneNumberEditText, nameEditText, emailEditText, groupEditText, memoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entering_information_of_friend);

        /*
        // 파일 읽기 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }

         */

        // 완료 버튼을 누르면 기입된 지인의 정보를 저장하여 FriendList 액티비티에 전달하여 기입된 정보를 정렬시켜야 함
        finButton = (Button) findViewById(R.id.finButton);
        // finButton.setOnClickListener((View.OnClickListener) this);

        finButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // 완료 버튼을 누르면 지인 목록 페이지로 이동
                Intent intent = new Intent(v.getContext(), FriendListActivity.class);
                startActivity(intent);
            }
        });



        // 전화번호 기입
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);

        // 지인의 이름 기입
        nameEditText = (EditText) findViewById(R.id.nameEditText);

        // 지인의 이메일 기입
        emailEditText = (EditText) findViewById(R.id.emailEditText);

        // 지인의 그룹 기입
        groupEditText = (EditText) findViewById(R.id.groupEditText);

        // 기타사항을 메모로 기입
        memoEditText = (EditText) findViewById(R.id.memoEditText);
    }


/*
    public void onClick(@NotNull View v){
        switch(v.getId()){
            case R.id.finButton:
                try{
                    // 입력받은 지인 정보를 String형으로 저장
                    String phoneTxt = phoneNumberEditText.getText().toString();
                    String nameTxt = nameEditText.getText().toString();
                    String emailTxt = emailEditText.getText().toString();
                    String groupTxt = groupEditText.getText().toString();
                    String memoTxt = memoEditText.getText().toString();

                    String friendInfoTxt = phoneTxt + "-" + nameTxt + "-" + emailTxt + "-" + groupTxt + "-" + memoTxt;

                    FileOutputStream outstream = openFileOutput("friendList.txt", Activity.MODE_WORLD_WRITEABLE);

                    outstream.write(friendInfoTxt.getBytes());

                    Toast.makeText(this, "지인 정보를 저장하는데 성공했습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(v.getContext(), FriendListActivity.class);
                    startActivity(intent);
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "지인 정보를 저장하는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
        }
    }

 */



}