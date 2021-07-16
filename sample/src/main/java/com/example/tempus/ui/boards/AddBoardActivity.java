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
    Button finButton;
    EditText productNameEdit;
    ImageButton imageButton;
    ImageButton addFriends;
    EditText memoEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_board);

        finButton = findViewById(R.id.finButton);
        productNameEdit = findViewById(R.id.productNameEdit);
        imageButton = findViewById(R.id.imageButton);
        addFriends = findViewById(R.id.addFriends);
        memoEdit = findViewById(R.id.memoEdit);
    }
}