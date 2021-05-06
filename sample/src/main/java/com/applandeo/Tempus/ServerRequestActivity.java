package com.applandeo.Tempus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ServerRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        RequestTask task = new RequestTask();
        task.execute();//스레드 실행 함수
        /*execute -> doInBackground -> onProgressUpdate -> onPostExecute
        * execute: 작업준비 및 시작
        * doInBackground: 백그라운드 스레드에서 비동기 작업 실행
        * onProgressUpdate: 백그라운드 스레드 진행상황을 메인 스레드로 전달(진행중)
        * onPostExecute: 백그라운드 스레드 종료후 진행상황을 메인 스레드에 전달(완료후)
        * */

        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(v -> startActivity(new Intent(this, ServerRespendActivity.class)));

    }

    private class RequestTask extends AsyncTask<Void, Void, String> {
        // 비동기 작업 라이브러리 AsyncTask사용, 필요에 따른 스레드 생성후 메인 스레드와 상호 작용한 뒤 종료

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {

                //String site_url_json = "http://273fb4d10f83.ngrok.io/post";
                String site_url_json = "http://82df587cda36.ngrok.io/post";
                //ngrok http 8000 -host-header="localhost:8000"
                //로컬 주소를 0.0.0.0으로 세팅하는 방법을 통해 해결이 가능할 것으로도 예상하나 모든 호스트에 대해서 접근을 허용하기 때문에 보안상 문제가 발생함(5/4)
                //웹서버 주소에 따른 기능 변환
                URL url = new URL(site_url_json);

                urlConnection = (HttpURLConnection) url.openConnection();//HttpURLConnection 객체를 생성하여 openConnection 메소드로 url 연결
                urlConnection.setRequestMethod("GET");//웹서버에 대한 요청 옵션
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

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
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

            try {
                JSONArray jsonarray = new JSONArray(strJson);
                JSONObject jsonobj = jsonarray.getJSONObject(0);

                String result_json_text =  jsonobj.getString("text");
                Log.d("FOR_LOG", result_json_text);

                TextView textView = (TextView)findViewById(R.id.showtext);
                textView.setText(result_json_text);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}



