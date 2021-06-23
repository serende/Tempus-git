package com.example.tempus.ui.friends;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.applandeo.Tempus.R;

public class AddFriendsActivity extends AppCompatActivity {

    String name;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        name = "";
        number = "";

        Button registerWithPhoneNumberButton = findViewById(R.id.registerWithPhoneNumberButton);
        registerWithPhoneNumberButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EnteringInformationOfFriendActivity.class);
            startActivity(intent);
        });

        Button registerWithAddressBookButton = findViewById(R.id.registerWithAddressBookButton);
        registerWithAddressBookButton.setOnClickListener(v -> {
            // 연락처 선택 화면
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(intent, 0);
        });
    }

    public void setName(String name){
        this.name = name;
    }
    public void setNumber(String number){
        this.number = number;
    }
    public String getName(){
        return name;
    }
    public String getNumber(){
        return number;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(0);     // 0 -> 이름
                        number = cursor.getString(1);   // 1 -> 번호

                        Log.i("afaconfirm", "name = " +name + ", number = " + number);
                    } while (cursor.moveToNext());
                }
                Toast nameToast = Toast.makeText(this.getApplicationContext(), name + "을(를) 불러오는데 성공했습니다.", Toast.LENGTH_SHORT);
                nameToast.show();

                cursor.close();

                Intent afIntent = new Intent(AddFriendsActivity.this, EnteringInformationOfFriendActivity.class);
                afIntent.putExtra("지인명", name);
                afIntent.putExtra("전화번호", number);

                startActivity(afIntent);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    
}