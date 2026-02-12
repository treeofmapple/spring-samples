package com.tom.mail.sender.logic.mail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.tom.mail.sender.exception.system.InternalException;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MailContentProcessor {

	private final Parser parser = Parser.builder().build();
	private final HtmlRenderer renderer = HtmlRenderer.builder().build();
	private final Configuration freemarkerConfig;

	public byte[] processEmailTemplate(String templateName, Map<String, Object> model) {
		try {
			Template template = freemarkerConfig.getTemplate(templateName);
			String processedText = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

			String finalHtml;
	        if (templateName.endsWith(".md")) {
	            finalHtml = convertMarkdownToHtml(processedText);
	        } else {
	            finalHtml = processedText;
	        }
	        return finalHtml.getBytes(StandardCharsets.UTF_8);
		} catch (RuntimeException | IOException | TemplateException e) {
			throw new InternalException(String.format("Failed to process the email template", e));
		}
	}

	private String convertMarkdownToHtml(String md) {
		var document = parser.parse(md);
		return renderer.render(document);
	}
}