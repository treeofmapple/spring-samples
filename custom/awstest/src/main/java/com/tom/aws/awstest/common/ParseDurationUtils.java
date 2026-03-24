package com.tom.aws.awstest.common;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class ParseDurationUtils {

	public long parseDurationMicroseconds(String duration) {
		long millis = 0L;
		String regex = "(\\d+)([smhdwSMHDW])";
		var matcher = Pattern.compile(regex).matcher(duration);
		while (matcher.find()) {
			long value = Long.parseLong(matcher.group(1));
			switch (matcher.group(2).toUpperCase()) {
			case "S" -> millis += value * 1000;
			case "M" -> millis += value * 60 * 1000;
			case "H" -> millis += value * 60 * 60 * 1000;
			case "D" -> millis += value * 24 * 60 * 60 * 1000;
			case "W" -> millis += value * 7 * 24 * 60 * 60 * 1000;
			default -> throw new IllegalArgumentException("Invalid time unit in duration: " + matcher.group(2));
			}
		}
		return millis;
	}

	public long parseDurationSeconds(String duration) {
		long millis = 0L;
		String regex = "(\\d+)([smhdwSMHDW])";
		var matcher = Pattern.compile(regex).matcher(duration);
		while (matcher.find()) {
			long value = Long.parseLong(matcher.group(1));
			switch (matcher.group(2).toUpperCase()) {
			case "S" -> millis += value;
			case "M" -> millis += value * 60;
			case "H" -> millis += value * 60 * 60;
			case "D" -> millis += value * 24 * 60 * 60;
			case "W" -> millis += value * 7 * 24 * 60 * 60;
			default -> throw new IllegalArgumentException("Invalid time unit in duration: " + matcher.group(2));
			}
		}
		return millis;
	}

}
