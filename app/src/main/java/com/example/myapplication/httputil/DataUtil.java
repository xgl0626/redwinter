package com.example.myapplication.httputil;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DataUtil {
    public static String getDateDescription(String formatDate) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        String[] date = formatDate.split("-");
        String time = date[2].substring(3);
        date[2] = date[2].substring(0, 2);

        int dy = gregorianCalendar.get(Calendar.YEAR) - Integer.parseInt(date[0]);
        int dm = gregorianCalendar.get(Calendar.MONTH) + 1 - Integer.parseInt(date[1]);
        int dd = gregorianCalendar.get(Calendar.DAY_OF_MONTH) - Integer.parseInt(date[2]);
        if (dy == 0 && dm == 0)
            switch (dd) {
                case 0:
                    return "今天" + time;
                case 1:
                    return "昨天" + time;
                case 2:
                    return "前天" + time;
                default:
                    return formatDate;
            }
        return formatDate;
    }
}
