package pl.bunnyslayer.util;

import java.util.Calendar;

import static java.util.Calendar.*;

public class DateManager {

    private final static String[] days = new String[]{"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};

    public static String getDayName() {
        return days[getCalendar().get(DAY_OF_WEEK) - 1];
    }

    public static String getFormatHour() {
        int hour = getCalendar().get(Calendar.HOUR_OF_DAY);
        String returnable = hour + "";
        if(hour < 10) {
            returnable = "0" + returnable;
        }
        return returnable;
    }

    public static String getFormatMinute() {
        int minute = getCalendar().get(Calendar.MINUTE);
        String returnable = minute + "";
        if(minute < 10) {
            returnable = "0" + returnable;
        }
        return returnable;
    }

    public static int getDaysOfMonth() {
        return getCalendar().getActualMaximum(DAY_OF_MONTH);
    }

    public static int getDayOfWeek() {
        return getCalendar().get(DAY_OF_WEEK);
    }

    public static int getYear() {
        return getCalendar().get(YEAR);
    }

    public static int getMonth() {
        return getCalendar().get(MONTH)+1;
    }

    public static int getDay() {
        return getCalendar().get(DAY_OF_MONTH);
    }

    public static int getHour() {
        return getCalendar().get(HOUR_OF_DAY);
    }

    public static int getMinute() {
        return getCalendar().get(MINUTE);
    }

    public static int getSecond() {
        return getCalendar().get(SECOND);
    }

    public static String getFormattedDate(String format) {
        format = format.replace("%Y", String.valueOf(getYear()));
        format = format.replace("%M", String.valueOf(getMonth()));
        format = format.replace("%D", String.valueOf(getDay()));
        format = format.replace("%h", String.valueOf(getHour()));
        format = format.replace("%m", String.valueOf(getMinute()));
        format = format.replace("%s", String.valueOf(getSecond()));
        return format;
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

}
