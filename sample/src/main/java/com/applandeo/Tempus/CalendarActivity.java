package com.applandeo.Tempus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.Tempus.utils.DrawableUtils;
import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.example.tempus.ui.addSchedule.ConfirmScheduleActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class CalendarActivity extends AppCompatActivity {

    // 캘린더 시작 및 종료 날짜
    String startDate = "20100101";
    String endDate = "20301231";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);

        List<EventDay> events = new ArrayList<>();

        // 현재 날짜에 원형 M기호 표시
        Calendar calendar = Calendar.getInstance();
        events.add(new EventDay(calendar, DrawableUtils.getCircleDrawableWithText(this, "T")));

        // 현재 날짜 +10일에 표시
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_MONTH, 10);
        events.add(new EventDay(calendar1, DrawableUtils.getCircleDrawableWithText(this, "1")));
        //events.add(new EventDay(calendar1, R.drawable.sample_icon_2));

        /*
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_MONTH, 10);
        events.add(new EventDay(calendar2, R.drawable.sample_icon_3, Color.parseColor("#228B22")));
         */

        // 현재 날짜 +7일에 표시
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_MONTH, 7);
        events.add(new EventDay(calendar3, R.drawable.sample_four_icons));

        Calendar calendar4 = Calendar.getInstance();
        calendar4.add(Calendar.DAY_OF_MONTH, 13);
        events.add(new EventDay(calendar4, DrawableUtils.getThreeDots(this)));

        CalendarView calendarView = findViewById(R.id.calendarView);

        // 캘린더 범위 설정
        Calendar min = Calendar.getInstance();
        min.set(Calendar.YEAR, Integer.parseInt(startDate.substring(0,4)));
        min.set(Calendar.MONTH, Integer.parseInt(startDate.substring(4,6))-1);
        min.set(Calendar.DATE, Integer.parseInt(startDate.substring(6,8)));

        Calendar max = Calendar.getInstance();
        max.set(Calendar.YEAR, Integer.parseInt(endDate.substring(0,4)));
        max.set(Calendar.MONTH, Integer.parseInt(endDate.substring(4,6))-1);
        max.set(Calendar.DATE, Integer.parseInt(endDate.substring(6,8)));

        calendarView.setMinimumDate(min);
        calendarView.setMaximumDate(max);

        // 정의된 이벤트들을 세팅
        calendarView.setEvents(events);

        calendarView.setDisabledDays(getDisabledDays());

        calendarView.setOnDayClickListener(eventDay -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일");
            String date = simpleDateFormat.format(eventDay.getCalendar().getTime());

            Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();

            // 일정 뷰어 페이지로 이동
            Intent CAIntent = new Intent(getApplicationContext(), ConfirmScheduleActivity.class);
            CAIntent.putExtra("날짜", date);
            startActivity(CAIntent);
        });

        // 랜덤 버튼을 눌렀을 때 toast를 띄우는 부분
        /*
        Button setDateButton = findViewById(R.id.setDateButton);
        setDateButton.setOnClickListener(v -> {
            try {
                Calendar randomCalendar = getRandomCalendar();
                String text = randomCalendar.getTime().toString();
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                calendarView.setDate(randomCalendar);


            } catch (OutOfDateRangeException exception) {
                exception.printStackTrace();

                Toast.makeText(getApplicationContext(),
                        "Date is out of range",
                        Toast.LENGTH_LONG).show();
            }
        });
         */
    }

    private List<Calendar> getDisabledDays() {
        Calendar firstDisabled = DateUtils.getCalendar();
        firstDisabled.add(Calendar.DAY_OF_MONTH, 2);

        Calendar secondDisabled = DateUtils.getCalendar();
        secondDisabled.add(Calendar.DAY_OF_MONTH, 1);

        Calendar thirdDisabled = DateUtils.getCalendar();
        thirdDisabled.add(Calendar.DAY_OF_MONTH, 18);

        List<Calendar> calendars = new ArrayList<>();
        calendars.add(firstDisabled);
        calendars.add(secondDisabled);
        calendars.add(thirdDisabled);
        return calendars;
    }

    // 랜덤 날짜를 select
    /*
    private Calendar getRandomCalendar() {
        Random random = new Random();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, random.nextInt(99));

        return calendar;
    }
     */
   }