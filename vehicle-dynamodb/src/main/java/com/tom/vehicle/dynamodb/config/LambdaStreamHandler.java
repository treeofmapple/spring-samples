package com.tom.vehicle.dynamodb.config;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tom.vehicle.dynamodb.VehicleApplication;

public class LambdaStreamHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(VehicleApplication.class);
        } catch (ContainerInitializationException e) {
            throw new RuntimeException("Could not initialize Spring", e);
        }
    }

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest request, Context context) {
        return handler.proxy(request, context);
    }
    
}