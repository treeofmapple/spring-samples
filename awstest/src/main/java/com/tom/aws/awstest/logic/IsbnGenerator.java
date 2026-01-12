package com.tom.aws.awstest.logic;

import java.util.Random;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IsbnGenerator {

	private final Random random = new Random();
	
	/*

	 [Prefix] - [Registration group] - [Registrant] - [Publication] - [Check digit]
	 Typical format: 978-1-4028-9462-6
	 
	 	978 or 979: Prefix (Bookland)

		1: Language (1 = English)

		4028: Publisher ID

		9462: Item number

		6: Check digit (calculated using a checksum formula)
	  
	 */
	
	public String generateIsbn13(String prefix, String publisherCode) {
        String base = prefix + publisherCode;

        while (base.length() < 12) {
            base += random.nextInt(10);
        }

        int[] digits = new int[13];
        for (int i = 0; i < 12; i++) {
            digits[i] = Character.getNumericValue(base.charAt(i));
        }

        digits[12] = calculateIsbn13CheckDigit(digits);

        StringBuilder sb = new StringBuilder();
        for (int digit : digits) {
            sb.append(digit);
        }

        return formatIsbn13(sb.toString(), prefix.length(), publisherCode.length());
	}
	
    private int calculateIsbn13CheckDigit(int[] isbn) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += isbn[i] * (i % 2 == 0 ? 1 : 3);
        }
        int mod = sum % 10;
        return mod == 0 ? 0 : 10 - mod;
    }
	
    private String formatIsbn13(String isbn, int prefixLen, int publisherLen) {
        return String.format(
            "%s-%s-%s-%s-%s",
            isbn.substring(0, prefixLen),
            isbn.substring(prefixLen, prefixLen + 1),       
            isbn.substring(prefixLen + 1, prefixLen + 1 + publisherLen),
            isbn.substring(prefixLen + 1 + publisherLen, 12),
            isbn.substring(12)
        );
    }
	
	
}
