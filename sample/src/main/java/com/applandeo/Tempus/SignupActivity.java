package com.applandeo.Tempus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignupActivity extends AppCompatActivity {
    private EditText mNameView;
    private EditText mPhoneNumberView;
    private EditText mAddressView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPassword2View;
    String name,pnum,address,email,password,password2;
    String userjson,result;
    BufferedReader reader = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mNameView = (EditText) findViewById(R.id.signup_name);
        mPhoneNumberView = (EditText) findViewById(R.id.signup_phonenumber);
        mAddressView = (EditText) findViewById(R.id.signup_office);
        mEmailView = (EditText) findViewById(R.id.signup_email);
        mPasswordView = (EditText) findViewById(R.id.signup_password);
        mPassword2View = (EditText) findViewById(R.id.signup_password2);

        Button SignupButton = (Button) findViewById(R.id.signup_button); // signup button
        SignupButton.setOnClickListener(v ->{
            name = mNameView.getText().toString();
            pnum = mPhoneNumberView.getText().toString();
            address = mAddressView.getText().toString();
            email = mEmailView.getText().toString();
            password = mPasswordView.getText().toString();
            password2 = mPassword2View.getText().toString();
            if(password.equals(password2)==false){
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();


            }else {
                JSONObject userdata = new JSONObject();
                try {
                    userdata.put("name", name);
                    userdata.put("pnum", pnum);
                    userdata.put("address", address);
                    userdata.put("email", email);
                    userdata.put("password", password);
                    userjson = userdata.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String[] params = {userjson};
                signupTask Write = new signupTask();
                Write.execute(params);
            }


        });

    }
    private class signupTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            //String hjson = params[0];
            String userdata = params[0];
            try {
                String host_url = "http://192.168.43.226:5000/signup";
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
                if(str.equals("error")==true) {
                    Toast.makeText(SignupActivity.this, "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SignupActivity.this, "회원가입이 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

        }
    }
}