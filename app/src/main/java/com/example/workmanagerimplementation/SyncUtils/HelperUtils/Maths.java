package com.example.workmanagerimplementation.SyncUtils.HelperUtils;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 * Created by Md.harun or rashid on 23,March,2021
 * BABL, Bangladesh,
 */
public class Maths {
    public static String format(Number n) {
        NumberFormat format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.FLOOR);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        return format.format(n);
    }
    public static String formatWeight(Number n) {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        return format.format(n);
    }
    public static String formatWeightTwoDecimal(Number n) {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return format.format(n);
    }
    public static String formatTwoDecimal(Number n) {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return format.format(n);
    }
    public static String formatPrice(Number n) {
        NumberFormat format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.FLOOR);
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return format.format(n);
    }

    public static String getUniqueNumber() {
        return String.valueOf(System.currentTimeMillis());
    }



    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    private String isEmpty(Object obj) {
        if (!obj.toString().isEmpty() || obj != null || !obj.equals("")) {
            return String.valueOf(obj);
        }
        return "0";
    }

    static String formatDate(long dateInMilliseconds) {
        Date date = new Date(dateInMilliseconds);
        return DateFormat.getDateInstance().format(date);
    }

   /* public static String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if ( julianDay == currentJulianDay +1 ) {
            return context.getString(R.string.tomorrow);
        } else {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }*/

    public   static String convertEngToBangla(String engNumber) {
        String benNumber = "";
        for (int i = 0; i < engNumber.length(); i++) {
            if (engNumber.charAt(i) == '0')
                benNumber = benNumber + '০';
            else if (engNumber.charAt(i) == '1')
                benNumber = benNumber + '১';
            else if (engNumber.charAt(i) == '2')
                benNumber = benNumber + '২';
            else if (engNumber.charAt(i) == '3')
                benNumber = benNumber + '৩';
            else if (engNumber.charAt(i) == '4')
                benNumber = benNumber + '৪';
            else if (engNumber.charAt(i) == '5')
                benNumber = benNumber + '৫';
            else if (engNumber.charAt(i) == '6')
                benNumber = benNumber + '৬';
            else if (engNumber.charAt(i) == '7')
                benNumber = benNumber + '৭';
            else if (engNumber.charAt(i) == '8')
                benNumber = benNumber + '৮';
            else if (engNumber.charAt(i) == '9')
                benNumber = benNumber + '৯';
            else
                benNumber = benNumber + engNumber.charAt(i);
        }
        return benNumber;
    }

}
