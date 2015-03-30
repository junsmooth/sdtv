package com.potevio.sdtv.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String date2String(String format, Date date) {
		SimpleDateFormat sfDateFormat = new SimpleDateFormat(format);
		return sfDateFormat.format(date);
	}

	public static String currentDateString(String format) {
		return date2String(format, new Date());
	}

	public static String currentCommoneDateString() {
		return currentDateString("yyyy-MM-dd HH:mm:ss");
	}

	public static Date string2Date(String dateTime) throws ParseException {
		SimpleDateFormat sfDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return sfDateFormat.parse(dateTime);

	}
}
