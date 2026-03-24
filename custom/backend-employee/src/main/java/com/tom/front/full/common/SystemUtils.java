package com.tom.front.full.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
