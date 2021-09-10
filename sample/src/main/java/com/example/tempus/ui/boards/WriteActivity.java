package com.example.tempus.ui.boards;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.applandeo.Tempus.MainActivity;
import com.applandeo.Tempus.R;
import com.applandeo.Tempus.SignupActivity;
import com.android.volley.toolbox.StringRequest;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;

public class WriteActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;

    Button changeDisplay;
    ImageButton addPhoto;
    Button finButton;

    EditText dateEdit;
    EditText contentEdit;

    String mCurrentPhotoPath;
    Uri imageUri;
    Uri photoURI, albumURI;

    ImageView userImage;

    RadioGroup radioGroup;

    String WR_date, WR_body;

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "boundary=----WebKitFormBoundarylLEkUd8JSJOasqs0";
    String user_id = "test";

    Intent WAIntent;
    String user_EMAIL;

    String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        WAIntent = getIntent();
        user_EMAIL = WAIntent.getStringExtra("EMAIL");
        groupName = WAIntent.getStringExtra("GROUP");

        // Disable StrictMode
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        changeDisplay = findViewById(R.id.changeDisplay);
        changeDisplay.setOnClickListener(view -> {
            // 지출 내역 형식으로 변경
            Intent intent = new Intent(getApplicationContext(), CreateExpenditureHistoryActivityForWrite.class);
            intent.putExtra("EMAIL", user_EMAIL);
            intent.putExtra("GROUP", groupName);
            startActivity(intent);
        });

        userImage = findViewById(R.id.userImage);

        addPhoto = findViewById(R.id.addPhoto);
        addPhoto.setOnClickListener(v -> {
            // file search
            getAlbum();
        });

        dateEdit = findViewById(R.id.dateEdit);
        contentEdit = findViewById(R.id.contentEdit);

        finButton = findViewById(R.id.finButton);
        finButton.setOnClickListener(view -> {
            JSONObject jsonObject = new JSONObject();

            JSONObject head = new JSONObject();     //JSON 오브젝트의 head 부분
            JSONObject body = new JSONObject();     //JSON 오브젝트의 body 부분

            String headjson = null;
            String bodyjson = null;

            WR_date = dateEdit.getText().toString();
            WR_body = contentEdit.getText().toString();

            String urIString = "http://192.168.0.3:5000/imgupload";
            //String urIString = "https://webhook.site/d4dc0f16-d848-41ba-a14f-bbea18b82018";
            DoFileUpload(urIString, getAbsolutePath(photoURI));
            //uploadMultipart(urIString, getAbsolutePath(photoURI));

            /*
            HttpPost post = new HttpPost("http://echo.200please.com");
            InputStream inputStream = new FileInputStream(zipFileName);
            File file = new File(imageFileName);
            String message = "This is a multipart post";
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody
                    ("upfile", file, ContentType.DEFAULT_BINARY, imageFileName);
            builder.addBinaryBody
                    ("upstream", inputStream, ContentType.create("application/zip"), zipFileName);
            builder.addTextBody("text", message, ContentType.TEXT_PLAIN);

            HttpEntity entity = builder.build();
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            try {
                AndroidUploader uploader = new AndroidUploader("user", "userPwd");

                String path = getAbsolutePath(photoURI);

                uploader.uploadPicture(path);
            } catch (Exception e) {
                Log.e(e.getClass().getName(), e.getMessage());
            }



            try {
                head.put("WR_ID", "1");     //head 부분 생성 시작
                head.put("WR_TYPE", "A");    //head 부분 생성 완료
                // A: 자유 형식, B: 지출 목록 형식

                jsonObject.put("head", head);   //head 오브젝트 추가
                headjson = jsonObject.toString();
                // 작성일자
                body.put("WR_DATE", WR_date);   //body부분 생성 시작
                // 글 내용
                body.put("WR_BODY", WR_body);   //body부분 생성 완료


                jsonObject.put("body", body);   //body 오브젝트 추가
                bodyjson = jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 작성 일자 혹은 내용이 빈 칸인 경우 완료버튼 onClick함수 종료
            if (dateEdit.getText().length() == 0 || contentEdit.getText().length() == 0) {
                Toast.makeText(WriteActivity.this, "작성 일자 혹은 내용에 작성된 글이 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }//            Log.e("json", "생성한 json : " + jsonObject.toString());
            String[] params = {headjson,bodyjson};
            PostTask Write = new PostTask();
            Write.execute(params);

             */

            Intent intent = new Intent(WriteActivity.this, boardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // 상위 스택 액티비티 모두 제거
            intent.putExtra("EMAIL", user_EMAIL);
            intent.putExtra("GROUP", groupName);

            WriteActivity.this.finish();
            startActivity(intent);
        });

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

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

    /* 갤러리에 크롭핑한 사진 저장
    private void galleryAddPic() {
        Log.i("galleryAddPic", "Call");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }
     */

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
                    Toast.makeText(WriteActivity.this, "사진찍기를 취소하였습니다.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(WriteActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
        }
    }

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (i == R.id.allShareRadioButton) {
                Toast.makeText(WriteActivity.this, "지인 모두와 공유", Toast.LENGTH_SHORT).show();
            } else if (i == R.id.partShareRadioButton) {
                Toast.makeText(WriteActivity.this, "특정 그룹과 공유", Toast.LENGTH_SHORT).show();
            } else if (i == R.id.nonShareRadioButton) {
                Toast.makeText(WriteActivity.this, "공유하지 않음", Toast.LENGTH_SHORT).show();
            }
        }
    };

//    public void uploadMultipart(String url, String path) {
//        try {
//            String uploadId = UUID.randomUUID().toString();
//            //Creating a multi part request
//            new MultipartUploadRequest(this, uploadId, url)
//                    .addFileToUpload(path, "image") //Adding file
//                    .addFileToUpload(path, "image2") //Adding file
//                    .addParameter("content", "test") //Adding text parameter to the request
//                    .addParameter("id", "test") //Adding text parameter to the request
//                    .addParameter("time", "test") //Adding text parameter to the request
//                    .setNotificationConfig(new UploadNotificationConfig().setTitle("[푸드다이어리] 사진 업로드 성공!").setCompletedMessage("사진 업로드를 성공적으로 완료했습니다."))
//                    .setMaxRetries(2)
//                    .startUpload();
//        } catch (Exception exc) {
//            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

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
            Toast.makeText(WriteActivity.this, "전송 완료", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            Toast.makeText(WriteActivity.this, "오류 메세지" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

