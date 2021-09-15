package com.example.tempus.ui.boards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applandeo.Tempus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShoppingActivity extends AppCompatActivity {

    // 제품명 입력란
    EditText nameEdit;
    // 검색 버튼
    Button searchBtn;
    // 검색 결과를 보여줄 레이아웃
    LinearLayout ll;

    // 제품 이름, 가격, 링크(임시값)
    String productName;
    int productPrice = 2000;
    String productURL = "https://www.naver.com/";
    String json;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        nameEdit = findViewById(R.id.nameEdit);
        searchBtn = findViewById(R.id.searchBtn);
        ll = findViewById(R.id.ll);

        searchBtn.setOnClickListener(v->{
            // 서버로 nameEdit.getText().toString() 을 전달하고 정보를 받아오는 코드
            productName = nameEdit.getText().toString();
            // 기존에 존재하던 ll 내부의 view를 모두 제거
            ll.removeAllViews();

            try{
                for(int i = 0; i<5;i++)
                    count = i;
                    makeLinearLayout(ll);
            } catch(Exception e){
                Log.e("makeLinearerror", e.toString());}
        });
    }

    public void makeLinearLayout(LinearLayout ll){
        try {
            LinearLayout sl = new LinearLayout(this);
            sl.setOrientation(LinearLayout.HORIZONTAL);
            sl.setPadding(0, ConvertDPtoPX(this, 10), 0, 0);
            ShoppingTask Task = new ShoppingTask();
            JSONObject prdname = new JSONObject();

            try {
                prdname.put("prdname", productName);
                json = prdname.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String[] params = {json};
            String result = Task.execute(params).get();
            JSONArray jsonarray = new JSONArray(result);
            JSONObject jsonobj = jsonarray.getJSONObject(count);
            String result_productName =  jsonobj.getString("name");
            String result_price = jsonobj.getString("price");
            String result_link = jsonobj.getString("link");
            TextView pName = new TextView(this);

            // 서버에서 productName과 productPrice에 제품명, 가격을 넣는 코드

            pName.setText(result_productName + "       가격 : " + result_price + "원      ");
            pName.setTextSize(ConvertDPtoPX(this, 7));
            pName.setTextColor(Color.BLACK);
            pName.setPadding(ConvertDPtoPX(this, 20), 0, 0, 0);

            Button URLBtn = new Button(this);

            // 서버에서 productURL에 제품 판매 링크를 넣는 코드

            URLBtn.setText(result_link);
            URLBtn.setBackgroundColor(Color.WHITE);
            URLBtn.setTextColor(Color.BLUE);
            URLBtn.setTextSize(ConvertDPtoPX(this, 7));
            URLBtn.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(productURL));
                startActivity(intent);
            });

            sl.addView(pName);
            sl.addView(URLBtn);
            ll.addView(sl);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // 동적 레이아웃에서 사이즈를 DP로 쓰기 위한 함수
    public static int ConvertDPtoPX(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private class ShoppingTask extends AsyncTask<String, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";



        @Override
        protected String doInBackground(String... params) {
            String prdname = params[0];
            try {
                String site_url_json = "http://192.168.0.3:5000/productRecommendation";
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
                streamWriter.write(prdname);
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
}