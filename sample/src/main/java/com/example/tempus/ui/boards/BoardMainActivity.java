package com.example.tempus.ui.boards;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.applandeo.Tempus.R;
import com.example.tempus.ui.MyService;
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
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BoardMainActivity extends AppCompatActivity {

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;

    private FloatingActionButton openFAB, addBoardFAB, friendFAB, shoppingFAB, notifyONFAB, notifyOFFFAB;
    private ImageButton imageButton1, imageButton2;
    private String id = "kim";
    private String userjson,result;

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "boundary=----WebKitFormBoundarylLEkUd8JSJOasqs0";
    String user_id = "test";

    GridLayout grid;
    Intent BMAIntent = getIntent();

    int notifyNum = 0;  // 알림 켜진지 꺼진지

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
        imageButton2 = findViewById(R.id.imageButton2);
        ImageLoadTask task2 = new ImageLoadTask("http://192.168.0.3:5000/imgdownload",imageButton2);
        task2.execute();
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
            notifyONFAB = findViewById(R.id.notifyONFAB);
            notifyOFFFAB = findViewById(R.id.notifyOFFFAB);
            imageButton1 = findViewById(R.id.imageButton1);
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

        // 현재 알림 버튼을 눌러야 알림이 시작 되는데 버튼을 누르기 전에도 알림이 시작된 상태로 돼있도록 할 방법을 찾아야 함
        notifyONFAB.setOnClickListener(v -> {
            anim();
            Toast.makeText(getApplicationContext(),"알림 ON",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BoardMainActivity.this, MyService.class);
            startService(intent);
        });

        notifyOFFFAB.setOnClickListener(v -> {
            anim();
            Toast.makeText(getApplicationContext(),"알림 OFF",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BoardMainActivity.this,MyService.class);
            stopService(intent);
        });

        imageButton1.setOnClickListener(view -> {
            Intent baIntent = new Intent(getApplicationContext(), boardActivity.class);
            baIntent.putExtra("그룹명", "2팀 게시판");
            startActivity(baIntent);
        });

        imageButton2.setOnClickListener(view -> {
            Intent baIntent2 = new Intent(getApplicationContext(), boardActivity.class);
            baIntent2.putExtra("그룹명", "우리 가족방");
            startActivity(baIntent2);
        });

        try{
            makeLinearLayout(grid);
        } catch(Exception e){}
    }

    public void anim() {
        if(isFabOpen) {
            try{
                addBoardFAB.startAnimation(fab_close);
                friendFAB.startAnimation(fab_close);
                shoppingFAB.startAnimation(fab_close);
                notifyONFAB.startAnimation(fab_close);
                notifyOFFFAB.startAnimation(fab_close);

                addBoardFAB.setClickable(false);
                friendFAB.setClickable(false);
                shoppingFAB.setClickable(false);
                notifyONFAB.setClickable(false);
                notifyOFFFAB.setClickable(false);

                isFabOpen = false;
            } catch (Exception e){
                Log.e("animERROR", e.toString());
            }

        } else {
            try{
                addBoardFAB.startAnimation(fab_open);
                friendFAB.startAnimation(fab_open);
                shoppingFAB.startAnimation(fab_open);
                notifyONFAB.startAnimation(fab_open);
                notifyOFFFAB.startAnimation(fab_open);

                addBoardFAB.setClickable(true);
                friendFAB.setClickable(true);
                shoppingFAB.setClickable(true);
                notifyONFAB.setClickable(true);
                notifyOFFFAB.setClickable(true);

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

    // 그리드 레이아웃 내에 리니어레이아웃을 생성할 함수
    public void makeLinearLayout(GridLayout gl){
        LinearLayout sl = new LinearLayout(this);
        sl.setOrientation(LinearLayout.VERTICAL);

        // addBoard에서 전달 받은 이미지 또는 서버에서 전달받은 이미지를 보여주며, board액티비티로 이동시키는 버튼
        ImageButton IB = new ImageButton(this);
        byte[] byteArray = BMAIntent.getByteArrayExtra("image");
        IB.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));

        // addBoard에서 전달 받은 게시판명을 텍스트뷰로 세팅
        TextView BoardText = new TextView(this);
        BoardText.setText(BMAIntent.getStringExtra("boardName"));

        IB.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), boardActivity.class);
            intent.putExtra("GROUP", BMAIntent.getStringExtra("boardName"));
            startActivity(intent);
        });

        sl.addView(IB);
        sl.addView(BoardText);

        gl.addView(sl);
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String urlStr;
        private ImageView imageView;
        //HashMap 객체를 만들고 이미지의 주소를 메모리에 만들어진 비트맵 객체와 매핑
        private HashMap<String, Bitmap> bitmapHashMap = new HashMap<String, Bitmap>();

        public ImageLoadTask(String urlStr ,ImageView imageView) {
            this.urlStr = urlStr;
            this.imageView = imageView;

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //웹서버의 이미지 데이터를 받아 비트맵 객체로 만들어줌
        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = null;
            try{
                //새로운 비트맵 객체를 만들기 전에 해시테이블 안에 동일한 주소를 요청하는 경우에 이전에 만들어졌던 비트맵 객체를 메모리에서 해제
                if(bitmapHashMap.containsKey(urlStr)){
                    Bitmap oldBitmap = bitmapHashMap.remove(urlStr);
                    if(oldBitmap != null){
                        oldBitmap.recycle();
                        oldBitmap = null;
                    }
                }
                URL url = new URL(urlStr);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                bitmapHashMap.put(urlStr, bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
            //여기서 return 하면 onPostExcute에 넘어감
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        //비트맵 객체로 변환하고 나면 메인 스레드에서 이미지뷰에 표시
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            imageView.setImageBitmap(bitmap);
            imageView.invalidate();
        }
    }
}