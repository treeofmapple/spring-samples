package com.tom.aws.lambda.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@ConditionalOnProperty(name = "jaeger.tracing.enabled", havingValue = "true", matchIfMissing = false)
public class TelemetryConfig {

	@Value("${jaeger.tracing.url}")
	private String url;

	@Value("${jaeger.tracing.ratio:0.1}")
	private double ratio;
	
	@Value("${jaeger.tracing.batch:100}")
	private int size;
	
	@Bean
	OtlpGrpcSpanExporter otlpGrpcSpanExporter() {
		return OtlpGrpcSpanExporter.builder().setEndpoint(url).build();
	}
	
    @Bean
    SdkTracerProvider sdkTracerProvider(OtlpGrpcSpanExporter spanExporter) {
        Sampler sampler = Sampler.traceIdRatioBased(ratio);

        BatchSpanProcessor spanProcessor = BatchSpanProcessor.builder(spanExporter)
                .setMaxExportBatchSize(size)
                .build();
        
        return SdkTracerProvider.builder()
                .addSpanProcessor(spanProcessor)
                .setSampler(sampler)
                .build();
    }
}
