package com.example.tempus.ui.boards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.Tempus.R;
import com.example.tempus.ui.friends.AddFriendsActivity;
import com.example.tempus.ui.friends.ConfirmFriendInfoActivity;
import com.example.tempus.ui.friends.FriendListActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class InviteActivity extends AppCompatActivity {

    final static String FilePath= "/data/data/com.applandeo.materialcalendarsampleapp/files/friendList.txt";

    LinearLayout lm;
    LinearLayout.LayoutParams params;
    
    Button addButton, finButton;

    // 지인을 선택하면 지인명을 저장할 배열
    String[] names = new String[30];
    Integer nameNum = 0;

    String read;
    String[] readArr;

    Intent IAIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        /*
        여기서 지인목록으로부터 지인을 선택하고, board로 넘겨준 뒤
        거기서 서버와 WR_USER로 공유해서 게시판에 등록된 유저인지 판단
         */

        IAIntent = getIntent();

        addButton = findViewById(R.id.addButton);
        finButton = findViewById(R.id.finButton);
        lm = findViewById(R.id.ll);

        // 파일에서 지인 정보 가져옴
        read = ReadFile(FilePath);

        // '|'를 기준으로 지인 정보 분류
        readArr = read.split("\\|");
        
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddFriendsActivity.class);
            startActivity(intent);
        });
        finButton.setOnClickListener(v -> {
            // Board로 이동하면서 선택한 지인 정보 전달하도록 변경 필요
            Intent intent = new Intent(this, boardActivity.class);
            intent.putExtra("names", names);
            intent.putExtra("nameNum", nameNum);
            intent.putExtra("GROUP", IAIntent.getStringExtra("GROUP"));

            for(int i = 0; i<nameNum; i++){
                Log.i("testNames", names[i] + " " + nameNum + " ");
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            InviteActivity.this.finish();
            startActivity(intent);
        });

        // 레이아웃 생성
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

    // 체크하는 버튼 추가해야 함
    public void MakeLinearLayout (LinearLayout lm){

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

                    // 지인명
                    InfoView1.setText(" " + readArr[5*n+1]);
                    InfoView1.setTextColor(Color.BLACK);
                    InfoView1.setGravity(Gravity.CENTER);
                    InfoView1.setPadding(102, 40, 0, 0);

                    // 그룹명
                    InfoView2.setText(" " + readArr[5*n+3]);
                    InfoView2.setTextColor(Color.BLACK);
                    InfoView2.setPadding(250,40,0,0);
                    sl.addView(InfoView1);
                    sl.addView(InfoView2);

                    Log.v("setText", " " + readArr[5*n+1] + " " + readArr[5*n+3]);

                    LinearLayout btnL = new LinearLayout(this);
                    btnL.setLayoutParams(Riparams);

                    // 정보 확인 버튼 생성
                    final Button btn = new Button(this);

                    // setId 버튼에 대한 키값
                    btn.setId(n + 1);
                    btn.setText("정보 확인");
                    btn.setTextColor(Color.BLACK);
                    btn.setPadding(200,0,100,0);
                    btn.setBackgroundColor(0);

                    final int friendNum = n;

                    // 버튼 클릭 시 지인 정보 확인 페이지로 이동
                    btn.setOnClickListener(v -> {
                        Log.d("log", "position :" + friendNum);

                        Intent baIntent = new Intent(InviteActivity.this, ConfirmFriendInfoActivity.class);
                        baIntent.putExtra("지인 번호", friendNum);
                        startActivity(baIntent);
                    });

                    // 멤버로 추가할 지인을 선택하는 체크박스
                    final CheckBox cb = new CheckBox(this);
                    cb.setBackgroundColor(Color.GRAY);
                    cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            // TODO
                            // names 배열에 nameNum번째에 해당 이름을 집어넣고 nameNum++
                            names[nameNum] = readArr[5*nameNum+1];
                            Log.i("checkTest", nameNum+1 + "번째 "+ readArr[5*nameNum+1] + "추가");
                            Toast.makeText(getApplicationContext(),nameNum+1 + "번째 "+ readArr[5*nameNum+1] + " 추가",Toast.LENGTH_SHORT).show();
                            nameNum++;
                        } else {
                            try{
                                // names 배열에 nameNum번쨰에 있는 이름을 제거하고 nameNum--
                                names[nameNum-1] = NULL;
                                Log.i("checkTest", nameNum + "번째 "+ readArr[5*(nameNum-1)+1] + "삭제");
                                Toast.makeText(getApplicationContext(),nameNum + "번째 "+ readArr[5*(nameNum-1)+1] + " 삭제",Toast.LENGTH_SHORT).show();
                                nameNum--;
                            }catch(Exception e){
                                Log.e("checkTest", e.toString());
                            }

                        }
                    });

                    //버튼 및 체크박스 add
                    btnL.addView(btn);
                    sl.addView(btnL);
                    sl.addView(cb);

                    // lm에 정의된 레이아웃 추가
                    lm.addView(sl);
                }
            }
            catch(Exception e){
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsStrting = sw.toString();
                Log.e("makellIA", exceptionAsStrting);

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