package com.example.tempus.ui.boards;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.applandeo.Tempus.R;
import com.example.tempus.ui.addSchedule.expenditureBreakdownActivity;
import com.example.tempus.ui.friends.AddFriendsActivity;
import com.example.tempus.ui.friends.FriendListActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BoardMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("게시판");

        // 플로팅버튼은 게시판추가 액티비티로 이동
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddBoardActivity.class);
                startActivity(intent);
            }
        });

        ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        imageButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent baIntent = new Intent(getApplicationContext(), boardActivity.class);
                startActivity(baIntent);
            }
        });

        ImageButton imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent baIntent2 = new Intent(getApplicationContext(), boardActivity.class);
                startActivity(baIntent2);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.boardmenu, menu);
        return true;
    }

    // 메뉴에서 다른 액티비티로 이동
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
                Intent AFintent = new Intent(getApplicationContext(), AddFriendsActivity.class);
                startActivity(AFintent);
            case R.id.friendsList:
                Toast.makeText(getApplicationContext(), "지인 목록 보기",
                        Toast.LENGTH_SHORT).show();
                Intent FLintent = new Intent(getApplicationContext(), FriendListActivity.class);
                startActivity(FLintent);
        }
        return super.onOptionsItemSelected(item);
    }
}