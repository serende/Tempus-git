package com.example.tempus.ui.friends;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.Tempus.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ConfirmFriendInfoActivity extends AppCompatActivity {
    final static String FilePath= "/data/data/com.applandeo.materialcalendarsampleapp/files/friendList.txt";

    TextView phoneNumberTextView;
    TextView nameTextView;
    TextView emailTextView;
    TextView groupTextView;
    TextView memoTextView;

    Button finButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_friend_info);

        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        groupTextView = findViewById(R.id.groupTextView);
        memoTextView = findViewById(R.id.memoTextView);

        SetText();

        finButton = findViewById(R.id.finButton);
        finButton.setOnClickListener(v -> {
            /*
            1. 현재 페이지에서는 수정 페이지로 이동하는 것만 구현(intent할 때 n으로 지인 번호도 다시 전달해야 함)
                - 수정 페이지 따로 구현 필요
            2. 수정 페이지에서는 ReadFile을 통해 텍스트를 전부 가져온 뒤
            3. 아래 코드를 이용해서 textView가 아닌 EditText에 setText를 해준 뒤
            4. editText에 입력된 값을 지인정보 입력 페이지에서 했던 것처럼 다시 저장
            if (readArr != null)
        {
            int nCnt = readArr.length;

            // readArr[0+5n]: 전화번호, readArr[1+5n]: 등록명, readArr[2+5n]: 이메일, readArr[3+5n]: 그룹명, readArr[4+5n]: 메모
            for (int i=0; i<nCnt; ++i)
            {
                Log.i("ARRTAG", "arr[" + i + "] = " + readArr[i]);
            }

            // intent하면서 전달받은 값을 가져와서 지인 번호로 사용
            Intent friendIntent = getIntent();
            Integer n = friendIntent.getIntExtra("지인 번호", -1);
            Log.v("friendNum", "전달된 지인 번호 : " + n);

            // TextView에 setText
            phoneNumberTextView.setText(readArr[0+5*n]);
            nameTextView.setText(readArr[1+5*n]);
            emailTextView.setText(readArr[2+5*n]);
            groupTextView.setText(readArr[3+5*n]);
            memoTextView.setText(readArr[4+5*n]);

            Intent friendIntent = getIntent();
            Integer n = friendIntent.getIntExtra("지인 번호", -1);
            Log.v("friendNum", "전달된 지인 번호 : " + n);
        }
        else{
            Toast.makeText(this.getApplicationContext(), "추가된 지인이 없습니다.", Toast.LENGTH_SHORT).show();
        }
             */
        });
    }
    // 파일에서 텍스트를 읽어 옴
    public String ReadFile (String path){
        StringBuffer strBuffer = new StringBuffer();
        try {
            InputStream is = new FileInputStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = "";

            while ((line = reader.readLine()) != null) {
                strBuffer.append(line+"\n");
            }
            reader.close();
            is.close();
        }
        catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("FilereadCFI", exceptionAsStrting);

            e.printStackTrace();
            Toast.makeText(this.getApplicationContext(), "파일을 읽는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            return "";
        }
        return strBuffer.toString();
    }

    public void SetText (){
        // 파일에서 지인 정보 가져옴
        String read = ReadFile(FilePath);

        // '-'를 기준으로 지인 정보 분류
        String[] readArr = read.split("\\|");

        if (readArr != null)
        {
            int nCnt = readArr.length;

            // readArr[0+5n]: 전화번호, readArr[1+5n]: 등록명, readArr[2+5n]: 이메일, readArr[3+5n]: 그룹명, readArr[4+5n]: 메모
            for (int i=0; i<nCnt; ++i)
            {
                Log.i("ARRTAG", "arr[" + i + "] = " + readArr[i]);
            }

            // intent하면서 전달받은 값을 가져와서 지인 번호로 사용
            Intent friendIntent = getIntent();
            Integer n = friendIntent.getIntExtra("지인 번호", -1);
            Log.v("friendNum", "전달된 지인 번호 : " + n);

            // TextView에 setText
            phoneNumberTextView.setText(readArr[0+5*n]);
            nameTextView.setText(readArr[1+5*n]);
            emailTextView.setText(readArr[2+5*n]);
            groupTextView.setText(readArr[3+5*n]);
            memoTextView.setText(readArr[4+5*n]);
        }
        else{
            Toast.makeText(this.getApplicationContext(), "추가된 지인이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}