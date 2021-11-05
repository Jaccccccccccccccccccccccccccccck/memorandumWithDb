package com.example.memorandum.tools;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    @SuppressLint("SimpleDateFormat")
    private static  final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
    //将日期格式转成文本格式
    public static String DateToString(Date date){
        return sdf.format(date);
    }
    //将文本格式转成日期格式
    public static Date StringToDate(String dateStr){
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
