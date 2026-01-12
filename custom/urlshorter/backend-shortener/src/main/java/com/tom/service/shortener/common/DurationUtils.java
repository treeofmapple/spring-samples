package com.tom.service.shortener.common;

import java.time.Duration;

import org.springframework.stereotype.Component;

@Component
public class DurationUtils {

    public static Duration parseDuration(String durationStr) {
        if (durationStr == null || durationStr.isBlank()) {
            throw new IllegalArgumentException("Duration string cannot be null or empty.");
        }

        durationStr = durationStr.trim().toLowerCase();

        char unit = durationStr.charAt(durationStr.length() - 1);
        String numberPart = durationStr.substring(0, durationStr.length() - 1).trim();
        long value;

        try {
            value = Long.parseLong(numberPart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number in duration: " + numberPart, e);
        }

        return switch (unit) {
        	case 'c' -> Duration.ofMillis(value);
            case 's' -> Duration.ofSeconds(value);
            case 'm' -> Duration.ofMinutes(value);
            case 'h' -> Duration.ofHours(value);
            case 'd' -> Duration.ofDays(value);
            default -> throw new IllegalArgumentException("Unsupported time unit: " + unit
                    + ". Supported units: c(millisecond), s (seconds), m (minutes), h (hours), d (days).");
        };
    }
}