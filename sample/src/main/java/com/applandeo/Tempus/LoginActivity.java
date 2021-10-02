package com.applandeo.Tempus;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.core.app.ActivityCompat;

import com.example.tempus.ui.boards.BoardMainActivity;
import com.example.tempus.ui.friends.EnteringInformationOfFriendActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.applandeo.Tempus.SaveSharedPreference.*;

public class LoginActivity extends Activity {

    // UI references.

    final static String FilePath= "/data/data/com.applandeo.materialcalendarsampleapp/files/check.txt";

    private EditText mEmailView;
    private EditText mPasswordView;
    String email,password;
    String userjson,result;
    BufferedReader reader = null;

    String checkNum = "1";

    String host_ip= "http://192.168.43.226:5000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.

        mEmailView = findViewById(R.id.username);
        mPasswordView = findViewById(R.id.password);

        Button loginInButton = findViewById(R.id.login); // login button
        Button SignUpButton = findViewById(R.id.sign_up); // sign up button

        // 자동 로그인, 입력된 이메일 정보를 저장
        //SaveSharedPreference.setUserName(LoginActivity.this, mEmailView.getText().toString());

        // SharedPreference로 저장된 파일에 저장된 계정이 있으면 boardmain으로 이동
        try{
            if(getUserName().length() != 0) {
                // Call BoardMainActivity
                Intent intent = new Intent(LoginActivity.this, BoardMainActivity.class);
                intent.putExtra("EMAIL", getUserName());
                intent.putExtra("host_ip", host_ip);
                startActivity(intent);
                this.finish();
            }
        } catch(Exception e){
            Log.e("SPERROR", e.toString());
        }


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

        SignUpButton.setOnClickListener(v -> {
            //회원 가입 페이지로 이동
            Intent intent = new Intent(this, SignupActivity.class);//임시 페이지
            intent.putExtra("host_ip", host_ip);
            startActivity(intent);
        });

        mEmailView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if(id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL){
                return true;
            }
            return false;
        });

        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                //attemptLogin();

                return true;
            }
            return false;
        });

        try{
            checkPermission();
        }
        catch (Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("CPERROR", exceptionAsStrting);

            e.printStackTrace();
        }
    }
    private class loginTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            //String hjson = params[0];
            String userdata = params[0];
            try {
                String host_url = host_ip+"login";
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
                Toast.makeText(LoginActivity.this, "이메일 또는 비밀번호 오류", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                setUserName(LoginActivity.this, mEmailView.getText().toString());   // userName을 SharedPreference로 저장
                Intent intent = new Intent(LoginActivity.this, BoardMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // 상위 스택 액티비티 모두 제거
                intent.putExtra("EMAIL", mEmailView.getText().toString());
                intent.putExtra("host_ip", host_ip);
//                Toast.makeText(getApplicationContext(), mEmailView.getText().toString(), Toast.LENGTH_SHORT).show();

                LoginActivity.this.finish();
                startActivity(intent);
            }
        }
    }

    public void checkPermission(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}