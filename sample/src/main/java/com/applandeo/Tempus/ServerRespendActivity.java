package com.applandeo.Tempus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerRespendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_respend);

//        ParseTask task = new ParseTask();
//        task.execute();//스레드 실행 함수
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {
        // 비동기 작업 라이브러리 AsyncTask사용, 필요에 따른 스레드 생성후 메인 스레드와 상호 작용한 뒤 종료

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {

                JSONObject jsonob = new JSONObject();
                JSONArray jsonarr = new JSONArray();

                for(int i =1;i<3;i++){
                    JSONObject data = new JSONObject();

                    data.put("이름", "인간_"+i);
                    data.put("나이", 10+i);
                    data.put("주소","서울 중앙로"+i+"길");
                    //jsonarr.add(data);
                    /*cannot be resolve method 'add' in JSONArray문제 발생(해결 필요 5/4)*/

                }

                //String site_url_json = "http://273fb4d10f83.ngrok.io/post";
                String site_url_json = "http://ba294d38fd60.ngrok.io/post";
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