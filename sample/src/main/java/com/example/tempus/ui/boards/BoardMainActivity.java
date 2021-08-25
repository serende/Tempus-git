package com.example.tempus.ui.boards;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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
import android.os.AsyncTask;
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
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BoardMainActivity extends AppCompatActivity {

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;

    private FloatingActionButton openFAB, addBoardFAB, friendFAB, shoppingFAB;
    private ImageButton imageButton1, imageButton2;
    private String id = "Lee";
    private String userjson,result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_main);
        JSONObject useremail = new JSONObject();
        try{
            useremail.put("email",id);
            userjson = useremail.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        String[] params = {userjson};
        boardTask task = new boardTask();
        task.execute(params);//스레드 실행 함수
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("게시판");

        FloatingActionButton openFAB = findViewById(R.id.openFAB);
        openFAB.setOnClickListener(v -> {
            anim();
        });

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
            Intent baIntent = new Intent(getApplicationContext(), boardActivity.class);
            baIntent.putExtra("그룹명", "2팀 게시판");
            startActivity(baIntent);
        });

        imageButton2 = findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(view -> {
            Intent baIntent2 = new Intent(getApplicationContext(), boardActivity.class);
            baIntent2.putExtra("그룹명", "우리 가족방");
            startActivity(baIntent2);
        });
    }

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
    private class boardTask extends AsyncTask<String, Void, String> {
        // 비동기 작업 라이브러리 AsyncTask사용, 필요에 따른 스레드 생성후 메인 스레드와 상호 작용한 뒤 종료
        /*execute -> doInBackground -> onProgressUpdate -> onPostExecute
         * execute: 작업준비 및 시작
         * doInBackground: 백그라운드 스레드에서 비동기 작업 실행
         * onProgressUpdate: 백그라운드 스레드 진행상황을 메인 스레드로 전달(진행중)
         * onPostExecute: 백그라운드 스레드 종료후 진행상황을 메인 스레드에 전달(완료후)
         * */
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(String... params) {
            try {

                //String site_url_json = "http://273fb4d10f83.ngrok.io/post";
                String site_url_json = "http://192.168.0.3:5000/board";
                //ngrok http 8000 -host-header="localhost:8000"
                //로컬 주소를 0.0.0.0으로 세팅하는 방법을 통해 해결이 가능할 것으로도 예상하나 모든 호스트에 대해서 접근을 허용하기 때문에 보안상 문제가 발생함(5/4)
                //웹서버 주소에 따른 기능 변환
                URL url = new URL(site_url_json);

                urlConnection = (HttpURLConnection) url.openConnection();//HttpURLConnection 객체를 생성하여 openConnection 메소드로 url 연결
                urlConnection.setRequestMethod("GET");//웹서버에 대한 요청 옵션
                //urlConnection.setRequestProperty("accept", "application/json");
                /*
                 * HEAD: 문서의 헤더정보 요청
                 * GET: 웹서버로부터 리소스를 가져옴
                 * POST: 폼에 입력된 내용을 서버로 전송
                 * DELETE: 웹서버의 리소스를 지움, 대부분의 서버는 허용하지 않으며 인증을 요청할 수 있음
                 * put: 웹서버에 대해 리소스를 전달
                 * OPTIONS: 특정 URL에 대해 지원되는 요청 메소드의 목록을 리턴, 요청 URL이 *일 때 서버 전체에 적용
                 * TRACE: 요청을 추적
                 * */
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();//데이터를 받기위해 Stream 변경
                StringBuffer buffer = new StringBuffer();//데이터를 받을 버퍼

                reader = new BufferedReader(new InputStreamReader(inputStream));// reader에 데이터를 받아옴

                String line;//버퍼에 넣게 도와줄 문자열 변수
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }// reader(배열)에서 지정된 번호에 따른 데이터를 한줄 한줄 읽어 null값이 아니라면 buffer에 저장

                resultJson = buffer.toString();//버퍼를 string으로 변환하여 보냄

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }


        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            try {
                JSONArray jsonarray = new JSONArray(strJson);//resultJson을 Json 형태로 받음

                JSONObject jsonobj = jsonarray.getJSONObject(0);//json데이터배열의 index번호

                /*
                 * Json 파일 구성(예시): Author, Title, Text로 구성
                 * 변경 가능(현재 txt 파일에 저장하는 형식이라 Response에서 json데이터 양식을 다르게 하여 보내고 txt 파일을 reset하면 됨)
                 * */

                String main_json_text =  jsonobj.getString("text");//ex3
                String result_json_text = main_json_text;//ex4
                // json 데이터 name, 즉 json은 key값과 data값으로 구성된 배열인데 key값을 입력하면 그에 따른 데이터 값을 받아옴
                Log.d("FOR_LOG", result_json_text);

                TextView textView = (TextView)findViewById(R.id.testtext);
                textView.setText(result_json_text);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    /*
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
                break;
            case R.id.changeDisplayCalendar:
                break;
            case R.id.changeDisplaySlide:
                break;
            case R.id.friendsList:
                Intent FLintent = new Intent(getApplicationContext(), FriendListActivity.class);
                startActivity(FLintent);
                break;
            case R.id.registerFriends:
                Intent AFintent = new Intent(getApplicationContext(), AddFriendsActivity.class);
                startActivity(AFintent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
     */
}