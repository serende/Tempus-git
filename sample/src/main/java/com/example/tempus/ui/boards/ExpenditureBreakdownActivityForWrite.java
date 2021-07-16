package com.example.tempus.ui.boards;

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

//import com.example.tempus.R;

public class ExpenditureBreakdownActivityForWrite extends AppCompatActivity {

    TextView dayView;

    EditText productNameEdit;
    EditText priceEdit;
    EditText purchaseDateEdit;
    EditText tagEdit;
    EditText memoEdit;

    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure_breakdown);

        dayView = findViewById(R.id.dayView);

        Button changeDisplay = findViewById(R.id.changeDisplay);
        changeDisplay.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
            startActivity(intent);
        });

        Button finButton = findViewById(R.id.finButton);
        finButton.setOnClickListener(view -> {
            // TODO
        });

        productNameEdit = findViewById(R.id.productNameEdit);
        priceEdit = findViewById(R.id.priceEdit);
        purchaseDateEdit = findViewById(R.id.purchaseDateEdit);
        tagEdit = findViewById(R.id.tagEdit);
        memoEdit = findViewById(R.id.memoEdit);

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
    }

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = (radioGroup, i) -> {
        if(i == R.id.allShareRadioButton){
            Toast.makeText(ExpenditureBreakdownActivityForWrite.this, "지인 모두와 공유", Toast.LENGTH_SHORT).show();
        }
        else if(i == R.id.partShareRadioButton){
            Toast.makeText(ExpenditureBreakdownActivityForWrite.this, "특정 그룹과 공유", Toast.LENGTH_SHORT).show();
        }
        else if(i == R.id.nonShareRadioButton){
            Toast.makeText(ExpenditureBreakdownActivityForWrite.this, "공유하지 않음", Toast.LENGTH_SHORT).show();
        }
    };
}