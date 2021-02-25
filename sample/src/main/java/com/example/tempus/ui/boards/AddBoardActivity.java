package com.example.tempus.ui.boards;

import android.os.Bundle;

import com.applandeo.Tempus.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

//import com.example.tempus.R;

public class AddBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_board);

        Button finButton = (Button) findViewById(R.id.finButton);
        EditText productNameEdit = (EditText) findViewById(R.id.productNameEdit);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        ImageButton addFriends = (ImageButton) findViewById(R.id.addFriends);
        EditText memoEdit = (EditText) findViewById(R.id.memoEdit);
    }
}