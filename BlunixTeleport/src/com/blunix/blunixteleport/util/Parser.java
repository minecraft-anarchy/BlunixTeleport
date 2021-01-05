package com.blunix.blunixteleport.util;

public class Parser {

	public static Double getDoubleFromString(String stringNumber) {
		double number;
		try {
			number = Double.parseDouble(stringNumber);
		} catch (Exception e) {
			return null;
		}
		return number;
	}
	
	public static Integer getIntegerFromString(String stringNumber) {
		Integer number;
		try {
			number = Integer.parseInt(stringNumber);
		} catch (Exception e) {
			return null;
		}
		return number;

	}
}
