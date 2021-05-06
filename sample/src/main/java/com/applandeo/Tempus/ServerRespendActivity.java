package com.applandeo.Tempus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class ServerRespendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respend);


        ParseTask test = new ParseTask();//네트워크는 스레드를 사용하여 백 그라운드에서 작업해야함, 안그럼 NetworkOnMainThreadException 발생
        test.execute();


    }
    private class ParseTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... params) {
            try {
                JSONObject jsonob = new JSONObject();
                JSONArray jsonarr = new JSONArray();
                jsonarr = new JSONArray();

                for (int i = 1; i < 3; i++) {
                    JSONObject data = new JSONObject();

                    data.put("이름", "인간_" + i);
                    data.put("나이", 10 + i);
                    data.put("주소", "서울 중앙로" + i + "길");
                    //jsonarr.add(data); put으로 대체됨
                    jsonarr.put(data);
                }

                jsonob.put("사람들", jsonarr);
                TextView textView = (TextView) findViewById(R.id.respend);
                textView.setText(jsonob.toString());
                String json = jsonob.toString();

                try {
                    String host_url = "https://webhook.site/3e806cef-cad6-4542-982d-6064843cfcf2";
                    URL url = new URL(host_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(15*1000);//Timeout setting
                    conn.setRequestProperty("Content-Type", "application/json");//Request body 전달시 json 형태로 전달
                    conn.setRequestMethod("POST");//보내는 데이터 형태는 post로 응답
                    conn.setDoOutput(true);//서버로 응답을 보냄
                    conn.setDoInput(true);//서버로부터 응답을 받음
                    conn.connect();
                    OutputStreamWriter streamWriter = new OutputStreamWriter(conn.getOutputStream());
                    streamWriter.write(json);//Request body에 json data 세팅
                    streamWriter.flush();//json data 입력후 저장
                    streamWriter.close();//
                    int responsecode = conn.getResponseCode();//http 응답코드 송신
//                    conn.setRequestMethod("POST"); // 보내는 타입
//                    conn.setRequestProperty("Content-Type", "application/json");
//                    conn.setDoOutput(true);
                    //OutputStream osw = conn.getOutputStream();
//                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//                    bw.write(String.valueOf(json.getBytes("UTF-8")));
//                    bw.flush();
//                    bw.close();
//                    DataOutputStream lo = new DataOutputStream(conn.getOutputStream());
//                      lo.writeBytes(json);
//                      lo.flush();
//                      lo.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }catch (Exception e){
                    e.printStackTrace();
                }
            return null;
        }
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

        }

    }



}