package com.example.tempus.ui.friends;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.Tempus.R;
import com.example.tempus.ui.boards.WriteActivity;
import com.example.tempus.ui.boards.boardActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

@SuppressLint("SdCardPath")
public class FriendListActivity extends AppCompatActivity {

    final static String FilePath= "/data/data/com.applandeo.materialcalendarsampleapp/files/friendList.txt";

    LinearLayout lm;
    LinearLayout.LayoutParams params;

    Button addButton;

    Intent FLAIntent;
    String user_EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        FLAIntent = getIntent();
        user_EMAIL = FLAIntent.getStringExtra("EMAIL");

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddFriendsActivity.class);
            intent.putExtra("EMAIL", user_EMAIL);
            startActivity(intent);
        });

        // 레이아웃 생성
        lm = findViewById(R.id.ll);
        params = new LinearLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT
                , Toolbar.LayoutParams.WRAP_CONTENT);

        MakeLinearLayout(lm);
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
            Log.e("FilereadFL", exceptionAsStrting);

            e.printStackTrace();
            Toast.makeText(this.getApplicationContext(), "저장된 지인 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            return "";
        }
        return strBuffer.toString();
    }

    public void MakeLinearLayout (LinearLayout lm){
        // 파일에서 지인 정보 가져옴
        String read = ReadFile(FilePath);

        // '|'를 기준으로 지인 정보 분류
        String[] readArr = read.split("\\|");

        if (readArr != null)
        {
            int nCnt = readArr.length;

            // readArr[0+5n]: 전화번호, readArr[1+5n]: 등록명, readArr[2+5n]: 이메일, readArr[3+5n]: 그룹명, readArr[4+5n]: 메모
            for (int i=0; i<nCnt; ++i)
            {
                Log.i("ARRTAG", "arr[" + i + "] = " + readArr[i]);
            }

            try{
                LinearLayout.LayoutParams Riparams = new LinearLayout.LayoutParams(ConvertDPtoPX(this,100), ConvertDPtoPX(this, 50));
                Riparams.weight = 1.0f;
                Riparams.gravity = Gravity.RIGHT;

                // n은 지인 번호
                for(int n=0;n<readArr.length/5;n++){
                    LinearLayout sl = new LinearLayout(this);
                    sl.setOrientation(LinearLayout.HORIZONTAL);

                    // 지인 정보를 보여줄 TextView 추가
                    // 보여주는 정보는 등록된 지인명과 그룹명
                    TextView InfoView1 = new TextView(this);
                    TextView InfoView2 = new TextView(this);

                    InfoView1.setText(" " + readArr[5*n+1]);
                    InfoView1.setGravity(Gravity.CENTER);
                    InfoView1.setPadding(102, 40, 0, 0);

                    InfoView2.setText(" " + readArr[5*n+3]);
                    InfoView2.setPadding(250,40,0,0);
                    sl.addView(InfoView1);
                    sl.addView(InfoView2);

                    Log.v("setText", " " + readArr[5*n+1] + " " + readArr[5*n+3]);

                    LinearLayout btnL = new LinearLayout(this);
                    btnL.setLayoutParams(Riparams);

                    // 버튼 생성
                    final Button btn = new Button(this);

                    // setId 버튼에 대한 키값
                    btn.setId(n + 1);
                    btn.setText("정보 확인");
                    btn.setPadding(200,10,100,0);
                    btn.setBackgroundColor(0);

                    final int friendNum = n;

                    // 버튼 클릭 시 지인 정보 확인 페이지로 이동
                    btn.setOnClickListener(v -> {
                        Log.d("log", "position :" + friendNum);

                        Intent intent = new Intent(FriendListActivity.this, ConfirmFriendInfoActivity.class);
                        intent.putExtra("지인 번호", friendNum);
                        intent.putExtra("EMAIL", user_EMAIL);
                        startActivity(intent);
                    });

                    //버튼 add
                    btnL.addView(btn);
                    sl.addView(btnL);

                    // lm에 정의된 레이아웃 추가
                    lm.addView(sl);
                }
            }
            catch(Exception e){
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsStrting = sw.toString();
                Log.e("makell", exceptionAsStrting);

                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this.getApplicationContext(), "추가된 지인이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 동적 레이아웃에서 사이즈를 DP로 쓰기 위한 함수
    public static int ConvertDPtoPX(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
