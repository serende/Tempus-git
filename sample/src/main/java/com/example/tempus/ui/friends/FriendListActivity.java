//package com.example.tempus.ui.friends;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.applandeo.Tempus.CalendarActivity;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.applandeo.Tempus.R;
//
//public class FriendListActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_friend_list);
//
//        Button addButton = (Button) findViewById(R.id.addButton);
//        addButton.setOnClickListener(v -> {
//            Intent intent = new Intent(this, AddFriendsActivity.class);
//            startActivity(intent);
//        });
//
//        // final LinearLayout lm = (LinearLayout) findViewById(R.id.ll);
//
//        /* 동적 레이아웃
//        // linearLayout params 정의
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
//
//        // 버튼 동적 생성, 저장된 지인 정보와 동기화되도록 수정 필요, 뷰 위치 수정 필요
//        for (int j=0; j<=5; j++){
//            //LinearLayout 생성
//            LinearLayout ll = new LinearLayout(this);
//            ll.setOrientation(LinearLayout.HORIZONTAL);
//
//            // TextView 생성
//            TextView tvProdc = new TextView(this);
//            tvProdc.setText("Name" + j + " " + "2팀");
//            ll.addView(tvProdc);
//
//            // 버튼 생성
//            final Button btn = new Button(this);
//
//            // setId 버튼에 대한 키값
//            btn.setId(j + 1);
//            btn.setText("Apply");
//            btn.setLayoutParams(params);
//
//            final int position = j;
//
//            btn.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    Log.d("log", "position :" + position);
//                    Toast.makeText(getApplicationContext(), "클릭한 position:" + position, Toast.LENGTH_LONG).show();
//                }
//            });
//
//            //버튼 add
//            ll.addView(btn);
//
//            //정의된 LinearLayout add
//            lm.addView(ll);
//        }
//
//         */
//
//        Button friendsNameButton = (Button) findViewById(R.id.friendsNameButton);
//        friendsNameButton.setOnClickListener(v -> {
//            // 지인 정보를 보여주는 페이지로 이동
//        });
//
//        Button friendsGroupButton = (Button) findViewById(R.id.friendsGroupButton);
//        friendsGroupButton.setOnClickListener(v -> {
//            // 지인 정보를 보여주는 페이지로 이동
//        });
//
//    }
//}

package com.example.tempus.ui.friends;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.Tempus.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

@SuppressLint("SdCardPath")
public class FriendListActivity extends AppCompatActivity {

    // avd가 아닌 스마트폰에서 실행할 시 경로 체크 필요
    final static String FilePath= "/data/data/com.applandeo.materialcalendarsampleapp/files/friendList.txt";

    LinearLayout lm;
    LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddFriendsActivity.class);
            startActivity(intent);
        });

        /*
        동적 레이아웃에서 수행되어야 할 것
        1. 루프를 통해 friendlist.txt에서 등록된 지인 수만큼 반복
        2. 공백을 통해 지인 정보를 구분하여 필요한 정보인 이름과 그룹명만 불러와서 저장
        3. 저장된 내용을 setText로 받아와서 출력
        4. tableLayout을 통해 정렬
        5. 각 지인별로 지인 정보 페이지로 이동하는 버튼 생성
         */

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
            Log.e("Fileread", exceptionAsStrting);

            e.printStackTrace();
            Toast.makeText(this.getApplicationContext(), "파일을 읽는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            return "";
        }
        return strBuffer.toString();
    }

    public void MakeLinearLayout (LinearLayout lm){
        // 파일에서 지인 정보 가져옴
        String read = ReadFile(FilePath);

        // '-'를 기준으로 지인 정보 분류
        String[] readArr = read.split("\\-");

        if (readArr != null)
        {
            int nCnt = readArr.length;

            // readArr[0+5n]: 전화번호, readArr[1+5n]: 등록명, readArr[2+5n]: 이메일, readArr[3+5n]: 그룹명, readArr[4+5n]: 메모
            for (int i=0; i<nCnt; ++i)
            {
                Log.i("ARRTAG", "arr[" + i + "] = " + readArr[i]);
            }

            try{
                // 레이아웃 추가
                LinearLayout ll = new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);

                // n은 지인 번호
                for(int n=0; ;n++){
                    LinearLayout sl = new LinearLayout(this);
                    sl.setOrientation(LinearLayout.HORIZONTAL);

                    // 지인 정보를 보여줄 TextView 추가
                    // 보여주는 정보는 등록된 지인명과 그룹명
                    TextView InfoView1 = new TextView(this);
                    TextView InfoView2 = new TextView(this);
                    InfoView1.setText(" " + readArr[5*n+1]);
                    InfoView1.setPadding(100, 40, 0, 0);
                    InfoView2.setText(" " + readArr[5*n+3]);
                    InfoView2.setPadding(252,40,0,0);
                    sl.addView(InfoView1);
                    sl.addView(InfoView2);

                    // 버튼 생성
                    final Button btn = new Button(this);

                    // setId 버튼에 대한 키값
                    btn.setId(n + 1);
                    btn.setText("정보 확인");
                    btn.setPadding(0,40,20,0);
                    btn.setLayoutParams(params);

                    final int position = n;

                    // 지인 정보 확인 페이지로 이동해야 함
                    btn.setOnClickListener(v -> {
                        Log.d("log", "position :" + position);
                        Toast.makeText(getApplicationContext(), "클릭한 position:" + position, Toast.LENGTH_LONG).show();
                    });

                    //버튼 add
                    sl.addView(btn);

                    // lm에 정의된 레이아웃 추가
                    ll.addView(sl);
                    lm.addView(ll);

                    /*
        final LinearLayout lm = (LinearLayout) findViewById(R.id.ll);

        // linearLayout params 정의
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);

        // 버튼 동적 생성, 저장된 지인 정보와 동기화되도록 수정 필요, 뷰 위치 수정 필요
        for (int j=0; j<=5; j++){
            //LinearLayout 생성
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            // TextView 생성
            TextView tvProdc = new TextView(this);
            tvProdc.setText("Name" + j + " " + "2팀");
            ll.addView(tvProdc);

            // 버튼 생성
            final Button btn = new Button(this);

            // setId 버튼에 대한 키값
            btn.setId(j + 1);
            btn.setText("Apply");
            btn.setLayoutParams(params);

            final int position = j;

            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("log", "position :" + position);
                    Toast.makeText(getApplicationContext(), "클릭한 position:" + position, Toast.LENGTH_LONG).show();
                }
            });

            //버튼 add
            ll.addView(btn);

            //정의된 LinearLayout add
            lm.addView(ll);
        }
         */
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
}


