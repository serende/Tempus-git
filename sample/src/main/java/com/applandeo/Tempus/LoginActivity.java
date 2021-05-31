package com.applandeo.Tempus;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends Activity {

    // UI references.

    private EditText mEmailView;
    private EditText mPasswordView;
    String email,password;
    String userjson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.

        mEmailView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button loginInButton = (Button) findViewById(R.id.login); // login button

        Button SignuppButton = (Button) findViewById(R.id.sign_up); // sign up button


        loginInButton.setOnClickListener(v -> {
            //서버 구축시 데이터 넘김
            email = mEmailView.getText().toString();
            password = mPasswordView.getText().toString();
            JSONObject userdata = new JSONObject();
            try{
                userdata.put("email",email);
                userdata.put("password",password);
                userjson = userdata.toString();
            }catch (JSONException e){
                e.printStackTrace();
            }
            String[] params = {userjson};
            loginTask Write = new loginTask();
            Write.execute(params);


        });

        SignuppButton.setOnClickListener(v -> {
            //회원 가입 페이지로 이동
            Intent Signintent = new Intent(this, SignupActivity.class);//임시 페이지
            startActivity(Signintent);
        });


        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if(id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL){
                    return true;
                }
                return false;
            }

        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    //attemptLogin();

                    return true;
                }
                return false;
            }
        });





    }
    private class loginTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            //String hjson = params[0];
            String userdata = params[0];
            try {
                String host_url = "http://192.168.0.3:5000/login";
                URL url = new URL(host_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(15*1000);//Timeout setting
                conn.setRequestProperty("Content-Type", "application/json");//Request body 전달시 json 형태로 전달
                conn.setRequestMethod("POST");//보내는 데이터 형태는 post로 응답
                conn.setDoOutput(true);//서버로 응답을 보냄
                conn.setDoInput(true);//서버로부터 응답을 받음
                conn.connect();
                OutputStreamWriter streamWriter = new OutputStreamWriter(conn.getOutputStream());
                streamWriter.write(userdata);//Request body에 json data 세팅
                streamWriter.flush();//json data 입력후 저장
                streamWriter.close();
                int responsecode = conn.getResponseCode();//http 응답코드 송신


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
        }
    }

}