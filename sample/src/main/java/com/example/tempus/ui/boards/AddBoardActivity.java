package com.example.tempus.ui.boards;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.applandeo.Tempus.LoginActivity;
import com.applandeo.Tempus.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

//import com.example.tempus.R;

public class AddBoardActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;

    Button finButton;
    EditText BoardNameEdit;
    ImageButton addPhoto;
    EditText memoEdit;
    ImageView userImage;

    String mCurrentPhotoPath;
    Uri imageUri;
    Uri photoURI, albumURI;

    String BoardName;
    String BoardMemo;

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "boundary=----WebKitFormBoundarylLEkUd8JSJOasqs0";
    String user_id = "admin";//로그인시 갱신되는 이메일 계정, 현재 테스트로 임시 정의하여 사용중
    String WR_ID,WR_BODY,boardjson;
    BufferedReader reader = null;
    String result;

    Intent ABAIntent;
    String user_EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_board);

        ABAIntent = getIntent();
        user_EMAIL = ABAIntent.getStringExtra("EMAIL");

        try{
            BoardNameEdit = findViewById(R.id.BoardNameEdit);
            addPhoto = findViewById(R.id.addPhoto);
            memoEdit = findViewById(R.id.memoEdit);
            finButton = findViewById(R.id.finButton);
        } catch(Exception e){
            Log.e("EditERROR", "findViewById ERROR");
        }


        BoardName = BoardNameEdit.getText().toString();

        addPhoto.setOnClickListener(v -> {
            // file search
            try{
                getAlbum();
            } catch(Exception e){
                Log.e("getAlbumError", e.toString());
            }

        });

        BoardMemo = memoEdit.getText().toString();

        finButton.setOnClickListener(v -> {
            // TODO

            // 이미지 전달 전 사이즈 조정
            WR_ID = BoardNameEdit.getText().toString();//인텐드 받은 이메일을 담을 변수
            WR_BODY = BoardNameEdit.getText().toString();//addboard내 게시판 제목을 담을 변수
            JSONObject userdata = new JSONObject();
            try{
                userdata.put("WR_ID",user_id);//임시로 인텐드 받을 이메일을 미리 선언하여 사용중
                userdata.put("WR_BODY",WR_BODY);
//                userdata.put("WR_CONTENT",memoEdit);//게시판 설명 데이터를 담을 변수, 현재 게시판 설명에 대한 사용처가 없어 변수는 미사용중
                boardjson = userdata.toString();
            }catch (JSONException e){
                e.printStackTrace();
            }
            String[] params = {boardjson};
            AddboardTask Write = new AddboardTask();
            Write.execute(params);
            DoFileUpload("http://192.168.0.3:5000/imgupload", getAbsolutePath(photoURI));

            //            Intent intent = new Intent(AddBoardActivity.this, BoardMainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // 상위 스택 액티비티 모두 제거
//            intent.putExtra("EMAIL", user_EMAIL);
//            AddBoardActivity.this.finish();
//            startActivity(intent);

            try{
                /*
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap = ((BitmapDrawable)userImage.getDrawable()).getBitmap();//오류 발생(09-05)
                float scale = 1024/(float)bitmap.getWidth();
                int image_w = (int) (bitmap.getWidth() * scale);
                int image_h = (int) (bitmap.getHeight() * scale);
                Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
                resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                 */

                Intent intent = new Intent(getApplicationContext(), BoardMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.putExtra("image", byteArray);
                intent.putExtra("boardName", BoardNameEdit.getText().toString());
                intent.putExtra("EMAIL", user_EMAIL);
                AddBoardActivity.this.finish();
                startActivity(intent);

            } catch (Exception e){
                Log.e("ABitmapError", e.toString());
                /*
                E/ABitmapError: java.lang.NullPointerException:
                Attempt to invoke virtual method
                'android.graphics.drawable.Drawable android.widget.ImageView.getDrawable()'
                on a null object reference
                 */
            }
        });

        checkPermission();
    }

    public void DoFileUpload(String apiUrI, String absolutePath) {
        HttpFileUpload(apiUrI, "", absolutePath);
    }

    // 이미지의 절대경로를 전달
    private String getAbsolutePath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        //Toast.makeText(WriteActivity.this, cursor.getString(column_index), Toast.LENGTH_SHORT).show();
        return cursor.getString(column_index);
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "photos");

        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }

    private void getAlbum() {
        Log.i("getAlbum", "Call");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }

    // 이미지 crop
    public void cropImage() {
        Log.i("cropImage", "Call");
        Log.i("cropImage", "photoURI : " + photoURI + " / albumURI : " + albumURI);

        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        // 50x50픽셀미만은 편집할 수 없다는 문구 처리 + 갤러리, 포토 둘다 호환하는 방법
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setDataAndType(photoURI, "image/*");
        //cropIntent.putExtra("outputX", 200); // crop한 이미지의 x축 크기, 결과물의 크기
        //cropIntent.putExtra("outputY", 200); // crop한 이미지의 y축 크기
        cropIntent.putExtra("aspectX", 1); // crop 박스의 x축 비율, 1&1이면 정사각형
        cropIntent.putExtra("aspectY", 1); // crop 박스의 y축 비율
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("output", albumURI); // 크랍된 이미지를 해당 경로에 저장
        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Log.i("REQUEST_TAKE_PHOTO", "OK");
                        //galleryAddPic();

                        userImage.setImageURI(imageUri);
                    } catch (Exception e) {
                        Log.e("REQUEST_TAKE_PHOTO", e.toString());
                    }
                } else {
                    Toast.makeText(AddBoardActivity.this, "사진찍기를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_TAKE_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getData() != null) {
                        try {
                            File albumFile = null;
                            albumFile = createImageFile();
                            photoURI = data.getData();
                            albumURI = Uri.fromFile(albumFile);
                            cropImage();
                        } catch (Exception e) {
                            Log.e("TAKE_ALBUM_SINGLE ERROR", e.toString());
                        }
                    }
                }
                break;

            case REQUEST_IMAGE_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    //galleryAddPic();

                    try{
                        // 이미지 뷰어에 이미지 전송
                        userImage.setImageURI(albumURI);
                    } catch (Exception e){
                        Log.e("setImageURIERROR", e.toString());
                    }
                }
                break;
        }
    }

    // 권한 설정
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 다시 보지 않기 버튼을 만드려면 이 부분에 바로 요청을 하도록 하면 됨 (아래 else{..} 부분 제거)
            // ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_CAMERA);

            // 처음 호출시엔 if()안의 부분은 false로 리턴 됨 -> else{..}의 요청으로 넘어감
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_CAMERA:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(AddBoardActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
        }
    }

    // 서버에 이미지 업로드
    public void HttpFileUpload(String urlString, String params, String fileName) {
        try {
            FileInputStream mFileInputStream = new FileInputStream(fileName);
            URL connectUrl = new URL(urlString);
            Log.d("Test", "mFileInputStream  is " + mFileInputStream);

            // open connection
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("file", fileName);
            conn.setRequestProperty("user", user_id);
            conn.setRequestProperty("name", "file");
            conn.setRequestProperty("someParameter", "someValue");


            // write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

            Log.d("Test", "image byte is " + bytesRead);

            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.e("Test", "File is written");
            mFileInputStream.close();
            dos.flush(); // finish upload...

            // get response
            int ch;
            InputStream is = conn.getInputStream();
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String s = b.toString();
            Log.e("Test", "result = " + s);
            dos.close();
            Toast.makeText(AddBoardActivity.this, "전송 완료", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            Toast.makeText(AddBoardActivity.this, "오류 메세지" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private class AddboardTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            //String hjson = params[0];
            String userdata = params[0];
            try {
                String host_url = "http://192.168.0.3:5000/addboard";
//                String host_url = "https://webhook.site/2e08c0c3-79dc-4f65-bba8-3cba6718f78f";
                URL url = new URL(host_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(15*1000);//Timeout setting
                conn.setRequestProperty("Content-Type", "application/json");//Request body 전달시 json 형태로 전달
                conn.setRequestMethod("POST");//보내는 데이터 형태는 post로 응답
                conn.setDoOutput(true);//서버로 응답을 보냄
                conn.setDoInput(true);//서버로부터 응답을 받음
                conn.connect();
                OutputStreamWriter streamWriter = new OutputStreamWriter(conn.getOutputStream());
                streamWriter.write(userdata);//Request body에 json data 세팅
                streamWriter.flush();//json data 입력후 저장
                streamWriter.close();
                InputStream inputStream = conn.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                result = buffer.toString();
                int responsecode = conn.getResponseCode();//http 응답코드 송신


            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if(str.equals("error")==true) {
                Toast.makeText(AddBoardActivity.this, "errer", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(AddBoardActivity.this, "전송 성공", Toast.LENGTH_SHORT).show();
            }
        }
    }
}