package com.applandeo.Tempus;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import com.example.tempus.ui.MyService;
import com.example.tempus.ui.boards.BoardMainActivity;

public class appClass extends Application {
    NotificationManager notificationManager;
    NotificationChannel notificationChannel;
    @Override
    public void onCreate(){
        super.onCreate();

        // create Notification Channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){                                 // 안드로이드 8.0이상에서만 작동
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationChannel = new NotificationChannel(                                  // 알림 채널 정보(id, 채널명)
                    "Noti_id",
                    "알림 채널",
                    NotificationManager.IMPORTANCE_DEFAULT);                                // 알림 중요도
            notificationChannel.setDescription("Tempus Notification Channel");                     // 채널 간단 설명
            notificationChannel.enableLights(true);                                         // 알림 표시등 표시 여부
            notificationChannel.setLightColor(Color.GREEN);                                 // 알림 표시등 빛의 색깔 설정
            notificationChannel.enableVibration(true);                                      // 진동 발생 여부
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});        // 진동 패턴
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);   // 잠금 화면에 표시 될지 설정
            notificationManager.createNotificationChannel(notificationChannel);             // 채널 생성
        }

        // 앱이 설치돼있을 때 기본적으로 알림 ON
        // 초대가 왔을 때만 알림이 오도록 변경 필요
        Intent intent = new Intent(this, MyService.class);
        //startService(intent);
    }
}
