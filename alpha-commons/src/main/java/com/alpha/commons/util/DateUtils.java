package com.alpha.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DateUtils {

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/*public static float getAge(Date date) {
		LocalDate birth = dateToLocalDate(date);
		return getAge(birth);
	}*/
	
	public static float getAge(LocalDate birth) {
		LocalDate today = LocalDate.now();
		long dayDiff = ChronoUnit.DAYS.between(birth, today);
		float dayUnit = 365;
		float age = dayDiff / dayUnit;
		float result = (float)(Math.round(age*10))/10;
		return result;
	}
	
	public static float getAge(Date date) {
		LocalDate birth = dateToLocalDate(date);
		LocalDate today = LocalDate.now();
		long year = ChronoUnit.YEARS.between(birth, today);
		LocalDate birth2 = birth.plusYears(year);
		long diffDay = ChronoUnit.DAYS.between(birth2, today);
		int lengthOfYear = 365;
		if(today.isLeapYear()) {
			lengthOfYear = 366;
		}
		float result = year + ((float)diffDay / (float)lengthOfYear);
		return result;
	}
	
	public static String getAgeText(Date date) {
		LocalDate birth = dateToLocalDate(date);
		LocalDate today = LocalDate.now();
		long year = ChronoUnit.YEARS.between(birth, today);
		LocalDate birth2 = birth.plusYears(year);
		long diffMonth = ChronoUnit.MONTHS.between(birth2, today);
		String result = "";
		if(year == 0) {
			result = diffMonth + "个月";
			return result;
		} else if (diffMonth == 0) {
			result = year + "岁";
			return result;
		} else {
			result = year + "岁" + diffMonth + "个月";
		}
		return result;
	}
	
	public static int getAgeByBirth(Date date) {
		LocalDate birthDay = dateToLocalDate(date);
		LocalDate now = LocalDate.now();
		Period period = Period.between(birthDay, now);
		// 相距多少年
		int yearsBetween = period.getYears();
		return yearsBetween;
	}
	
	public static float getAgeOld(Date date) {
		LocalDate birthDay = dateToLocalDate(date);
		LocalDate now = LocalDate.now();
		Period period = Period.between(birthDay, now);
		// 相距多少年
		int yearsBetween = period.getYears();
		// 相距年超过1
		if (yearsBetween > 1)
			yearsBetween = yearsBetween - 1;
		// 处理出生年与现在年的年份，出生的年占了几分之几年，现在年同理
		int daysOfbirthYear = birthDay.getDayOfYear(); // 出生日期在出生年的天数
		int lengthOfBirthYear = birthDay.lengthOfYear(); // 年长度
		int daysOfCurrentYear = now.getDayOfYear(); // 现在日期在现在年的天数
		int lengthOfCurrentYear = now.lengthOfYear(); // 年长度

		double age = 0;
		double mergeDays = (lengthOfBirthYear + lengthOfCurrentYear) / (double) 2;
		double mergeYearsOfBirth = (lengthOfBirthYear - daysOfbirthYear - 1) / mergeDays;
		double mergeYearsOfCurrent = (daysOfCurrentYear - 1) / mergeDays;
		if (yearsBetween == 0) {
			age = yearsBetween + (double) (lengthOfBirthYear - daysOfbirthYear) / lengthOfBirthYear;
		} else {
			age = yearsBetween + mergeYearsOfBirth + mergeYearsOfCurrent;
		}
		float result = (float)(Math.round(age*1000))/1000;
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
		LocalDateTime localDateTime = dateToLocalDateTime(date);
		LocalDate localDate = localDateTime.toLocalDate();
		return localDate;
	}
	
	public static LocalDateTime dateToLocalDateTime(Date date) {
		if(date==null)
			return  LocalDateTime.now();
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
		return localDateTime;
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
	
	public static String date2String(Date date, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		String result = "";
		if(pattern.equals(DATE_FORMAT)) {
			LocalDate datetime = dateToLocalDate(date);
			result = datetime.format(formatter);
		} else {
			LocalDateTime datetime = dateToLocalDateTime(date);
			result = datetime.format(formatter);
		}
		return result;
	}
	
	public static Date string2DateTime(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		Date date = sdf.parse(dateStr);
		return date;
	}

	public static List<Integer> listMonth(int minValue,int maxValue){
		List<Integer> result = new ArrayList<>();
		int thisValue = minValue;
		while (thisValue != maxValue) {
			//当前值等于最大月份 设置为最小月份
			if (thisValue > 12)
				thisValue = 1;
			result.add(thisValue++);
		}
		result.add(maxValue);
		return result;
	}
}
