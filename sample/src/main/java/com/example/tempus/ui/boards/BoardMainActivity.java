package com.example.tempus.ui.boards;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
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
    private String id = "kim";
    private String userjson,result;

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "boundary=----WebKitFormBoundarylLEkUd8JSJOasqs0";
    String user_id = "test";

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

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

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

            try {
                JSONArray jsonarray = new JSONArray(strJson);

                JSONObject jsonobj = jsonarray.getJSONObject(0);



                String result_json_text =  jsonobj.getString("text");//ex3


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
    public void HttpFileDownload(String urlString,String fileName) {
        try {
            FileInputStream mFileInputStream = new FileInputStream(fileName);
            URL connectUrl = new URL(urlString);
            Log.d("Test", "mFileInputStream  is " + mFileInputStream);

            // open connection
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("file", fileName);
            conn.setRequestProperty("user", user_id);
            conn.setRequestProperty("name", "file");
            conn.setRequestProperty("someParameter", "someValue");


            // write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

            Log.d("Test", "image byte is " + bytesRead);

            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.e("Test", "File is written");
            mFileInputStream.close();
            dos.flush(); // finish upload...

            // get response
            int ch;
            InputStream is = conn.getInputStream();
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String s = b.toString();
            Log.e("Test", "result = " + s);
            // 원본에서 EditText/TextView에 텍스트 설정하는 것으로 추정하여 주석처리
            // mEdityEntry.setText(s);
            dos.close();
            Toast.makeText(BoardMainActivity.this, "전송 완료", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            Toast.makeText(BoardMainActivity.this, "오류 메세지" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}