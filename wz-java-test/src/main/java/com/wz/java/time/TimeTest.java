package com.wz.java.time;

import java.io.Serializable;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Locale;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.nextOrSame;

/**
 * LocalDate、LocalTime、Instant、Duration 以及 Period
 * wangzhen23
 * 2017/7/26.
 */
public class TimeTest {
    public static void main(String[] args) {
        //2014-07-26
        LocalDate date = LocalDate.of(2017, 7, 26);
        //2014
        int year = date.getYear();
        //JULY
        Month month = date.getMonth();
        int day = date.getDayOfMonth();
        //WEDNESDAY
        DayOfWeek dow = date.getDayOfWeek();
        int len = date.lengthOfMonth();
        boolean leap = date.isLeapYear();
        System.out.println("Date:" + date + " ,year:" + year + " ,month:" + month
                + " ,day:" + day + " ,DayofWeek:" + dow + " ,len:" + len
             + " ,leap:" + leap);

        LocalDate today = LocalDate.now();
        LocalTime.now();

        LocalTime time = LocalTime.of(13, 45, 20);
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();
        System.out.println("hour:" + hour + " ,minute:" + minute + " ,second:" + second);

        LocalDate date1 = LocalDate.parse("2014-03-18");
        LocalTime time1 = LocalTime.parse("13:45:20");
        // 2014-03-18T13:45:20
        LocalDateTime dt1 = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45, 30);
        LocalDateTime dt2 = LocalDateTime.of(date1, time1);
        LocalDateTime dt3 = date.atTime(13, 45, 20);
        LocalDateTime dt4 = date.atTime(time1);
        LocalDateTime dt5 = time.atDate(date1);

        LocalDate date2 = dt1.toLocalDate();
        LocalTime time2 = dt1.toLocalTime();

        Instant instant1 = Instant.ofEpochSecond(3);
        Instant instant2 = Instant.ofEpochSecond(3, 0);
        Instant.ofEpochSecond(2, 1_000_000_000);
        Instant.ofEpochSecond(4, -1_000_000_000);

        Duration d1 = Duration.between(time1, time2);
        Duration d2 = Duration.between(dt1, dt2);
        Duration d3 = Duration.between(instant1, instant2);
        d1.getSeconds();
        System.out.println("d2:" + d2.getSeconds());
        System.out.println("time:" + time.toString());
        Temporal timeModified = d2.addTo(time);// time 不可变
        System.out.println(d2.getSeconds() + " time:" + time.toString() + " ,after:" + timeModified);

        Period tenDays = Period.between(LocalDate.of(2014, 3, 8),
                LocalDate.of(2014, 3, 18));
        System.out.println("days:" + tenDays.getDays());

        Duration threeMinutes = Duration.ofMinutes(3);
        Duration threeMinutes1 = Duration.of(3, ChronoUnit.MINUTES);
        Period tenDays1 = Period.ofDays(10);
        Period threeWeeks = Period.ofWeeks(3);
        Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);


        LocalDate date11 = LocalDate.of(2017, 7, 26);
        LocalDate date22 = date11.with(nextOrSame(DayOfWeek.WEDNESDAY));
        LocalDate date3 = date22.with(lastDayOfMonth());
        System.out.println(date11.toString() + "--" + date22.toString() + "++" + date3);

        LocalDate date0 = LocalDate.of(2014, 3, 18);
        String s1 = date0.format(DateTimeFormatter.BASIC_ISO_DATE);
        String s2 = date0.format(DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println(s1 + " ------------" + s2);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date12 = LocalDate.of(2014, 3, 18);
        String formattedDate = date1.format(formatter);
        LocalDate date23 = LocalDate.parse(formattedDate, formatter);

        DateTimeFormatter italianFormatter =
                DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ITALIAN);
        LocalDate date01 = LocalDate.of(2014, 3, 18);
        String formattedDate0 = date01.format(italianFormatter); // 18. marzo 2014
        LocalDate date02 = LocalDate.parse(formattedDate0, italianFormatter);
        System.out.println("italian:" + formattedDate0);

    }
}
