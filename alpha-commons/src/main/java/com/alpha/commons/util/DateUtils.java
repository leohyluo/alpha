package com.alpha.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static float getAge(Date date) {
		LocalDate birth = dateToLocalDate(date);
		return getAge(birth);
	}
	
	public static float getAge(LocalDate birth) {
		LocalDate today = LocalDate.now();
		long dayDiff = ChronoUnit.DAYS.between(birth, today);
		float dayUnit = 365;
		float age = dayDiff / dayUnit;
		float result = (float)(Math.round(age*10))/10;
		return result;
	}
	
	public static float getDiffMonth(Date birth) {
		float monthUnit = 30;
		LocalDate date = dateToLocalDate(birth);
		LocalDate today = LocalDate.now();
		long dayDiff = ChronoUnit.DAYS.between(date, today);
		float month = dayDiff / monthUnit;
		float result = (float)(Math.round(month * 10)) / 10;
		System.out.println(result);
		return result;
	}
	
	public static LocalDate dateToLocalDate(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
		LocalDate localDate = localDateTime.toLocalDate();
		return localDate;
	}
	
	public static Date stringToDate(String dateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		LocalDate localDate = LocalDate.parse(dateStr, formatter);
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zoneDateTime = localDate.atStartOfDay(zoneId);
		Date date = Date.from(zoneDateTime.toInstant());
		return date;
	}
	
	public static Date string2Date(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Date date = sdf.parse(dateStr);
		return date;
	}
	
	public static Date string2DateTime(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		Date date = sdf.parse(dateStr);
		return date;
	}
	
	/*public static void main(String[] args) {
		Date date;
		try {
			date = string2Date("2017-8-11");
			getDiffMonth(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
