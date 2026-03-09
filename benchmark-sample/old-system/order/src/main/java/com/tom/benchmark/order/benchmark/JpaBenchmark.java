package com.tom.benchmark.order.benchmark;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.tom.benchmark.order.OrderApplication;
import com.tom.benchmark.order.common.ServiceLogger;
import com.tom.benchmark.order.dto.ClientRequest;
import com.tom.benchmark.order.dto.ClientResponse;
import com.tom.benchmark.order.dto.ProductRequest;
import com.tom.benchmark.order.dto.ProductResponse;
import com.tom.benchmark.order.openfeign.ClientServiceFeign;
import com.tom.benchmark.order.openfeign.ProductServiceFeign;

import net.datafaker.Faker;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 20, time = 5, timeUnit = TimeUnit.SECONDS)
@Fork(value = 0)
public class JpaBenchmark {

	@Value("${application.test.client-count:1000}")
	private int CLIENT_COUNT;

	@Value("${application.test.product-count:4000}")
	private int PRODUCT_COUNT;

	@Value("${application.test.data-check.repetitions:100}")
	private int LOOKUP_REPETITIONS;

	private ConfigurableApplicationContext context;
	private ClientServiceFeign clientServiceFeign;
	private ProductServiceFeign productServiceFeign;

	private List<String> clientCpfs;
	private List<String> productSkus;
	private Random random = new Random();
	private Faker faker;

	@Setup(Level.Trial)
	public void setup() {
		long startTime = System.currentTimeMillis();

		context = new SpringApplicationBuilder(OrderApplication.class).web(WebApplicationType.NONE).run();

		clientServiceFeign = context.getBean(ClientServiceFeign.class);
		productServiceFeign = context.getBean(ProductServiceFeign.class);
		faker = new Faker();

		clientCpfs = new ArrayList<>(CLIENT_COUNT);
		for (int i = 0; i < CLIENT_COUNT; i++) {
			ClientRequest clientRequest = new ClientRequest(faker.name().fullName(), faker.number().digits(11));
			ClientResponse clientResponse = clientServiceFeign.createClient(clientRequest);
			clientCpfs.add(clientResponse.cpf());
		}
		
		productSkus = new ArrayList<>(PRODUCT_COUNT);
		for (int i = 0; i < PRODUCT_COUNT; i++) {
			ProductRequest productRequest = new ProductRequest(
					"SKU-" + i + "-" + faker.random().hex(10),
					faker.commerce().productName(),
					faker.lorem().sentence(),
					new BigDecimal(faker.commerce().price()));
			ProductResponse productResponse = productServiceFeign.createProduct(productRequest);
			productSkus.add(productResponse.sku());
		}
        long endTime = System.currentTimeMillis();
        long durationInMs = endTime - startTime;
		
		ServiceLogger.info("Total Data Generation Setup Took: " + durationInMs + " ms");
	}

	@TearDown(Level.Trial)
	public void tearDown() {
		if (context != null) {
			clientCpfs.forEach(clientServiceFeign::deleteClient);
			productSkus.forEach(productServiceFeign::deleteProduct);
			context.close();
		}
	}

	@Benchmark
	public void findRandomClientByCPF(Blackhole bh) {
		for (int i = 0; i < LOOKUP_REPETITIONS; i++) {
			String randomCpf = clientCpfs.get(random.nextInt(CLIENT_COUNT));
			bh.consume(clientServiceFeign.findByCpf(randomCpf));
		}
	}

	@Benchmark
	public void findRandomProductBySKU(Blackhole bh) {
		for (int i = 0; i < LOOKUP_REPETITIONS; i++) {
			String randomSku = productSkus.get(random.nextInt(PRODUCT_COUNT));
			bh.consume(productServiceFeign.findBySku(randomSku));
		}
	}

}
