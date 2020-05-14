package com.sangdaero.walab.ranking.service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

@Service
public class RankingService {

    // 현재 날짜 월요일
    public static String getCurMonday(){
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        return formatter.format(c.getTime());
    }

    // 현재 날짜 일요일
    public static String getCurSunday(){
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        c.add(c.DATE,7);
        return formatter.format(c.getTime());
    }

    // 이번 달 첫째 날
    public static String getFirstDayMonth() {
        Calendar calendar = Calendar.getInstance();
        String firstDay="";

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int fday=calendar.getActualMinimum(Calendar.DAY_OF_MONTH);

        String m = checkMonth(month);
        String d = checkDay(fday);

        firstDay=Integer.toString(year)+"-"+m+"-"+d;

        return firstDay;
    }

    // 이번 달 마지막 날
    public static String getLastDayMonth() {
        Calendar calendar = Calendar.getInstance();
        String lastDay="";

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int eday=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String m = checkMonth(month);
        String d = checkDay(eday);

        lastDay=Integer.toString(year)+"-"+m+"-"+d;

        return lastDay;
    }

    private static String checkDay(int day) {
        String d;
        if(day<10) {
            d="0"+Integer.toString(day);
        } else {
            d=Integer.toString(day);
        }
        return d;
    }

    private static String checkMonth(int month) {
        String m;
        if (month < 10) {
            m = "0" + Integer.toString(month);
        } else {
            m = Integer.toString(month);
        }
        return m;
    }

}
