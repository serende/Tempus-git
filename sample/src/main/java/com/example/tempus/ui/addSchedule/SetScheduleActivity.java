package com.example.tempus.ui.addSchedule;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.Tempus.R;
import com.example.tempus.ui.boards.WriteActivity;

//import com.example.tempus.R;

public class SetScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule);

        TextView dayView = (TextView) findViewById(R.id.dayView);
        dayView.setText("");    // print eventDay

        EditText editText = (EditText) findViewById(R.id.editText);

        Button changeDisplay = (Button) findViewById(R.id.changeDisplay);
        changeDisplay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), expenditureBreakdownActivity.class);
                startActivity(intent);
            }
        });

        Button finButton = (Button) findViewById(R.id.finButton);
        finButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // finish adding date
            }
        });

        Button startTime = (Button) findViewById(R.id.startTime);
        startTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // time-setting pop-up view
            }
        });

        Button endTime = (Button) findViewById(R.id.endTime);
        endTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // time-setting pop-up view
            }
        });

        Button details = (Button) findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // memo pop-up view
            }
        });

        Button addFile = (Button) findViewById(R.id.addFile);
        addFile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // choose and add file
            }
        });

        Switch allDaySwitch = (Switch) findViewById(R.id.allDaySwitch);
        allDaySwitch.setOnCheckedChangeListener(new allDaySwitchListener());

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
    }

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(i == R.id.allShareRadioButton){
                Toast.makeText(SetScheduleActivity.this, "지인 모두와 공유", Toast.LENGTH_SHORT).show();
            }
            else if(i == R.id.partShareRadioButton){
                Toast.makeText(SetScheduleActivity.this, "특정 그룹과 공유", Toast.LENGTH_SHORT).show();
            }
            else if(i == R.id.nonShareRadioButton){
                Toast.makeText(SetScheduleActivity.this, "공유하지 않음", Toast.LENGTH_SHORT).show();
            }
        }
    };
}

class allDaySwitchListener implements CompoundButton.OnCheckedChangeListener{
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
            // all day
            ;
        else
            // just in time
            ;
    }
}