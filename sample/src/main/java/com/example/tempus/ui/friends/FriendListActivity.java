package com.example.tempus.ui.friends;

import android.content.Intent;
import android.os.Bundle;

import com.applandeo.Tempus.CalendarActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.Tempus.R;

public class FriendListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddFriendsActivity.class);
            startActivity(intent);
        });

        final LinearLayout lm = (LinearLayout) findViewById(R.id.ll);

        // linearLayout params 정의
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);

        // 버튼 동적 생성, 저장된 지인 정보와 동기화되도록 수정 필요, 디자인 수정 필요
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

        Button friendsNameButton = (Button) findViewById(R.id.friendsNameButton);
        friendsNameButton.setOnClickListener(v -> {
            // 지인 정보를 보여주는 페이지로 이동
        });

        Button friendsGroupButton = (Button) findViewById(R.id.friendsGroupButton);
        friendsGroupButton.setOnClickListener(v -> {
            // 지인 정보를 보여주는 페이지로 이동
        });

    }
}
