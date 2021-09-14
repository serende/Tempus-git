package com.example.tempus.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.applandeo.Tempus.NOBroadCastReceiver;
import com.applandeo.Tempus.R;

import com.applandeo.Tempus.MainActivity;
import com.applandeo.Tempus.YESBroadCastReceiver;

// 백그라운드에서도 알림이 동작하도록 서비스로 구현
public class MyService extends Service {
    NotificationManager Notifi_Manage;
    ServiceThread thread;
    //Notification Notifi;
    Notification.Builder NotifiBuilder;

    String InviteUser;      // 게시판 초대를 보낸 사용자
    String InviteGroupName; // 초대받은 게시판명
    int InviteYN = 0;       // 게시판 초대 메시지 유무

   public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifi_Manage = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업
    public void onDestroy() {
        thread.stopForever();
        thread = null;          // 쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class myServiceHandler extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent(MyService.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    MyService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            /*
            Notifi = new Notification.Builder(getApplicationContext(), "Noti_id")
                    .setContentTitle("Tempus")              // 알림에서 위에 뜨는 메시지
                    .setContentText("게시판 초대 알림")        // 알림에서 밑에 뜨는 메시지
                    .setSmallIcon(R.drawable.tempus_logo)   // 알림에 작게 뜨는 아이콘
                    .setTicker("알림!!!")                    // 알림 발생 시 잠깐 나오는 텍스트
                    .setContentIntent(pendingIntent)        // 알림을 누르면 실행할 Intent
                    .build();

            //소리추가
            Notifi.defaults = Notification.DEFAULT_SOUND;

            //알림 소리를 한번만 내도록
            Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;

            //확인하면 자동으로 알림이 제거 되도록
            Notifi.flags = Notification.FLAG_AUTO_CANCEL;
            */

            Intent YESBroadCastIntent = new Intent(getApplicationContext(), YESBroadCastReceiver.class);
            YESBroadCastIntent.setAction("accept");
            YESBroadCastIntent.putExtra("EXTRA_NOTIFICATION_ID", 1);
            PendingIntent YESBroadPendingIntent =
                    PendingIntent.getBroadcast(getApplicationContext(), 0, YESBroadCastIntent, 0);

            Intent NOBroadCastIntent = new Intent(getApplicationContext(), NOBroadCastReceiver.class);
            NOBroadCastIntent.setAction("refuse");
            NOBroadCastIntent.putExtra("EXTRA_NOTIFICATION_ID", 0);
            PendingIntent NOBroadPendingIntent =
                    PendingIntent.getBroadcast(getApplicationContext(), 0, NOBroadCastIntent, 0);

            NotifiBuilder = new Notification.Builder(getApplicationContext(), "Noti_id");
            NotifiBuilder.setContentTitle("Tempus")                         // 알림에서 위에 뜨는 메시지
                    .setContentText(InviteGroupName + "게시판 초대 알림")      // 알림에서 밑에 뜨는 메시지
                    .setSmallIcon(R.drawable.tempus_logo)                   // 알림에 작게 뜨는 아이콘
                    .setTicker("알림!!!")                                    // 알림 발생 시 잠깐 나오는 텍스트
                    .setContentIntent(pendingIntent)                        // 알림을 누르면 실행할 Intent
                    .setAutoCancel(true)                                    // 확인하면 자동으로 알림이 지워지도록 설정
                    .addAction(R.drawable.tempus_logo, "수락", YESBroadPendingIntent)
                    .addAction(R.drawable.tempus_logo, "거절", NOBroadPendingIntent)
                    .build();

            //InviteYN = 1;

            // 초대 메시지가 왔을 경우에만 알림이 작동
            if(InviteYN == 1){
                Notifi_Manage.notify( 777 , NotifiBuilder.build());
                InviteYN = 0;
            } else if (InviteYN != 0) {
                Toast.makeText(getApplicationContext(), "Notification Error", Toast.LENGTH_SHORT).show();
            }
        }
    };
}