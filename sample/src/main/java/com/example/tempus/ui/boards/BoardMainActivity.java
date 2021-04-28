package com.example.tempus.ui.boards;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.applandeo.Tempus.R;
import com.example.tempus.ui.addSchedule.expenditureBreakdownActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class BoardMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("게시판");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 게시판 추가 페이지로 이동하도록 변경해야 함
                Intent intent = new Intent(getApplicationContext(), AddBoardActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.boardmenu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch(id){
            case R.id.action_search:
                Toast.makeText(getApplicationContext(), "검색",
                        Toast.LENGTH_SHORT).show();
            case R.id.changeDisplayCalendar:
                Toast.makeText(getApplicationContext(), "캘린더형으로 변경",
                        Toast.LENGTH_SHORT).show();
            case R.id.changeDisplaySlide:
                Toast.makeText(getApplicationContext(), "슬라이드형으로 변경",
                        Toast.LENGTH_SHORT).show();
            case R.id.registerFriends:
                Toast.makeText(getApplicationContext(), "지인 등록",
                        Toast.LENGTH_SHORT).show();
            case R.id.friendsList:
                Toast.makeText(getApplicationContext(), "지인 목록 보기",
                        Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
