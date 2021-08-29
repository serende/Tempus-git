package com.example.tempus.ui.addSchedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.Tempus.R;
import com.example.tempus.ui.boards.ContentActivity;
import com.example.tempus.ui.friends.ConfirmFriendInfoActivity;
import com.example.tempus.ui.friends.FriendListActivity;

import org.w3c.dom.Text;

public class ConfirmScheduleActivity extends AppCompatActivity {

    TextView DateTextView;
    LinearLayout ll;
    LinearLayout.LayoutParams params;

    Intent CSAIntent;

    // 일정 내용
    String content;

    // 작성자
    String writer;

    // 그룹명
    String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_schedule);

        CSAIntent = getIntent();

        DateTextView = findViewById(R.id.DateTextView);
        DateTextView.setText(CSAIntent.getStringExtra("날짜"));

        ll = findViewById(R.id.ll);
        params = new LinearLayout.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT
                , Toolbar.LayoutParams.MATCH_PARENT);

        // 임시 텍스트
        content = "오늘 마트에서 햄이 싸게 팔길래 많이 사왔어.\n"  + "저녁은 이걸로 부대찌개 해줄게~";
        writer = "어머니";
        groupName = "우리 가족방";

        makeLinearLayout(ll);
    }

    // 일정 뷰어 레이아웃 생성
    // 내용 일부, 작성자, 그룹으로 구성
    public void makeLinearLayout(LinearLayout ll){
        // 데이터가 들어가지 않아서 테스트용으로 for문을 한바퀴만 돌도록 설정
        for(int n=0;;n=n+3){
            LinearLayout sl = new LinearLayout(this);
            sl.setOrientation(LinearLayout.VERTICAL);
            sl.setBackground(ContextCompat.getDrawable(this, R.drawable.layoutborder4));

            Button writerBtn = new Button(this);
            Button contentBtn = new Button(this);
            Button groupBtn = new Button(this);

            writerBtn.setId(n+1);
            writerBtn.setText(writer);
            writerBtn.setTextColor(0xffff7f00);
            writerBtn.setBackgroundColor(0);
            writerBtn.setGravity(Gravity.LEFT);
            writerBtn.setPadding(ConvertDPtoPX(this,14),ConvertDPtoPX(this,10),0,0);

            // 글 내용을 일부만 보이게 수정 필요
            contentBtn.setId(n+2);
            contentBtn.setText(content);
            contentBtn.setTextColor(Color.BLACK);
            contentBtn.setBackgroundColor(0);
            contentBtn.setGravity(Gravity.LEFT);
            contentBtn.setPadding(ConvertDPtoPX(this,14),0,0,0);

            groupBtn.setId(n+3);
            groupBtn.setText(groupName);
            groupBtn.setTextColor(0xff50bcdf);
            groupBtn.setBackgroundColor(0);
            groupBtn.setGravity(Gravity.LEFT);
            groupBtn.setPadding(ConvertDPtoPX(this,14),10,0,0);

            writerBtn.setOnClickListener(v -> {
                Intent CSAOutIntent= new Intent(ConfirmScheduleActivity.this, ContentActivity.class);
                CSAOutIntent.putExtra("WRITER", writer);
                CSAOutIntent.putExtra("CONTENT", contentBtn.getText().toString());
                CSAOutIntent.putExtra("GROUP",  groupName);
                CSAOutIntent.putExtra("DATE", CSAIntent.getStringExtra("날짜"));
                startActivity(CSAOutIntent);
            });

            contentBtn.setOnClickListener(v -> {
                Intent CSAOutIntent= new Intent(ConfirmScheduleActivity.this, ContentActivity.class);
                CSAOutIntent.putExtra("WRITER", writer);
                CSAOutIntent.putExtra("CONTENT", contentBtn.getText().toString());
                CSAOutIntent.putExtra("GROUP",  groupName);
                CSAOutIntent.putExtra("DATE", CSAIntent.getStringExtra("날짜"));
                startActivity(CSAOutIntent);
            });

            groupBtn.setOnClickListener(v -> {
                Intent CSAOutIntent= new Intent(ConfirmScheduleActivity.this, ContentActivity.class);
                CSAOutIntent.putExtra("WRITER", writer);
                CSAOutIntent.putExtra("CONTENT", contentBtn.getText().toString());
                CSAOutIntent.putExtra("GROUP",  groupName);
                CSAOutIntent.putExtra("DATE", CSAIntent.getStringExtra("날짜"));
                startActivity(CSAOutIntent);
            });

            sl.addView(writerBtn);
            sl.addView(contentBtn);
            sl.addView(groupBtn);

            // ll에 정의된 레이아웃 추가
            ll.addView(sl);

            break;
        }
    }

    // 동적 레이아웃에서 사이즈를 DP로 쓰기 위한 함수
    public static int ConvertDPtoPX(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}