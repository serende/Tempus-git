package com.example.tempus.ui.addSchedule;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.Tempus.R;
import com.example.tempus.ui.boards.WriteActivity;

//import com.example.tempus.ui.R;

public class expenditureBreakdownActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure_breakdown);

        TextView dayView = (TextView) findViewById(R.id.dayView);

        Button changeDisplay = (Button) findViewById(R.id.changeDisplay);
        changeDisplay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), SetScheduleActivity.class);
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

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
    }

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(i == R.id.allShareRadioButton){
                Toast.makeText(expenditureBreakdownActivity.this, "지인 모두와 공유", Toast.LENGTH_SHORT).show();
            }
            else if(i == R.id.partShareRadioButton){
                Toast.makeText(expenditureBreakdownActivity.this, "특정 그룹과 공유", Toast.LENGTH_SHORT).show();
            }
            else if(i == R.id.nonShareRadioButton){
                Toast.makeText(expenditureBreakdownActivity.this, "공유하지 않음", Toast.LENGTH_SHORT).show();
            }
        }
    };
}