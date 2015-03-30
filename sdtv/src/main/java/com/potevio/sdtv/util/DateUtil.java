package com.potevio.sdtv.util;

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
}
