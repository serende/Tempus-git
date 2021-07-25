package com.example.tempus.ui.boards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.applandeo.Tempus.R;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

import static java.sql.Types.NULL;

public class ContentActivity extends AppCompatActivity {
    TextView nameTextView;
    TextView groupTextView;
    TextView contentView;
    TextView dateView;

    Button writeCommentBtn;

    Intent CAIntent;

    static final String[] LIST_MENU = {"댓글 작성자", "댓글 내용", "작성 일자"} ;
    ArrayAdapter adapter;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        CAIntent = getIntent();

        // 작성자명
        nameTextView = findViewById(R.id.nameTextView);

        // 그룹명
        groupTextView = findViewById(R.id.groupTextView);
        groupTextView.setText("그룹: " + CAIntent.getStringExtra("GROUP"));

        // 글의 내용
        contentView = findViewById(R.id.contentView);
        contentView.setText(CAIntent.getStringExtra("CONTENT"));

        // 작성 일자
        dateView = findViewById(R.id.dateView);
        contentView.setText(CAIntent.getStringExtra("DATE"));

        // ListView를 통한 댓글 구현
        // 댓글 하나당 list 3칸을 차지하도록 구현(댓글 작성자, 댓글 내용, 댓글 작성 일자)
        adapter = new ArrayAdapter(this, R.layout.memolist_type, LIST_MENU);

        listview = findViewById(R.id.listview1);
        listview.setAdapter(adapter);


        // 댓글 작성 액티비티로 이동 or 팝업이나 누르면 새로운 창이 나오면서 작성할 수 있도록
        writeCommentBtn = findViewById(R.id.writeCommentBtn);
    }

}