package com.example.tempus.ui.boards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.applandeo.Tempus.R;

import org.w3c.dom.Text;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Inflater;

import static java.sql.Types.NULL;

public class ContentActivity extends AppCompatActivity {
    TextView nameTextView;
    TextView groupTextView;
    TextView contentView;
    TextView dateView;

    Button writeCommentBtn;

    Intent CAIntent;

    static final String[] LIST_MENU = new String[99] ;
    ArrayAdapter adapter;
    ListView listview;

    EditText commentEdit;

    Integer n=1;
    String comNum; // comment number

    String user_EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        CAIntent = getIntent();
        user_EMAIL = CAIntent.getStringExtra("EMAIL");

        // 작성자명
        nameTextView = findViewById(R.id.nameTextView);
        nameTextView.setText(CAIntent.getStringExtra("WRITER"));
        if(CAIntent.getStringExtra("WRITER") == "") nameTextView.setText("작성자");

        // 그룹명
        groupTextView = findViewById(R.id.groupTextView);
        groupTextView.setText("그룹: " + CAIntent.getStringExtra("GROUP"));

        // 글의 내용
        contentView = findViewById(R.id.contentView);
        contentView.setText(CAIntent.getStringExtra("CONTENT"));
        if(CAIntent.getStringExtra("CONTENT") == "") contentView.setText("작성된 내용이 없습니다.");

        // 작성 일자
        dateView = findViewById(R.id.dateView);
        dateView.setText(CAIntent.getStringExtra("DATE"));

        // ListView를 통한 댓글 구현
        // 댓글 하나당 list 3칸을 차지하도록 구현(댓글 작성자, 댓글 내용, 댓글 작성 일자)
        adapter = new ArrayAdapter(this, R.layout.memolist_type, LIST_MENU);

        listview = findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        commentEdit = findViewById(R.id.commentEdit);

        // 댓글 작성
        // 누르면 댓글 창에 작성된 내용을 리스트배열로 전달하고 댓글 창을 비움
        // 나중에 서버로 전달하는 것도 추가해야 함
        // LIST_MENU n-1 n n+1 -> n-1:작성자 n:내용 n+1:작성 시간
        writeCommentBtn = findViewById(R.id.writeCommentBtn);
        writeCommentBtn.setOnClickListener(v -> {
            try{
                // 댓글 작성자 가져오는 내용
                // 임시로 작성자 1 2 3으로 나오도록 설정
                LIST_MENU[n-1]="작성자 " + n;

                // 댓글 내용을
                LIST_MENU[n]=commentEdit.getText().toString();

                // 댓글 작성 시간
                LIST_MENU[n+1]=GetTime();

                // 댓글 수+1(3개 단위)
                n=n+3;

                // 댓글창 초기화
                commentEdit.setText(null);

                /*
                // 댓글 정보를 서버로 전송하는 부분 필요
                comNum = n/3
                 */
            }
            catch (Exception e){
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsStrting = sw.toString();
                Log.e("CALIST", exceptionAsStrting);

                e.printStackTrace();
            }

        });
    }
    // 현재 시간 얻는 함수
    public String GetTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM월 dd일 hh시 mm분");
        String getTime = dateFormat.format(date);

        return getTime;
    }
}