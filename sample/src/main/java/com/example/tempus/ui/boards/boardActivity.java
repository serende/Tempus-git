package com.example.tempus.ui.boards;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.applandeo.Tempus.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class boardActivity extends AppCompatActivity {

    TextView dateView;
    Button contentView;
    TextView groupTextView;

    String WR_ID;   //id data
    String WR_TYPE; //TYPE
    String WR_DATE; //DATE
    String WR_BODY; //content

    Intent BAIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        BAIntent = getIntent();

        dateView = findViewById(R.id.dateView);
        contentView = findViewById(R.id.contentView);

        groupTextView = findViewById(R.id.groupTextView);
        groupTextView.setText("그룹: " + BAIntent.getStringExtra("그룹명"));

        boardTask task = new boardTask();
        task.execute();//스레드 실행 함수, 서버 호출에 사용됨, 위치 변경시 오류 발생할 수 있음

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(boardActivity.this, WriteActivity.class);
            startActivity(intent);
        });

        // 글을 누르면 댓글을 쓸 수 있는 ContentActivity로 이동
        // 변수도 intent를 통해 전달하도록 변경 필요
        contentView.setOnClickListener(v -> {
            Intent CAIntent = new Intent(boardActivity.this, ContentActivity.class);
            CAIntent.putExtra("ID", WR_ID);
            CAIntent.putExtra("TYPE", WR_TYPE);
            CAIntent.putExtra("DATE", WR_DATE);
            CAIntent.putExtra("CONTENT", WR_BODY);
            CAIntent.putExtra("GROUP", BAIntent.getStringExtra("그룹명"));
            startActivity(CAIntent);
        });
    }

    private class boardTask extends AsyncTask<Void, Void, String> {
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
        protected String doInBackground(Void... params) {
            try {

                //String site_url_json = "http://273fb4d10f83.ngrok.io/post";
                String site_url_json = "http://192.168.43.226:5000/board";
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

            //쓰레드 종료
            try {
                JSONArray jsonarray = new JSONArray(strJson);//resultJson을 Json 형태로 받음

                for(int i=0;jsonarray.getJSONObject(i)!=null;i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);//json데이터배열의 index번호
                    /*해당 인덱스 번호를 통해 가져온 json데이터들의 배열에서 원하는 데이터만 가져올 수 있음, 인덱스 번호는 배열의 순서대로 매겨져 0부터 시작하며
                     * 데이터가 null일 경우를 for,while로 체크하여 모두 가져올 수 있음*/
                    //JSONObject jsonobj = jsonarray.getJSONObject(0);

                    //head_json.put("WR_ID",jsonobj.getString());
                    JSONObject head_json = (JSONObject) jsonobj.get("head");//head data만 따로 때어냄
                    JSONObject body_json = (JSONObject) jsonobj.get("body");// body data만 따로 때어냄

                    //JsonParser ps = new JsonParser();
                    WR_ID = head_json.getString("WR_ID");//id data
                    WR_TYPE = head_json.getString("WR_TYPE");//TYPE
                    WR_DATE = body_json.getString("WR_DATE");//DATE
                    WR_BODY = body_json.getString("WR_BODY");//Content
                    //데이터들 보기좋게 합쳐둔거
                    String result_json_text = "WR_ID: " + WR_ID + "\n" + "WR_TYPE: " + WR_TYPE + "\n" + "WR_DATE: " + WR_DATE + "\n" + "WR_BODY:" + WR_BODY;
                    // json 데이터 name, 즉 json은 key값과 data값으로 구성된 배열인데 key값을 입력하면 그에 따른 데이터 값을 받아옴
                    Log.d("FOR_LOG", result_json_text);

                    dateView.setText(WR_DATE);
                    contentView.setText(WR_BODY);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}