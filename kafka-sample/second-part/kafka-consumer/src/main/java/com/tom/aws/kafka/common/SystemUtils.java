package com.tom.aws.kafka.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class SystemUtils {

	public String getLocalMachineIp() throws Exception {
		return InetAddress.getLocalHost().getHostAddress();	
	}
	
	@SuppressWarnings("deprecation")
	public String getMachinePublicIp() throws IOException, Exception {
		URL url = new URL("https://api.ipify.org");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", "spring-boot-application");
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
			return reader.readLine();
		}
	}

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
			case "S" -> millis += value ; 
			case "M" -> millis += value * 60 ;
			case "H" -> millis += value * 60 * 60 ;
			case "D" -> millis += value * 24 * 60 * 60 ;
			case "W" -> millis += value * 7 * 24 * 60 * 60 ;
			default -> throw new IllegalArgumentException("Invalid time unit in duration: " + matcher.group(2));
			}
		}
		return millis;
	}
	
	protected void getBannerPathResource(PrintStream out) {
		ClassPathResource resource = new ClassPathResource("banner/banner.txt");
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
