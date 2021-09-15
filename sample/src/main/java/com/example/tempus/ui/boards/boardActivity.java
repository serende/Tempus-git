package com.example.tempus.ui.boards;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.applandeo.Tempus.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class boardActivity extends AppCompatActivity {

    TextView dateView;
    Button contentView;
    TextView groupTextView;

    String WR_ID;   //id data
    String WR_TYPE; //TYPE
    String WR_DATE; //DATE
    String WR_BODY; //content
    String WR_CONNUM; // content number
    String WR_GROUP;
    Intent BAIntent;
    FloatingActionButton addFAB, friendFAB;
    String userjson;
    String[] names = new String[30];
    Integer nameNum;
    int count = 0;
    LinearLayout ll;
    LinearLayout.LayoutParams params;

    String user_EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        BAIntent = getIntent();
        names = getIntent().getStringArrayExtra("names");
        nameNum = getIntent().getIntExtra("nameNum", 0);
        user_EMAIL = BAIntent.getStringExtra("EMAIL");

        dateView = findViewById(R.id.dateView);
        contentView = findViewById(R.id.contentView);

        groupTextView = findViewById(R.id.groupTextView);
        groupTextView.setText("그룹: " + BAIntent.getStringExtra("GROUP"));
        WR_GROUP = BAIntent.getStringExtra("GROUP");
        PostTask task = new PostTask();
        task.execute();

        addFAB = findViewById(R.id.addFAB);
        addFAB.setOnClickListener(v -> {
            Intent intent = new Intent(boardActivity.this, WriteActivity.class);
            intent.putExtra("EMAIL", user_EMAIL);
            intent.putExtra("GROUP", BAIntent.getStringExtra("GROUP"));
            startActivity(intent);
        });

        // 멤버 등록 페이지로 이동
        friendFAB = findViewById(R.id.friendFAB);
        friendFAB.setOnClickListener(v -> {
            // TODO
            Intent intent = new Intent(boardActivity.this, InviteActivity.class);
            intent.putExtra("GROUP", BAIntent.getStringExtra("GROUP"));
            intent.putExtra("EMAIL", user_EMAIL);
            startActivity(intent);
        });

        // 글을 누르면 댓글을 쓸 수 있는 ContentActivity로 이동
        contentView.setOnClickListener(v -> {
            Intent intent = new Intent(boardActivity.this, ContentActivity.class);
            intent.putExtra("ID", WR_ID);
            intent.putExtra("TYPE", WR_TYPE);
            intent.putExtra("DATE", WR_DATE);
            intent.putExtra("CONTENT", WR_BODY);
            intent.putExtra("GROUP", BAIntent.getStringExtra("GROUP"));
            intent.putExtra("EMAIL", user_EMAIL);
            startActivity(intent);
        });

        // 레이아웃 생성
        ll = findViewById(R.id.ll);
        params = new LinearLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT
                , Toolbar.LayoutParams.WRAP_CONTENT);

//        MakeLinearLayout(ll);
    }

    private class PostTask extends AsyncTask<String, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        StringBuffer tempboardname = new StringBuffer();


        @Override
        protected String doInBackground(String... params) {
            String userid = params[0];
            try {
                String site_url_json = "http://192.168.0.3:5000/board";
//                String site_url_json = "https://webhook.site/088d425c-1da8-4bb0-922d-f632cf432ec4";
                URL url = new URL(site_url_json);

                urlConnection = (HttpURLConnection) url.openConnection();//HttpURLConnection 객체를 생성하여 openConnection 메소드로 url 연결
//                urlConnection.setRequestMethod("GET");//웹서버에 대한 요청 옵션
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                OutputStreamWriter streamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
                streamWriter.write(userid);
                streamWriter.flush();
                streamWriter.close();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;//버퍼에 넣게 도와줄 문자열 변수
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

        }
    }

    public void MakeLinearLayout(LinearLayout ll){
        try{
        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParams.weight = 1.0f;
        LayoutParams.gravity = Gravity.CENTER;

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertDPtoPX(this, 200));
        btnParams.gravity = Gravity.LEFT|Gravity.START;

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ConvertDPtoPX(this, 30));
        tvParams.gravity = Gravity.LEFT|Gravity.CENTER;

        // for루프로 글이 작성된 횟수만큼 반복하도록 해야 함

        LinearLayout sl = new LinearLayout(this);
        sl.setOrientation(LinearLayout.VERTICAL);
        sl.setLayoutParams(LayoutParams);
        sl.setBackground(ContextCompat.getDrawable(this, R.drawable.layoutborder4));

        Button btn = new Button(this);
        PostTask task = new PostTask();
        JSONObject usergroup = new JSONObject();

        try{
            usergroup.put("WR_GROUP",WR_GROUP);
            userjson = usergroup.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        String[] params = {userjson};
        String result = task.execute(params).get();
        JSONArray jsonarray = new JSONArray(result);
        JSONObject jsonobj = jsonarray.getJSONObject(count);
        btn.setLayoutParams(btnParams);
        btn.setText(jsonobj.toString());
        btn.setBackgroundColor(Color.WHITE);
        btn.setGravity(Gravity.LEFT|Gravity.START);
        btn.setBackground(ContextCompat.getDrawable(this, R.drawable.layoutborder4));
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(boardActivity.this, ContentActivity.class);
            intent.putExtra("ID", WR_ID);
            intent.putExtra("TYPE", WR_TYPE);
            intent.putExtra("DATE", WR_DATE);
            intent.putExtra("CONTENT", WR_BODY);
            intent.putExtra("GROUP", BAIntent.getStringExtra("GROUP"));
            intent.putExtra("EMAIL", user_EMAIL);
            startActivity(intent);
        });


        TextView dateView = new TextView(this);
        dateView.setLayoutParams(tvParams);
        dateView.setGravity(Gravity.CENTER);
        dateView.setText("date");
        dateView.setPadding(ConvertDPtoPX(this,4),0,0,0);
        dateView.setBackground(ContextCompat.getDrawable(this, R.drawable.layoutborder4));

        sl.addView(btn);
        sl.addView(dateView);
        ll.addView(sl);
        }  catch(Exception e) {
            e.printStackTrace();
        }


    }

    // 동적 레이아웃에서 사이즈를 DP로 쓰기 위한 함수
    public static int ConvertDPtoPX(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}