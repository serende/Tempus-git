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

public class CreateExpenditureHistoryActivityForWrite extends AppCompatActivity {

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
        setContentView(R.layout.activity_create_expenditure_history_for_write);

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
            Toast.makeText(CreateExpenditureHistoryActivityForWrite.this, "지인 모두와 공유", Toast.LENGTH_SHORT).show();
        }
        else if(i == R.id.partShareRadioButton){
            Toast.makeText(CreateExpenditureHistoryActivityForWrite.this, "특정 그룹과 공유", Toast.LENGTH_SHORT).show();
        }
        else if(i == R.id.nonShareRadioButton){
            Toast.makeText(CreateExpenditureHistoryActivityForWrite.this, "공유하지 않음", Toast.LENGTH_SHORT).show();
        }
    };
}