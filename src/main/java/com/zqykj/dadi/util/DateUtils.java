package com.zqykj.dadi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fengwei on 17/5/1.
 */
public class DateUtils {


    /**
     * 将带 MM-dd 的字符串格式化为 yyyy-MM-dd 的日期
     *
     * @param publish_date，例如：05-09发布
     * @return
     */
    public static String completeDate(String publish_date) {
        String date = "";
        if (publish_date != null && !"".equals(publish_date)) {
            date = publish_date.substring(0, publish_date.indexOf("-") + 3);
            date = getYear() + "-" + date;
        }
        return date;

    }

    /**
     * 取当前的年份，格式为 yyyy
     *
     * @return
     */
    public static int getYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    /**
     * 将时间字符串格式化为 yyyy-MM-dd
     *
     * @param time
     * @return
     */
    public static String formatDate(String time) {
        String re = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            re = sdf.format(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return re;
    }

    /**
     * 基于当天的日期，取加减yearAmount（多少年）、monthAmount（多少月）、dayAmount（多少天）后的日期
     *
     * @param yearAmount
     * @param monthAmount
     * @param dayAmount
     * @return
     */
    public static String getDate(Integer yearAmount, Integer monthAmount, Integer dayAmount) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, yearAmount);
        calendar.add(Calendar.MONTH, monthAmount);
        calendar.add(Calendar.DAY_OF_MONTH, dayAmount);
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取当前日期：yyyy-MM-dd
     *
     * @return
     */
    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间：HH:mm:ss
     *
     * @return
     */
    public static String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
