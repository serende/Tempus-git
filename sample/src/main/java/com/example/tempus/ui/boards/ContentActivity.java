package com.example.tempus.ui.boards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applandeo.Tempus.R;

public class ContentActivity extends AppCompatActivity {
    TextView nameTextView;
    TextView groupTextView;
    TextView contentView;
    TextView dateView;

    LinearLayout ll;
    Button writeCommentBtn;

    Intent CAIntent;

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

        // 댓글을 보여주는 TextView를 동적으로 배치할 레이아웃
        // 댓글에 나올 내용: 댓글 작성자, 댓글 내용, 댓글 작성 일자
        ll = findViewById(R.id.ll);

        // 댓글 작성 액티비티로 이동 or 팝업이나 누르면 새로운 창이 나오면서 작성할 수 있도록
        writeCommentBtn = findViewById(R.id.writeCommentBtn);
    }
}