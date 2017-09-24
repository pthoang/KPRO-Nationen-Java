package util;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Helper functions for handling dates.
 * 
 * @author doraoline
 *
 */
public class DateUtil {
	
	private static final String DATE_FORMAT = "dd.MM.yyyy";
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
	
	public static String dateToString(LocalDate date) {
		if (date == null) {
			return null;
		}
		return DATE_FORMATTER.format(date);
	}
	
	public static LocalDate stringToDate(String dateString) {
		try {
			return DATE_FORMATTER.parse(dateString, LocalDate::from);
		} catch (DateTimeParseException e) {
			return null;
		}
	}
	
	public static boolean validDate(String dateString) {
		return DateUtil.stringToDate(dateString) != null;
	}
	
	public static LocalDate dateToLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public static Date LocalDateToDate(LocalDate date) {
		return java.sql.Date.valueOf(date);
	}

}
