package uy.com.agm.gaston.soporte.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class DateHelper {
	public static Date getHoy() {
		return new Date();
	}

	public static Date getYesterday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return new Date(calendar.getTimeInMillis());
	}

	public static String formatDate(Date date) {
		String day, month, year;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		day = StringUtils.leftPad(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)), 2, "0");
		month = StringUtils.leftPad(Integer.toString(calendar.get(Calendar.MONTH) + 1), 2, "0");
		year = StringUtils.leftPad(Integer.toString(calendar.get(Calendar.YEAR)), 2, "0");

		return year + "-" + month + "-" + day;
	}

	public static String formatDateTime(Date date) {
		String hour, minute;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		hour = StringUtils.leftPad(Integer.toString(calendar.get(Calendar.HOUR)), 2, "0");
		minute = StringUtils.leftPad(Integer.toString(calendar.get(Calendar.MINUTE)), 2, "0");

		return formatDate(date) + " " + hour + "hs" + " " + minute + "ms";
	}

	public static Boolean equals(Date date1, Date date2) {
		int day1, month1, year1, day2, month2, year2;

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date1);
		day1 = calendar.get(Calendar.DAY_OF_MONTH);
		month1 = calendar.get(Calendar.MONTH) + 1;
		year1 = calendar.get(Calendar.YEAR);

		calendar.setTime(date2);
		day2 = calendar.get(Calendar.DAY_OF_MONTH);
		month2 = calendar.get(Calendar.MONTH) + 1;
		year2 = calendar.get(Calendar.YEAR);

		return day1 == day2 && month1 == month2 && year1 == year2;
	}

	public static Long diffMillis(Date date1, Date date2) {
		Long m1, m2;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date1);
		m1 = calendar.getTimeInMillis();
		calendar.setTime(date2);
		m2 = calendar.getTimeInMillis();
		return m2 - m1;
	}

	public static Date addSeconds(Date date, Integer seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTime();
	}
}
