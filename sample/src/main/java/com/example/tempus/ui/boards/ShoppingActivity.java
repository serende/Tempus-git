package com.example.tempus.ui.boards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applandeo.Tempus.R;

public class ShoppingActivity extends AppCompatActivity {

    // 제품명 입력란
    EditText nameEdit;
    // 검색 버튼
    Button searchBtn;
    // 검색 결과를 보여줄 레이아웃
    LinearLayout ll;

    // 제품 이름, 가격, 링크(임시값)
    String productName = "사과";
    int productPrice = 2000;
    String productURL = "https://www.naver.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        nameEdit = findViewById(R.id.nameEdit);
        searchBtn = findViewById(R.id.searchBtn);
        ll = findViewById(R.id.ll);

        searchBtn.setOnClickListener(v->{
            // 서버로 nameEdit.getText().toString() 을 전달하고 정보를 받아오는 코드

            // 기존에 존재하던 ll 내부의 view를 모두 제거
            ll.removeAllViews();

            try{
                for(int i = 0; i<12;i++)        // 현재 폰에서 최대로 띄울 수 있는 수가 12개
                    makeLinearLayout(ll);
            } catch(Exception e){
                Log.e("makeLinearerror", e.toString());}
        });
    }

    public void makeLinearLayout(LinearLayout ll){
        LinearLayout sl = new LinearLayout(this);
        sl.setOrientation(LinearLayout.HORIZONTAL);
        sl.setPadding(0,ConvertDPtoPX(this, 10),0,0);

        TextView pName = new TextView(this);

        // 서버에서 productName과 productPrice에 제품명, 가격을 넣는 코드

        pName.setText(productName + "       가격 : " + productPrice + "원      ");
        pName.setTextSize(ConvertDPtoPX(this, 7));
        pName.setTextColor(Color.BLACK);
        pName.setPadding(ConvertDPtoPX(this, 20), 0,0,0);

        Button URLBtn = new Button(this);

        // 서버에서 productURL에 제품 판매 링크를 넣는 코드

        URLBtn.setText("링크");
        URLBtn.setBackgroundColor(Color.WHITE);
        URLBtn.setTextColor(Color.BLUE);
        URLBtn.setTextSize(ConvertDPtoPX(this, 7));
        URLBtn.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(productURL));
            startActivity(intent);
        });

        sl.addView(pName);
        sl.addView(URLBtn);
        ll.addView(sl);
    }

    // 동적 레이아웃에서 사이즈를 DP로 쓰기 위한 함수
    public static int ConvertDPtoPX(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}