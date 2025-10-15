package com.tom.aws.lambda.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.tom.aws.lambda.AwsLambdaSampleApplication;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LambdaStreamHandler implements RequestStreamHandler {

	private static final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
	
	static {
		try {
			handler = new SpringBootProxyHandlerBuilder<AwsProxyRequest>()
					.defaultProxy()
					.springBootApplication(AwsLambdaSampleApplication.class)
					.buildAndInitialize();
		} catch (ContainerInitializationException e) {
			throw new RuntimeException("Could not initialize Spring App", e);
		}
	}
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		handler.proxyStream(input, output, context);
	}
	
}
