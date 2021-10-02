package com.applandeo.Tempus;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    static final String PREF_USER_NAME = "username";    // key 값
    static SharedPreferences SP;
    static SharedPreferences.Editor editor;

    static SharedPreferences getSharedPreferences(Context ctx) {    // 모든 액티비티에서 인스턴스를 얻기 위한 메소드이다.
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public static void setUserName(Context ctx, String userName) {  // 로그인 시 자동 로그인 여부에 따라 호출 될 메소드이다. userName이 저장된다.
        SP = getSharedPreferences(ctx);                     // SP 정의
        editor = SP.edit();                                 // 에디터 정의
        editor.putString(PREF_USER_NAME, userName);         // userName 저장
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getUserName() { // 현재 저장된 정보를 가져오기 위한 메소드이다.
        return SP.getString(PREF_USER_NAME, "");
    }

    // 로그아웃
    public static void clearUserName(Context ctx) { // 자동 로그인 해제 및 로그아웃 시 호출 될 메소드이다.
        editor.clear(); // 저장된 모든 데이터 삭제
        editor.commit();
    }
}