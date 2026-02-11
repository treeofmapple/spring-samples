package com.tom.mail.sender.logic.mail;

import org.springframework.stereotype.Component;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

@Component
public class MailContentProcessor {

	private final Parser parser;
	private final HtmlRenderer renderer;

	public MailContentProcessor() {
		MutableDataSet options = new MutableDataSet();
		this.parser = Parser.builder(options).build();
		this.renderer = HtmlRenderer.builder(options).build();
	}

	public String processContent(String content) {
		if (content == null)
			return "";

		if (content.trim().startsWith("<") && content.trim().endsWith(">")) {
			return content;
		}

		if (content.contains("# ") || content.contains("**") || content.contains("[")) {
			return convertMarkdownToHtml(content);
		}

		return content;
	}

	private String convertMarkdownToHtml(String md) {
		var document = parser.parse(md);
		return renderer.render(document);
	}
}