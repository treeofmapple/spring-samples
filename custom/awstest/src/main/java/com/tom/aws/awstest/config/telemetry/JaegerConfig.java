package com.tom.aws.awstest.config.telemetry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;

@Configuration
@ConditionalOnProperty(name = "jaeger.enabled", havingValue = "true", matchIfMissing = true)
public class JaegerConfig {

	@Value("${jaeger.url}")
	private String url;

	@Value("${jaeger.ratio:0.1}")
	private double ratio;
	
	@Value("${jaeger.batch:100}")
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
