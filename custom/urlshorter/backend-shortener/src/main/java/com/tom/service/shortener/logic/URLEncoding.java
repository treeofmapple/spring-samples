package com.tom.service.shortener.logic;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class URLEncoding {

	@Value("${application.encoding.key-length:10}")
	private int key_length = 10;

	private final String Alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private final char[] Characters = Alphabet.toCharArray();

	private final SecureRandom secureRandom = new SecureRandom();

	public String generateURL() {
		return generateShortKey();
	}

	public String generateShortKey() {
		return generateShortKey(key_length);
	}

	public String generateShortKey(int length) {
		StringBuilder shortKey = new StringBuilder();
		for (int i = 0; i < length; i++) {
			shortKey.append(Characters[secureRandom.nextInt(Characters.length)]);
		}
		return shortKey.toString();
	}

}