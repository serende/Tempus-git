package com.example.tempus.ui.friends;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.applandeo.Tempus.R;

import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;


public class EnteringInformationOfFriendActivity extends AppCompatActivity {

    Button finButton;
    EditText phoneNumberEditText, nameEditText, emailEditText, groupEditText, memoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entering_information_of_friend);

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

        // 현재 name은 정상적으로 setText되지만 number가 Textview로 전달되지 않음
        // Log도 찍히지 않음
        // phoneNumber로 따로 빼는게 아니라 바로 setText에 넣는 방식도 해봐야 함
        Intent autoIntent = getIntent();
        phoneNumberEditText.setText(autoIntent.getStringExtra("전화번호"));
        nameEditText.setText(autoIntent.getStringExtra("지인명"));

        Log.i("textInfo", "지인명: " + autoIntent.getStringExtra("지인명") +", 전화번호: " + autoIntent.getStringExtra("전화번호"));

        try{
            checkPermission();
        }
        catch (Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("CSP", exceptionAsStrting);

            e.printStackTrace();
        }

        // 완료 버튼을 누르면 기입된 지인의 정보를 파일로 저장함
        // 각 정보는 '-'으로 구분하고, 지인의 정보는 \n으로 구분함
        finButton = (Button) findViewById(R.id.finButton);
        finButton.setOnClickListener(v -> {
            try{
                // 입력받은 지인 정보를 String형으로 저장
                String phoneTxt = phoneNumberEditText.getText().toString().trim();
                String nameTxt = nameEditText.getText().toString().trim();
                String emailTxt = emailEditText.getText().toString().trim();
                String groupTxt = groupEditText.getText().toString().trim();
                String memoTxt = memoEditText.getText().toString();

                // 지인 정보를 하나의 변수에 저장
                String friendInfoTxt = phoneTxt + "-" + nameTxt + "-" + emailTxt + "-" + groupTxt + "-" + memoTxt + "\n" + "-";

                // 지인 정보를 입력할 파일 열기
                FileOutputStream outstream = openFileOutput("friendList.txt", Context.MODE_APPEND);

                // 파일에 지인 정보 입력
                outstream.write(friendInfoTxt.getBytes());

                outstream.close();

                Intent intent = new Intent(v.getContext(), FriendListActivity.class);
                startActivity(intent);
            } catch (Exception e){
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsStrting = sw.toString();
                Log.e("Filesave", exceptionAsStrting);

                e.printStackTrace();
                Toast.makeText(this.getApplicationContext(), "저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkPermission(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

}
