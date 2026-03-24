package com.tom.aws.awstest.config.telemetry;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;

public class OpenObserveLogstashAppender extends AppenderBase<ILoggingEvent> {

	private final String url;
	private final String authHeader;
	private final HttpClient client;
	private final Encoder<ILoggingEvent> encoder;

	public OpenObserveLogstashAppender(String url, String username, String password, Encoder<ILoggingEvent> encoder) {
		this.url = url;
		this.encoder = encoder;
		this.client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();

		String auth = username + ":" + password;
		this.authHeader = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		try {
			byte[] jsonBytes = encoder.encode(eventObject);

			String jsonLog = new String(jsonBytes).trim();
			String payload = "[" + jsonLog + "]";

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Content-Type", "application/json").header("Authorization", authHeader)
					.POST(HttpRequest.BodyPublishers.ofString(payload)).build();

			CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,
					HttpResponse.BodyHandlers.ofString());

			response.thenAccept(res -> {
				if (res.statusCode() >= 400) {
					System.err.println("OpenObserve Error: " + res.statusCode() + " " + res.body());
				}
			});

		} catch (Exception e) {
			System.err.println("Failed to connect to OpenObserve");
			addError("Failed to send log to OpenObserve", e);
		}
	}

	@Override
	public void start() {
		if (encoder != null && !encoder.isStarted()) {
			encoder.start();
		}
		super.start();
	}

	@Override
	public void stop() {
		if (encoder != null && encoder.isStarted()) {
			encoder.stop();
		}
		super.stop();
	}

}
