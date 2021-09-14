package com.example.tempus.ui.friends;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.tempus.ui.boards.WriteActivity;
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

    Intent EIFIntent;
    String user_EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entering_information_of_friend);

        EIFIntent = getIntent();
        user_EMAIL = EIFIntent.getStringExtra("EMAIL");

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

        // 연락처 연동으로 지인 정보를 받아올 경우 TextView에 setText
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
            Log.e("CPERROR", exceptionAsStrting);

            e.printStackTrace();
        }

        // 완료 버튼을 누르면 기입된 지인의 정보를 파일로 저장함
        // 각 정보는 '|'으로 구분하고, 지인의 정보는 \n으로 구분함
        finButton = findViewById(R.id.finButton);
        finButton.setOnClickListener(v -> {
            try{
                // 입력받은 지인 정보를 String형으로 저장
                String phoneTxt = phoneNumberEditText.getText().toString().trim();
                String nameTxt = nameEditText.getText().toString().trim();
                String emailTxt = emailEditText.getText().toString().trim();
                String groupTxt = groupEditText.getText().toString().trim();
                String memoTxt = memoEditText.getText().toString();

                // 지인 정보를 하나의 변수에 저장
                String friendInfoTxt = phoneTxt + "|" + nameTxt + "|" + emailTxt + "|" + groupTxt + "|" + memoTxt + "|" + "\n";

                // 지인 정보를 입력할 파일 열기
                FileOutputStream outstream = openFileOutput("friendList.txt", Context.MODE_APPEND);

                // 파일에 지인 정보 입력
                outstream.write(friendInfoTxt.getBytes());

                outstream.close();

                // 지인 목록 페이지로 이동
                Intent intent = new Intent(v.getContext(), FriendListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // 상위 스택 액티비티 모두 제거
                intent.putExtra("EMAIL", user_EMAIL);
                EnteringInformationOfFriendActivity.this.finish();
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
