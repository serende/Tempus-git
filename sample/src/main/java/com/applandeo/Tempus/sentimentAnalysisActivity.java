package com.applandeo.Tempus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class sentimentAnalysisActivity extends AppCompatActivity {
    private EditText inputView;
    private TextView resultview;
    String input;
    String json,result;
    BufferedReader reader = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentiment_analysis);

        inputView = (EditText) findViewById(R.id.signup_name);
        resultview = (TextView) findViewById(R.id.SAresult);
        Button SignupButton = (Button) findViewById(R.id.signup_button); // signup button
        SignupButton.setOnClickListener(v ->{
            input = inputView.getText().toString();
            JSONObject data = new JSONObject();
            try {
                data.put("input", input);

                json = data.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String[] params = {json};
                sentimentAnalysisActivity.SATask SA = new sentimentAnalysisActivity.SATask();
                SA.execute(params);



        });

    }
    private class SATask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            String input = params[0];
            try {
                String host_url = "http://192.168.0.3:5000/sentimentAnalysis";
                URL url = new URL(host_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(15*1000);//Timeout setting
                conn.setRequestProperty("Content-Type", "application/json");//Request body 전달시 json 형태로 전달
                conn.setRequestMethod("POST");//보내는 데이터 형태는 post로 응답
                conn.setDoOutput(true);//서버로 응답을 보냄
                conn.setDoInput(true);//서버로부터 응답을 받음
                conn.connect();
                OutputStreamWriter streamWriter = new OutputStreamWriter(conn.getOutputStream());
                streamWriter.write(input);//Request body에 json data 세팅
                streamWriter.flush();//json data 입력후 저장
                streamWriter.close();
                InputStream inputStream = conn.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                result = buffer.toString();
                int responsecode = conn.getResponseCode();//http 응답코드 송신


            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            Toast.makeText(sentimentAnalysisActivity.this, str, Toast.LENGTH_SHORT).show();

        }
    }
}