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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BoardMainActivity extends AppCompatActivity {

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;

    private FloatingActionButton openFAB, addBoardFAB, friendFAB, shoppingFAB;
    private ImageButton imageButton1, imageButton2;

    GridLayout grid;

    Intent BMAIntent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("게시판");

        FloatingActionButton openFAB = findViewById(R.id.openFAB);
        openFAB.setOnClickListener(v -> {
            anim();
        });

        // 게시판 버튼을 생성할 그리드레이아웃
        grid = findViewById(R.id.grid);

        // 플로팅 버튼 열고닫는 애니메이션
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        try{
            addBoardFAB = findViewById(R.id.addBoardFAB);
            friendFAB = findViewById(R.id.friendFAB);
            shoppingFAB = findViewById(R.id.shoppingFAB);
        } catch (Exception e){
            Log.e("FVBERROR", e.toString());
        }

        // 게시판 추가 액티비티로 이동
        addBoardFAB.setOnClickListener(v -> {
            anim();
            Intent intent = new Intent(getApplicationContext(), AddBoardActivity.class);
            startActivity(intent);
        });

        // 지인 목록 액티비티로 이동
        friendFAB.setOnClickListener(v -> {
            anim();
            Intent BMAIntent = new Intent(getApplicationContext(), FriendListActivity.class);
            startActivity(BMAIntent);
        });

        shoppingFAB.setOnClickListener(v -> {
            anim();
            // TODO
        });

        imageButton1 = findViewById(R.id.imageButton1);
        imageButton1.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), boardActivity.class);
            intent.putExtra("GROUP", "2팀 게시판");
            startActivity(intent);
        });

        imageButton2 = findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), boardActivity.class);
            intent.putExtra("GROUP", "우리 가족방");
            startActivity(intent);
        });

        try{
            makeLinearLayout(grid);
        } catch(Exception e){

        }
    }

    // 플로팅 버튼 애니메이션 설정 함수
    public void anim() {
        if(isFabOpen) {
            try{
                addBoardFAB.startAnimation(fab_close);
                friendFAB.startAnimation(fab_close);
                shoppingFAB.startAnimation(fab_close);

                addBoardFAB.setClickable(false);
                friendFAB.setClickable(false);
                shoppingFAB.setClickable(false);

                isFabOpen = false;
            } catch (Exception e){
                Log.e("animERROR", e.toString());
            }

        } else {
            try{
                addBoardFAB.startAnimation(fab_open);
                friendFAB.startAnimation(fab_open);
                shoppingFAB.startAnimation(fab_open);

                addBoardFAB.setClickable(true);
                friendFAB.setClickable(true);
                shoppingFAB.setClickable(true);

                isFabOpen = true;
            } catch (Exception e){
                Log.e("animERROR", e.toString());
            }
        }
    }

    // 그리드 레이아웃 내에 리니어레이아웃을 생성할 함수
    public void makeLinearLayout(GridLayout gl){
        LinearLayout sl = new LinearLayout(this);
        sl.setOrientation(LinearLayout.VERTICAL);

        // addBoard에서 전달 받은 이미지 또는 서버에서 전달받은 이미지를 보여주며, board액티비티로 이동시키는 버튼
        ImageButton IB = new ImageButton(this);
        byte[] byteArray = BMAIntent.getByteArrayExtra("image");
        IB.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));

        // addBoard에서 전달 받은 게시판명을 텍스트뷰로 세팅
        TextView BoardText = new TextView(this);
        BoardText.setText(BMAIntent.getStringExtra("boardName"));

        IB.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), boardActivity.class);
            intent.putExtra("GROUP", BMAIntent.getStringExtra("boardName"));
            startActivity(intent);
        });

        sl.addView(IB);
        sl.addView(BoardText);

        gl.addView(sl);
    }
}