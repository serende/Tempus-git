package com.example.tempus.ui.friends;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.Tempus.R;
import com.example.tempus.ui.MyService;
import com.example.tempus.ui.boards.AddBoardActivity;
import com.example.tempus.ui.boards.BoardMainActivity;
import com.example.tempus.ui.boards.ShoppingActivity;
import com.example.tempus.ui.boards.WriteActivity;
import com.example.tempus.ui.boards.boardActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;

    //private FloatingActionButton openFAB, boardMainFAB, shoppingFAB, notifyONFAB, notifyOFFFAB;
    private FloatingActionButton boardMainFAB, shoppingFAB;

    int InviteYN = 0;
    String InviteGroupName;

    String host_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        FLAIntent = getIntent();
        user_EMAIL = FLAIntent.getStringExtra("EMAIL");
        host_ip = FLAIntent.getStringExtra(host_ip);

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EnteringInformationOfFriendActivity.class);
            intent.putExtra("EMAIL", user_EMAIL);
            intent.putExtra("host_ip",host_ip);
            startActivity(intent);
        });

        // 레이아웃 생성
        lm = findViewById(R.id.ll);
        params = new LinearLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT
                , Toolbar.LayoutParams.WRAP_CONTENT);

        /*
        openFAB = findViewById(R.id.openFAB);
        openFAB.setOnClickListener(v -> {
            anim();
        });

         */

        try{
            boardMainFAB = findViewById(R.id.boardMainFAB);
            shoppingFAB = findViewById(R.id.shoppingFAB);
            //notifyONFAB = findViewById(R.id.notifyONFAB);
            //notifyOFFFAB = findViewById(R.id.notifyOFFFAB);
        } catch (Exception e){
            Log.e("FVBERROR", e.toString());
        }

        // anim()에서 사용할 애니메이션
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        // 게시판 추가 액티비티로 이동
        boardMainFAB.setOnClickListener(v -> {
            //anim();
            Intent intent = new Intent(getApplicationContext(), BoardMainActivity.class);
            intent.putExtra("EMAIL", user_EMAIL);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("host_ip",host_ip);
            FriendListActivity.this.finish();
            startActivity(intent);
        });

        shoppingFAB.setOnClickListener(v -> {
            //anim();
            Intent intent = new Intent(getApplicationContext(), ShoppingActivity.class);
            intent.putExtra("host_ip",host_ip);
            startActivity(intent);
        });

        /*
        notifyONFAB.setOnClickListener(v -> {
            anim();
            Toast.makeText(getApplicationContext(),"알림 ON",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(FriendListActivity.this, MyService.class);    // 알림 서비스 실행
            startService(intent);
        });

        notifyOFFFAB.setOnClickListener(v -> {
            anim();
            Toast.makeText(getApplicationContext(),"알림 OFF",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FriendListActivity.this,MyService.class);     // 알림 서비스 종료
            stopService(intent);
        });
         */
        MakeLinearLayout(lm);

    }

    // 애니메이션 실행 함수
    public void anim() {
        if(isFabOpen) {
            try{
                boardMainFAB.startAnimation(fab_close);
                shoppingFAB.startAnimation(fab_close);
                //notifyONFAB.startAnimation(fab_close);
                //notifyOFFFAB.startAnimation(fab_close);

                boardMainFAB.setClickable(false);
                shoppingFAB.setClickable(false);
                //notifyONFAB.setClickable(false);
                //notifyOFFFAB.setClickable(false);

                isFabOpen = false;
            } catch (Exception e){
                Log.e("animERROR", e.toString());
            }

        } else {
            try{
                boardMainFAB.startAnimation(fab_open);
                shoppingFAB.startAnimation(fab_open);
                //notifyONFAB.startAnimation(fab_open);
                //notifyOFFFAB.startAnimation(fab_open);

                boardMainFAB.setClickable(true);
                shoppingFAB.setClickable(true);
                //notifyONFAB.setClickable(true);
                //notifyOFFFAB.setClickable(true);

                isFabOpen = true;
            } catch (Exception e){
                Log.e("animERROR", e.toString());
            }
        }
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
                    //InfoView1.setTextColor(Color.BLUE);
                    InfoView1.setPadding(102, 40, 0, 0);

                    InfoView2.setText(" " + readArr[5*n+3]);
                    InfoView2.setPadding(250,40,0,0);
                    //InfoView2.setTextColor(Color.BLUE);
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
                    btn.setTextColor(Color.BLUE);
                    btn.setBackgroundColor(0);

                    final int friendNum = n;

                    // 버튼 클릭 시 지인 정보 확인 페이지로 이동
                    btn.setOnClickListener(v -> {
                        Log.d("log", "position :" + friendNum);

                        Intent intent = new Intent(FriendListActivity.this, ConfirmFriendInfoActivity.class);
                        intent.putExtra("지인 번호", friendNum);
                        intent.putExtra("EMAIL", user_EMAIL);
                        intent.putExtra("host_ip",host_ip);
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
