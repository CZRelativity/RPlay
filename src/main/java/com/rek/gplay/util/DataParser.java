package com.rek.gplay.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataParser {

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH);

    public static String parseDate(String dateStr) {
        String ret = dateStr;
        try {
            Date date = format.parse(dateStr);
            if (date != null) {
                long time = date.getTime();
                long now = System.currentTimeMillis();
                long day = 24L * 60 * 60 * 1000;
                if (now - time < 30 * day) {
                    ret=(now-time)/day+"天前";
                }else if(now-time<365*day){
                    ret=(now-time)/30/day+"月前";
                }else {
                    ret=dateStr.substring(0,4)+"年";
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String parseTitle(String title){
        return title
                .replaceAll("&mdash;","—")
                .replaceAll("&amp;","&")
                .replaceAll("<em class='highlight'>","")
                .replaceAll("</em>","");
    }

}
