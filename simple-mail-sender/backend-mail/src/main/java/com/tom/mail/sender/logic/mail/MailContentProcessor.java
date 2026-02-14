package com.tom.mail.sender.logic.mail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.tom.mail.sender.exception.system.InternalException;
import com.tom.mail.sender.global.constraints.MailConstraints;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MailContentProcessor {

	private final Parser parser = Parser.builder().build();
	private final HtmlRenderer renderer = HtmlRenderer.builder().build();

	// Only .txt files

	private Map<String, String> parseVariables(MultipartFile variablesFile) throws IOException {
		String content = new String(variablesFile.getBytes(), StandardCharsets.UTF_8);
		Map<String, String> map = new HashMap<>();

		content.lines().filter(line -> line != null && line.contains(":")).forEach(line -> {
			String[] parts = line.split(":", 2);
			map.put(parts[0].trim(), parts[1].trim());
		});
		return map;
	}

	public byte[] processEmailTemplate(MultipartFile file, MultipartFile variablesFile) {
		try {
			String templateName = file.getOriginalFilename();
	        String variablesName = variablesFile.getOriginalFilename();
			
			if (templateName == null || !templateName.toLowerCase().endsWith(MailConstraints.TEMPLATE_FILE_EXTENSION)) {
	            throw new InternalException("Invalid template file. Only .md files are allowed.");
	        }

			Boolean isValidVariableFile = false;
			for (String ext : MailConstraints.VARIABLES_FILE_EXTENSION) {
				if (variablesName != null && variablesName.toLowerCase().endsWith(ext)) {
					isValidVariableFile = true;
					break;
				}
			}

			if (!isValidVariableFile) {
				throw new InternalException("Invalid variables file. Allowed: "
						+ Arrays.toString(MailConstraints.VARIABLES_FILE_EXTENSION));
	        }
			
	        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
	        var parseVariables = parseVariables(variablesFile);

			if (parseVariables != null) {
				for (Map.Entry<String, String> entry : parseVariables.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					content = content.replace("${" + key + "}", value);
					content = content.replace("{{" + key + "}}", value);
				}
			}
					
			var document = parser.parse(content);
			return renderer.render(document).getBytes(StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new InternalException(String.format("Failed to process the email template:", e.getMessage()));
		}
	}
}