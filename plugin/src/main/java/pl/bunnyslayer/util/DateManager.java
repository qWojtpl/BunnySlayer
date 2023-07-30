package pl.bunnyslayer.util;

import java.util.Calendar;

public class DateManager {

    private final static String[] days = new String[]{"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};

    public static String getDayName() {
        return days[getCalendar().get(Calendar.DAY_OF_WEEK)];
    }

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
