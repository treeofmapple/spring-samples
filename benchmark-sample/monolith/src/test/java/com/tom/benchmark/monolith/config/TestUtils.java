package com.tom.benchmark.monolith.config;

import java.io.File;

public class TestUtils {

	public static void storeContentOnFolder(String folderPath, String filePath) {
		File dir = new File(folderPath);
		if (!dir.exists()) {
			if (dir.mkdirs()) {
				System.out.println("[INFO] Created output directory: " + folderPath);
			} else {
				System.out.println("[WARN] Could not create output directory: " + folderPath);
			}
		}

		File outputFile = new File(filePath);
		if (outputFile.exists()) {
			if (outputFile.delete()) {
				System.out.println("[INFO] Previous results file deleted: " + filePath);
			} else {
				System.out.println("[WARN] Could not delete existing results file: " + filePath);
			}
		}
	}

}
