package com.febrian.covidapp.global;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
    final public static String getDate(Integer minus) {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        calendar.add(Calendar.DATE, minus);

        return dateFormat.format(calendar.getTime());
    }
}
