package com.example.tempus.ui.addSchedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.tempus.R;

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

        Switch sharingSwitch = (Switch) findViewById(R.id.sharingSwitch);
        sharingSwitch.setOnCheckedChangeListener(new sharingSwitchListener());
    }
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

class sharingSwitchListener implements CompoundButton.OnCheckedChangeListener{
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
            // share with the members who belong to group
            ;
        else
            // not share
            ;
    }
}