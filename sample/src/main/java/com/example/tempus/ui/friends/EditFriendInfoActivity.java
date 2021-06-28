package com.example.tempus.ui.friends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.applandeo.Tempus.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

public class EditFriendInfoActivity extends AppCompatActivity {
    // friendlist.txt 파일 경로, AVD와 실제 스마트폰 모두 동일한 경로 사용
    final static String FilePath= "/data/data/com.applandeo.materialcalendarsampleapp/files/friendList.txt";

    Button finButton;
    EditText phoneNumberEditText, nameEditText, emailEditText, groupEditText, memoEditText;

    Integer n;

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

        // 수정할 지인의 정보들을 EditText에 SetText
        SetText();

        finButton = findViewById(R.id.finButton);
        finButton.setOnClickListener(v -> {
            // 입력받은 지인 정보를 String형으로 저장
            String phoneTxt = phoneNumberEditText.getText().toString().trim();
            String nameTxt = nameEditText.getText().toString().trim();
            String emailTxt = emailEditText.getText().toString().trim();
            String groupTxt = groupEditText.getText().toString().trim();
            String memoTxt = memoEditText.getText().toString();

            // 지인 정보를 하나의 변수에 저장
            String friendInfoTxt = phoneTxt + "|" + nameTxt + "|" + emailTxt + "|" + groupTxt + "|" + memoTxt + "|";

            /*
            ReadFile로 읽어온 정보 중 일부를 빼고 그 부분에 수정된 값을 집어넣고 그대로 덮어쓰기?
            for루프로 돌다가 n번째 줄에 이르면 그걸 바꾸는 방식
             */

            // friendlist.txt에 원래 들어있던 텍스트를 전부 가져와서 frBuffer에 담음
            StringBuffer frBuffer = new StringBuffer();
            Integer countN = 0;
            try{
                InputStream is = new FileInputStream(FilePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = "";

                while ((line = reader.readLine()) != null) {
                    // 수정할 지인이면 변경된 값을, 수정할 지인이 아니면 원래의 값을 frBuffer에 담는다.
                    if(countN == n){
                        frBuffer.append(friendInfoTxt);
                    }
                    else{
                        frBuffer.append(line+"\n");
                    }
                    countN++;
                    Log.i("frBuffer",frBuffer.toString());
                }
                reader.close();
                is.close();
            }
            catch (Exception e){
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsStrting = sw.toString();
                Log.e("FilereadEFI2", exceptionAsStrting);

                e.printStackTrace();
                Toast.makeText(this.getApplicationContext(), "파일을 읽는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }


            // 아직 파일에 저장 안함

            // 지인 정보 화면으로 다시 이동
            Intent CFIntent = new Intent(EditFriendInfoActivity.this, ConfirmFriendInfoActivity.class);
            CFIntent.putExtra("지인 번호", n);
            CFIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // 상위 스택 액티비티 모두 제거
            EditFriendInfoActivity.this.finish();
            startActivity(CFIntent);
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
            Log.e("FilereadEFI", exceptionAsStrting);

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
            n = friendIntent.getIntExtra("지인 번호", -1);
            Log.i("friendNum", "전달된 지인 번호 : " + n);

            // TextView에 setText
            phoneNumberEditText.setText(readArr[0+5*n]);
            nameEditText.setText(readArr[1+5*n]);
            emailEditText.setText(readArr[2+5*n]);
            groupEditText.setText(readArr[3+5*n]);
            memoEditText.setText(readArr[4+5*n]);
        }
        else{
            Toast.makeText(this.getApplicationContext(), "추가된 지인이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}