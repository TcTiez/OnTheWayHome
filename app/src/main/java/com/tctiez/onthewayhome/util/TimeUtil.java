package com.tctiez.onthewayhome.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

/**
 * Created by Eugene J. Jeon on 2015-08-14.
 */
public class TimeUtil {
    public static String getTimeToString(Date date) {
        try {
            return getTimeToString(date.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    public static String getTimeToString(long date) {
        String ret = "";
        try {
            DecimalFormat DF2 = new DecimalFormat("00");
            DecimalFormat DF3 = new DecimalFormat("000");
            DecimalFormat DF4 = new DecimalFormat("0000");
            Calendar tCal = Calendar.getInstance();
            tCal.setTimeInMillis(date);
            ret += DF4.format(tCal.get(Calendar.YEAR));
            ret += DF2.format(tCal.get(Calendar.MONTH));
            ret += DF2.format(tCal.get(Calendar.DATE));
            ret += DF2.format(tCal.get(Calendar.HOUR_OF_DAY));
            ret += DF2.format(tCal.get(Calendar.MINUTE));
            ret += DF2.format(tCal.get(Calendar.SECOND));
            ret += DF3.format(tCal.get(Calendar.MILLISECOND));
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static String getTimeToStringDayStart(Date date) {
        String ret = "";
        try {
            ret = getTimeToStringDayStart(date.getTime());
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static String getTimeToStringDayStart(long date) {
        Calendar tCal = Calendar.getInstance();
        tCal.setTimeInMillis(date);
        tCal.set(Calendar.HOUR_OF_DAY, 0);
        tCal.set(Calendar.MINUTE, 0);
        tCal.set(Calendar.SECOND, 0);
        tCal.set(Calendar.MILLISECOND, 0);

        return tCal.getTimeInMillis() + "";
    }

    public static String getTimeToStringDayEnd(Date date) {
        String ret = "";
        try {
            ret = getTimeToStringDayEnd(date.getTime());
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static String getTimeToStringDayEnd(long date) {
        Calendar tCal = Calendar.getInstance();
        tCal.setTimeInMillis(date);
        tCal.set(Calendar.HOUR_OF_DAY, 23);
        tCal.set(Calendar.MINUTE, 59);
        tCal.set(Calendar.SECOND, 59);
        tCal.set(Calendar.MILLISECOND, 999);
        return tCal.getTimeInMillis() + "";
    }

    public static String getTimeToStringWeekStart(Date date) {
        String ret = "";
        try {
            ret = getTimeToStringWeekStart(date.getTime());
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static String getTimeToStringWeekStart(long date) {
        Calendar tCal = Calendar.getInstance();
        tCal.setTimeInMillis(date);
        tCal.set(Calendar.HOUR_OF_DAY, 0);
        tCal.set(Calendar.MINUTE, 0);
        tCal.set(Calendar.SECOND, 0);
        tCal.set(Calendar.MILLISECOND, 0);

        int dayOfWeek = tCal.get(Calendar.DAY_OF_WEEK) - 1;
        tCal.add(Calendar.DATE, -dayOfWeek);

        return tCal.getTimeInMillis() + "";
    }

    public static String getTimeToStringWeekEnd(Date date) {
        String ret = "";
        try {
            ret = getTimeToStringWeekEnd(date.getTime());
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static String getTimeToStringWeekEnd(long date) {
        Calendar tCal = Calendar.getInstance();
        tCal.setTimeInMillis(date);

        int dayOfWeek = tCal.get(Calendar.DAY_OF_WEEK) - 1;
        tCal.add(Calendar.DATE, -dayOfWeek);
        tCal.add(Calendar.DATE, 6);

        tCal.set(Calendar.HOUR_OF_DAY, 23);
        tCal.set(Calendar.MINUTE, 59);
        tCal.set(Calendar.SECOND, 59);
        tCal.set(Calendar.MILLISECOND, 999);

        return tCal.getTimeInMillis() + "";
    }

    public static String getTimeToStringStart(Date date, long nowDate, int week, boolean isWeek) {
        String ret = "";
        try {
            ret = getTimeToStringStart(date.getTime(), nowDate, week, isWeek);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static String getTimeToStringStart(long date, long nowDate, int week, boolean isWeek) {

        String ret = "";

        try {

            long tL_start, tL_end;
            Calendar tCalStart = Calendar.getInstance();
            tCalStart.setTimeInMillis(nowDate);
            tCalStart.set(Calendar.HOUR_OF_DAY, 0);
            tCalStart.set(Calendar.MINUTE, 0);
            tCalStart.set(Calendar.SECOND, 0);

            Calendar tCalEnd = Calendar.getInstance();

            if (isWeek) {
                tCalStart.add(Calendar.DATE, -(tCalStart.get(Calendar.DAY_OF_WEEK) - 1));
                tL_start = tCalStart.getTimeInMillis();
                tCalEnd.setTimeInMillis(tL_start);
                tCalEnd.add(Calendar.WEEK_OF_YEAR, week);
                tCalEnd.add(Calendar.DATE, 6);
            } else {
                tL_start = tCalStart.getTimeInMillis();
                tCalEnd.setTimeInMillis(tL_start);
                tCalEnd.add(Calendar.DATE, week);
            }
            tCalEnd.set(Calendar.HOUR_OF_DAY, 23);
            tCalEnd.set(Calendar.MINUTE, 59);
            tCalEnd.set(Calendar.SECOND, 59);
            tL_end = tCalEnd.getTimeInMillis();

            if (date < nowDate) {
                while (date < tL_start) {
                    if (isWeek) {
                        tCalStart.add(Calendar.WEEK_OF_YEAR, -(week + 1));
                    } else {
                        tCalStart.add(Calendar.DATE, -(week + 1));
                    }
                    tL_start = tCalStart.getTimeInMillis();
                }
            } else {

                while (date > tL_end) {
                    if (isWeek) {
                        tCalStart.add(Calendar.WEEK_OF_YEAR, (week + 1));
                        tCalEnd.add(Calendar.WEEK_OF_YEAR, (week + 1));
                    } else {
                        tCalStart.add(Calendar.DATE, (week + 1));
                        tCalEnd.add(Calendar.DATE, (week + 1));
                    }
                    tL_start = tCalStart.getTimeInMillis();
                    tL_end = tCalEnd.getTimeInMillis();
                }
            }

            Calendar tCal = Calendar.getInstance();
            tCal.setTimeInMillis(tL_start);
            tCal.set(Calendar.HOUR_OF_DAY, 0);
            tCal.set(Calendar.MINUTE, 0);
            tCal.set(Calendar.SECOND, 0);
            tCal.set(Calendar.MILLISECOND, 0);

            ret = tCal.getTimeInMillis() + "";

        } catch (Exception e) {
            ret = "";
        }
        return ret;
    }

    public static String getTimeToStringEnd(Date date, long nowDate, int week, boolean isWeek) {
        String ret = "";
        try {
            ret = getTimeToStringEnd(date.getTime(), nowDate, week, isWeek);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static String getTimeToStringEnd(long date, long nowDate, int week, boolean isWeek) {
        String ret = "";
        try {
            long tL_start, tL_end;
            Calendar tCalStart = Calendar.getInstance();
            tCalStart.setTimeInMillis(nowDate);
            tCalStart.set(Calendar.HOUR_OF_DAY, 0);
            tCalStart.set(Calendar.MINUTE, 0);
            tCalStart.set(Calendar.SECOND, 0);

            Calendar tCalEnd = Calendar.getInstance();

            if (isWeek) {
                tCalStart.add(Calendar.DATE, -(tCalStart.get(Calendar.DAY_OF_WEEK) - 1));
                tL_start = tCalStart.getTimeInMillis();
                tCalEnd.setTimeInMillis(tL_start);
                tCalEnd.add(Calendar.WEEK_OF_YEAR, week);
                tCalEnd.add(Calendar.DAY_OF_YEAR, 6);
            } else {
                tL_start = tCalStart.getTimeInMillis();
                tCalEnd.setTimeInMillis(tL_start);
                tCalEnd.add(Calendar.DAY_OF_YEAR, week);
            }
            tCalEnd.set(Calendar.HOUR_OF_DAY, 23);
            tCalEnd.set(Calendar.MINUTE, 59);
            tCalEnd.set(Calendar.SECOND, 59);
            tL_end = tCalEnd.getTimeInMillis();

            if (date < nowDate) {
                while (date < tL_start) {
                    if (isWeek) {
                        tCalStart.add(Calendar.WEEK_OF_YEAR, -(week + 1));
                        tCalEnd.add(Calendar.WEEK_OF_YEAR, -(week + 1));
                    } else {
                        tCalStart.add(Calendar.DAY_OF_YEAR, -(week + 1));
                        tCalEnd.add(Calendar.DAY_OF_YEAR, -(week + 1));
                    }
                    tL_start = tCalStart.getTimeInMillis();
                    tL_end = tCalEnd.getTimeInMillis();
                }
            } else {
                while (date > tL_end) {
                    if (isWeek) {
                        tCalEnd.add(Calendar.WEEK_OF_YEAR, (week + 1));
                    } else {
                        tCalEnd.add(Calendar.DAY_OF_YEAR, (week + 1));
                    }
                    tL_end = tCalEnd.getTimeInMillis();
                }
            }

            Calendar tCal = Calendar.getInstance();
            tCal.setTimeInMillis(tL_end);
            tCal.set(Calendar.HOUR_OF_DAY, 23);
            tCal.set(Calendar.MINUTE, 59);
            tCal.set(Calendar.SECOND, 59);
            tCal.set(Calendar.MILLISECOND, 999);

            ret = tCal.getTimeInMillis() + "";

        } catch (Exception e) {
            ret = "";
        }
        return ret;
    }

    public static Date StringdateToDate(String date) {
        return new Date(Long.parseLong(date));
    }

    public static String getDateToDeviceDateFormatStrTime(Context cont, Date date) {
        try {
            return DateUtils.formatDateTime(cont, date.getTime(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDateToDeviceDateFormatStrTimeFull(Context cont, Date date) {
        try {
            return DateUtils.formatDateTime(cont, date.getTime(), DateUtils.FORMAT_SHOW_TIME);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDateToDeviceDateFormatStrWeekFull(Context cont, Date date) {
        try {
            return DateUtils.formatDateTime(cont, date.getTime(), DateUtils.FORMAT_SHOW_WEEKDAY);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDateToDeviceDateFormatStrWeekTime(Context cont, Date date) {
        try {
            return DateUtils.formatDateTime(cont, date.getTime(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_WEEKDAY);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDateToDeviceDateFormatStrFullWeek(Context cont, Date date) {
        try {
            return DateUtils.formatDateTime(cont, date.getTime(), DateUtils.FORMAT_SHOW_WEEKDAY);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDateToDeviceDateFormatStrYYYYMMDDWeek(Context cont, Date date) {
        try {
            return DateUtils.formatDateTime(cont, date.getTime(),
                    DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_ALL);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDateToDeviceDateFormatStrYYYYMMDD(Context cont, Date date) {
        try {
            return DateUtils.formatDateTime(cont, date.getTime(),
                    DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDateToDeviceDateFormatStrYYYYMM(Context cont, Date date) {
        try {
            return DateUtils.formatDateTime(cont, date.getTime(),
                    DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NO_MONTH_DAY | DateUtils.FORMAT_ABBREV_ALL);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDateToDeviceDateFormatStr(Context cont, Date date) {
        try {
            return DateUtils.formatDateTime(cont, date.getTime(),
                    DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDateToDeviceDateFormatStrAmPm(Context cont, Date date) {
        try {
            return DateUtils.formatDateTime(cont, date.getTime(), DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME
                    | DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_12HOUR);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDateToDeviceDateFormatFullStr(Context cont, Date date) {
        try {
            return DateUtils.formatDateTime(cont, date.getTime(), DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME
                    | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_ALL);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDateToDeviceDateStrFormatYMDhsBar(Context cont) {
        String ret = null;
        try {
            String tStr = getDateToDeviceDateStrFormatYMDBar(cont);
            if (tStr != null && !tStr.equals("")) {
                ret = tStr + " ";
                tStr = getDateToDeviceDateStrFormaths(cont);
                if (tStr != null && !tStr.equals("")) {
                    ret += tStr;
                } else {
                    ret = null;
                }
            }
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static String getDateToDeviceDateStrFormatYMDBar(Context cont) {
        String ret = null;
        try {
            ret = Settings.System.getString(cont.getContentResolver(), Settings.System.DATE_FORMAT);
            if (TextUtils.isEmpty(ret)) {
                ret = "yyyy/MM/dd";
            } else {
                ret = ret.replace("-", "/");
            }
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static String getDateToDeviceDateStrFormaths(Context cont) {
        String ret = null;
        try {
            ret = DateFormat.is24HourFormat(cont) ? "H:mm" : "a h:mm";
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static String getDateToDeviceDateFormatNotelist(Context cont, Date date) {
        String ret = null;
        try {
            java.text.DateFormat tFormat = null;
            String format = Settings.System.getString(cont.getContentResolver(), Settings.System.DATE_FORMAT);
            String tS_24H = DateFormat.is24HourFormat(cont) ? " H:mm" : " a h:mm";
            if (TextUtils.isEmpty(format)) {
                tFormat = new SimpleDateFormat("yyyy/MM/dd" + tS_24H, Locale.getDefault());
            } else {
                format = format.replace("-", "/");
                tFormat = new SimpleDateFormat(format + tS_24H, Locale.getDefault());
            }
            ret = tFormat.format(date);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static boolean sameDateYYYY(Date date1, Date date2) {
        try {
            return sameDateYYYY(date1.getTime(), date2.getTime());
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean sameDateYYYY(long date1, long date2) {
        boolean isRet = false;
        try {
            Calendar tCal1 = Calendar.getInstance();
            Calendar tCal2 = Calendar.getInstance();

            tCal1.setTimeInMillis(date1);
            tCal2.setTimeInMillis(date2);
            if (tCal1.get(Calendar.YEAR) == tCal2.get(Calendar.YEAR)) {
                isRet = true;
            }
        } catch (Exception e) {
            isRet = false;
        }
        return isRet;
    }

    public static boolean sameDateYYYYMM(Date date1, Date date2) {
        try {
            return sameDateYYYYMM(date1.getTime(), date2.getTime());
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean sameDateYYYYMM(long date1, long date2) {
        boolean isRet = false;
        try {
            Calendar tCal1 = Calendar.getInstance();
            Calendar tCal2 = Calendar.getInstance();

            tCal1.setTimeInMillis(date1);
            tCal2.setTimeInMillis(date2);
            if (tCal1.get(Calendar.MONTH) == tCal2.get(Calendar.MONTH)) {
                if (tCal1.get(Calendar.YEAR) == tCal2.get(Calendar.YEAR)) {
                    isRet = true;
                }
            }
        } catch (Exception e) {
            isRet = false;
        }
        return isRet;
    }

    public static boolean sameDateYYYYMMWEEK(Date date1, Date date2) {
        try {
            return sameDateYYYYMMWEEK(date1.getTime(), date2.getTime());
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean sameDateYYYYMMWEEK(long date1, long date2) {
        boolean isRet = false;
        try {
            Calendar tCal1 = Calendar.getInstance();
            Calendar tCal2 = Calendar.getInstance();

            tCal1.setTimeInMillis(date1);
            tCal2.setTimeInMillis(date2);
            if (tCal1.get(Calendar.WEEK_OF_MONTH) == tCal2.get(Calendar.WEEK_OF_MONTH)) {
                if (tCal1.get(Calendar.MONTH) == tCal2.get(Calendar.MONTH)) {
                    if (tCal1.get(Calendar.YEAR) == tCal2.get(Calendar.YEAR)) {
                        isRet = true;
                    }
                }
            }
        } catch (Exception e) {
            isRet = false;
        }
        return isRet;
    }

    public static boolean sameDateYYYYMMDD(Date date1, Date date2) {
        try {
            return sameDateYYYYMMDD(date1.getTime(), date2.getTime());
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean sameDateYYYYMMDD(long date1, long date2) {
        boolean isRet = false;
        try {
            Calendar tCal1 = Calendar.getInstance();
            Calendar tCal2 = Calendar.getInstance();

            tCal1.setTimeInMillis(date1);
            tCal2.setTimeInMillis(date2);
            if (tCal1.get(Calendar.DATE) == tCal2.get(Calendar.DATE)) {
                if (tCal1.get(Calendar.MONTH) == tCal2.get(Calendar.MONTH)) {
                    if (tCal1.get(Calendar.YEAR) == tCal2.get(Calendar.YEAR)) {
                        isRet = true;
                    }
                }
            }
        } catch (Exception e) {
            isRet = false;
        }
        return isRet;
    }

    public static boolean sameDateYYYYMMDDHHMMSS(Calendar cal1, Calendar cal2) {
        try {
            return sameDateYYYYMMDDHHMMSS(cal1.getTimeInMillis(), cal2.getTimeInMillis());
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean sameDateYYYYMMDDHHMMSS(Date date1, Date date2) {
        try {
            return sameDateYYYYMMDDHHMMSS(date1.getTime(), date2.getTime());
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean sameDateYYYYMMDDHHMMSS(long date1, long date2) {
        boolean isRet = false;
        try {
            Calendar tCal1 = Calendar.getInstance();
            Calendar tCal2 = Calendar.getInstance();

            tCal1.setTimeInMillis(date1);
            tCal2.setTimeInMillis(date2);
            if (tCal1.get(Calendar.SECOND) == tCal2.get(Calendar.SECOND)) {
                if (tCal1.get(Calendar.MINUTE) == tCal2.get(Calendar.MINUTE)) {
                    if (tCal1.get(Calendar.HOUR_OF_DAY) == tCal2.get(Calendar.HOUR_OF_DAY)) {
                        if (tCal1.get(Calendar.DATE) == tCal2.get(Calendar.DATE)) {
                            if (tCal1.get(Calendar.MONTH) == tCal2.get(Calendar.MONTH)) {
                                if (tCal1.get(Calendar.YEAR) == tCal2.get(Calendar.YEAR)) {
                                    isRet = true;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            isRet = false;
        }
        return isRet;
    }

    public static boolean sameTimeZone(TimeZone item1, TimeZone item2) {
        boolean isRet = false;
        try {
            isRet = item1.getID().equals(item2.getID());
        } catch (Exception e) {
            isRet = false;
        }
        return isRet;
    }

    public static int getDateToIntYYYYMM(Date date) {
        int ret = -1;
        try {
            Calendar tCal = Calendar.getInstance();
            tCal.setTimeInMillis(date.getTime());
            int tInt = (tCal.get(Calendar.MONTH) + 1);
            ret = Integer.parseInt(tCal.get(Calendar.YEAR) + "" + (tInt > 9 ? tInt : "0" + tInt));
        } catch (Exception e) {
            ret = -1;
        }
        return ret;
    }

    public static int getDateToIntYYYYMMDD(Date date) {
        int ret = -1;
        try {
            Calendar tCal = Calendar.getInstance();
            tCal.setTimeInMillis(date.getTime());
            int tInt = (tCal.get(Calendar.MONTH) + 1);
            ret = Integer.parseInt(tCal.get(Calendar.YEAR) + "" + (tInt > 9 ? tInt : "0" + tInt));
            tInt = tCal.get(Calendar.DATE);
            ret = Integer.parseInt(ret + "" + (tInt > 9 ? tInt : "0" + tInt));
        } catch (Exception e) {
            ret = -1;
        }
        return ret;
    }
}
