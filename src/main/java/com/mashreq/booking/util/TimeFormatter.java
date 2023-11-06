package com.mashreq.booking.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeFormatter {
    public static Instant parseDateTime(String date) {
        try {
            int length = date.length();
            String format = length == 16 ? "yyyy-MM-dd HH:mm" : "yyyy-MM-dd HH:mm:ss";

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
            return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        } catch (Exception e) {
            return Instant.parse(date);
        }
    }
}
