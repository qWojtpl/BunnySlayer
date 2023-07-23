package pl.bunnyslayer.util;

import java.util.Calendar;

public class DateManager {

    public static String getHour() {
        int hour = getCalendar().get(Calendar.HOUR_OF_DAY);
        String returnable = hour + "";
        if(hour < 10) {
            returnable = "0" + returnable;
        }
        return returnable;
    }

    public static String getMinute() {
        int minute = getCalendar().get(Calendar.MINUTE);
        String returnable = minute + "";
        if(minute < 10) {
            returnable = "0" + returnable;
        }
        return returnable;
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

}
