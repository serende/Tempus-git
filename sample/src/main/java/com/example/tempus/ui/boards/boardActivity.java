package com.example.tempus.ui.boards;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.applandeo.Tempus.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class boardActivity extends AppCompatActivity {

    //TextView dateView;
   //Button contentView;
    //TextView groupTextView;

    String WR_ID;   //id data
    String WR_TYPE; //TYPE
    String WR_DATE; //DATE
    String WR_BODY; //content
    String WR_CONNUM; // content number
    String WR_GROUP;

    private String userjson,result,userfileName;

    Intent BAIntent;
    FloatingActionButton addFAB, friendFAB;

    int count = 0;
    String[] names = new String[30];
    Integer nameNum;

    LinearLayout ll;
    LinearLayout.LayoutParams params;

    String user_EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        BAIntent = getIntent();
        names = getIntent().getStringArrayExtra("names");
        nameNum = getIntent().getIntExtra("nameNum", 0);
        user_EMAIL = BAIntent.getStringExtra("EMAIL");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("게시판                             그룹: " + BAIntent.getStringExtra("GROUP"));

        /*
        dateView = findViewById(R.id.dateView);
        contentView = findViewById(R.id.contentView);
         */

        //groupTextView = findViewById(R.id.groupTextView);
        //groupTextView.setText("그룹: " + BAIntent.getStringExtra("GROUP"));

        WR_GROUP = BAIntent.getStringExtra("GROUP");
        PostTask task = new PostTask();
        task.execute();

        addFAB = findViewById(R.id.addFAB);
        addFAB.setOnClickListener(v -> {
            Intent intent = new Intent(boardActivity.this, WriteActivity.class);
            intent.putExtra("EMAIL", user_EMAIL);
            intent.putExtra("GROUP", BAIntent.getStringExtra("GROUP"));
            startActivity(intent);
        });

        // 멤버 등록 페이지로 이동
        friendFAB = findViewById(R.id.friendFAB);
        friendFAB.setOnClickListener(v -> {
            // TODO
            Intent intent = new Intent(boardActivity.this, InviteActivity.class);
            intent.putExtra("GROUP", BAIntent.getStringExtra("GROUP"));
            intent.putExtra("EMAIL", user_EMAIL);
            startActivity(intent);
        });

        /*
        // 글을 누르면 댓글을 쓸 수 있는 ContentActivity로 이동
        contentView.setOnClickListener(v -> {
            Intent intent = new Intent(boardActivity.this, ContentActivity.class);
            intent.putExtra("ID", WR_ID);
            intent.putExtra("TYPE", WR_TYPE);
            intent.putExtra("DATE", WR_DATE);
            intent.putExtra("CONTENT", WR_BODY);
            intent.putExtra("GROUP", BAIntent.getStringExtra("GROUP"));
            intent.putExtra("EMAIL", user_EMAIL);
            startActivity(intent);
        });

         */

        // 레이아웃 생성
        ll = findViewById(R.id.ll);
        params = new LinearLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT
                , Toolbar.LayoutParams.WRAP_CONTENT);

        try{
            for(int i = 0; i<9;i++){
                MakeLinearLayout(ll);
            }
        } catch(Exception e){Log.e("makeLinearerror", e.toString());}
    }

    private class PostTask extends AsyncTask<String, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        StringBuffer tempboardname = new StringBuffer();


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

        }
    }

    public void MakeLinearLayout(LinearLayout ll){
        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParams.weight = 1.0f;
        LayoutParams.gravity = Gravity.CENTER;

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertDPtoPX(this, 200));
        btnParams.gravity = Gravity.LEFT|Gravity.START;

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ConvertDPtoPX(this, 30));
        tvParams.gravity = Gravity.LEFT|Gravity.CENTER;

        LinearLayout.LayoutParams IVParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        IVParams.gravity = Gravity.CENTER;

        LinearLayout sl = new LinearLayout(this);
        sl.setOrientation(LinearLayout.VERTICAL);
        sl.setLayoutParams(LayoutParams);
        sl.setBackground(ContextCompat.getDrawable(this, R.drawable.layoutborder4));

        TextView btn = new TextView(this);
        btn.setLayoutParams(btnParams);
        btn.setText("contents");
        btn.setBackgroundColor(Color.WHITE);
        btn.setGravity(Gravity.LEFT|Gravity.START);
        btn.setBackground(ContextCompat.getDrawable(this, R.drawable.layoutborder4));
        /*
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(boardActivity.this, ContentActivity.class);
            intent.putExtra("ID", WR_ID);
            intent.putExtra("TYPE", WR_TYPE);
            intent.putExtra("DATE", WR_DATE);
            intent.putExtra("CONTENT", WR_BODY);
            intent.putExtra("GROUP", BAIntent.getStringExtra("GROUP"));
            intent.putExtra("EMAIL", user_EMAIL);
            startActivity(intent);
        });

         */

        LinearLayout ssl = new LinearLayout(this);
        ssl.setOrientation(LinearLayout.HORIZONTAL);
        ssl.setBackground(ContextCompat.getDrawable(this, R.drawable.layoutborder));

        TextView dateView = new TextView(this);
        dateView.setLayoutParams(tvParams);
        dateView.setGravity(Gravity.CENTER);
        dateView.setText("작성 일자: "+"date" + "                                    ");
        dateView.setPadding(ConvertDPtoPX(this,4),0,0,0);
        //dateView.setBackground(ContextCompat.getDrawable(this, R.drawable.layoutborder));

        TextView nameView = new TextView(this);
        nameView.setText("작성자: "+"NAME");
        nameView.setGravity(Gravity.CENTER);
        nameView.setLayoutParams(tvParams);
        //nameView.setBackground(ContextCompat.getDrawable(this, R.drawable.layoutborder));

        ssl.addView(dateView);
        ssl.addView(nameView);

        sl.addView(btn);
        sl.addView(ssl);

        // 첨부된 사진이 있으면 생성하도록 변경 필요(현재는 임시 조건문)
        int i=0;
        if(i==1){
            TextView tv = new TextView(this);
            tv.setText("첨부된 사진");
            tv.setLayoutParams(tvParams);
            tv.setGravity(Gravity.CENTER);

            ImageView IV = new ImageView(this);
            IV.setLayoutParams(IVParams);

            JSONObject fileName = new JSONObject();
            try{
                fileName.put("name",BAIntent.getStringExtra("GROUP"));
                userfileName = fileName.toString();
            }catch (JSONException e){
                e.printStackTrace();
            }
            String[] params2 = {userfileName};
            boardActivity.ImageLoadTask task2 = new boardActivity.ImageLoadTask("http://192.168.0.3:5000/imgdownload",IV);//Imageview(IB)에 해당 url에서 이미지를 받아 넣음
//          ImageLoadTask task2 = new ImageLoadTask("https://webhook.site/2e08c0c3-79dc-4f65-bba8-3cba6718f78f",IB);
            task2.execute(params2);
            IV.setScaleType(ImageView.ScaleType.FIT_CENTER);
            byte[] byteArray = BAIntent.getByteArrayExtra("image");

            sl.addView(tv);
            sl.addView(IV);
        }

        ll.addView(sl);
    }

    // 동적 레이아웃에서 사이즈를 DP로 쓰기 위한 함수
    public static int ConvertDPtoPX(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

        private String urlStr;
        private ImageView imageView;

        HttpURLConnection urlConnection = null;
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
        protected Bitmap doInBackground(String... params) {
            String fileName = params[0];
            Bitmap bitmap = null;
            try{
                //새로운 비트맵 객체를 만들기 전에 해시테이블 안에 동일한 주소를 요청하는 경우에 이전에 만들어졌던 비트맵 객체를 메모리에서 해제
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(15*1000);//Timeout setting
                conn.setRequestProperty("Content-Type", "application/json");//Request body 전달시 json 형태로 전달
                conn.setRequestMethod("GET");//보내는 데이터 형태는 post로 응답
                conn.setDoOutput(true);//서버로 응답을 보냄
                conn.setDoInput(true);//서버로부터 응답을 받음
                conn.connect();
                OutputStreamWriter streamWriter = new OutputStreamWriter(conn.getOutputStream());
                streamWriter.write(fileName);//Request body에 json data 세팅
                streamWriter.flush();//json data 입력후 저장
                streamWriter.close();
                conn.getResponseCode();
                if(bitmapHashMap.containsKey(urlStr)){
                    Bitmap oldBitmap = bitmapHashMap.remove(urlStr);
                    if(oldBitmap != null){
                        oldBitmap.recycle();
                        oldBitmap = null;
                    }
                }
//                conn.getResponseCode();
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                bitmapHashMap.put(urlStr, bitmap);


//                Toast.makeText(BoardMainActivity.this, "", Toast.LENGTH_SHORT).show();
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