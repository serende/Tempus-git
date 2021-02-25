package com.example.tempus.ui.boards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.applandeo.Tempus.R;

//import com.example.tempus.R;

public class ExpenditureBreakdownActivityForWrite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure_breakdown);

        TextView dayView = (TextView) findViewById(R.id.dayView);

        Button changeDisplay = (Button) findViewById(R.id.changeDisplay);
        changeDisplay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(intent);
            }
        });

        Button finButton = (Button) findViewById(R.id.finButton);
        finButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // TODO
            }
        });

        EditText productNameEdit = (EditText) findViewById(R.id.productNameEdit);
        EditText priceEdit = (EditText) findViewById(R.id.priceEdit);
        EditText purchaseDateEdit = (EditText) findViewById(R.id.purchaseDateEdit);
        EditText tagEdit = (EditText) findViewById(R.id.tagEdit);
        EditText memoEdit = (EditText) findViewById(R.id.memoEdit);
    }
}