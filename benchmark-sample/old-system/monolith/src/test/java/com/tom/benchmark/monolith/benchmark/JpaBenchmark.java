package com.tom.benchmark.monolith.benchmark;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.tom.benchmark.monolith.MonolithApplication;
import com.tom.benchmark.monolith.annotations.RBenchmarkRunner;
import com.tom.benchmark.monolith.client.Client;
import com.tom.benchmark.monolith.client.ClientRepository;
import com.tom.benchmark.monolith.config.ServiceLogger;
import com.tom.benchmark.monolith.product.Product;
import com.tom.benchmark.monolith.product.ProductRepository;

import net.datafaker.Faker;

@State(Scope.Benchmark)
@RBenchmarkRunner
public class JpaBenchmark {

	@Value("${application.test.product-count:4000}")
	private int PRODUCT_COUNT;
    
	@Value("${application.test.data-check.repetitions:100}")
	private int LOOKUP_REPETITIONS;
	
	private ConfigurableApplicationContext context;
    private ClientRepository clientRepository;
    private ProductRepository productRepository;
    
    private List<Long> clientIds;
    private List<Long> productIds;
    private Random random = new Random();
    private Faker faker;
    
    @Setup(Level.Trial)
    public void setup() {
        long startTime = System.currentTimeMillis();
    	
        context = new SpringApplicationBuilder(MonolithApplication.class)
                .web(WebApplicationType.NONE)
                .run();
        
        clientRepository = context.getBean(ClientRepository.class);
        productRepository = context.getBean(ProductRepository.class);
        faker = new Faker();
        
        List<Client> clientsToSave = new ArrayList<>(CLIENT_COUNT);
        for (int i = 0; i < CLIENT_COUNT; i++) {
            clientsToSave.add(new Client(
            		null, 
            		faker.name().fullName(), 
            		faker.number().digits(11)));
        }
        clientIds = clientRepository.saveAll(clientsToSave).stream().map(Client::getId).toList();
        
        List<Product> productsToSave = new ArrayList<>(PRODUCT_COUNT);
        for (int i = 0; i < PRODUCT_COUNT; i++) {
            productsToSave.add(new Product(
                    null,
                    "SKU-" + i + "-" + faker.random().hex(10),
                    faker.commerce().productName(),
                    faker.lorem().sentence(),
                    new BigDecimal(faker.commerce().price())
            ));
        }

        for(int i=0; i < productsToSave.size(); i++) {
            productsToSave.get(i).setSku("SKU-" + i + "-" + faker.random().hex(10));
        }
        
        productIds = productRepository.saveAll(productsToSave).stream().map(Product::getId).toList();
        long endTime = System.currentTimeMillis();
        long durationInMs = endTime - startTime;
        
        ServiceLogger.info("Total Data Generation Setup Took: " + durationInMs + " ms");
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        if (context != null) {
            clientRepository.deleteAll();
            productRepository.deleteAll();
            context.close();
        }
    }
    
    @Benchmark
    public void findRandomClientById(Blackhole bh) { 
        for (int i = 0; i < LOOKUP_REPETITIONS; i++) {
            Long randomId = clientIds.get(random.nextInt(CLIENT_COUNT));
            bh.consume(clientRepository.findById(randomId).orElse(null));
        }
    }

    @Benchmark
    public void findRandomProductById(Blackhole bh) {
        for (int i = 0; i < LOOKUP_REPETITIONS; i++) {
            Long randomId = productIds.get(random.nextInt(PRODUCT_COUNT));
            bh.consume(productRepository.findById(randomId).orElse(null));
        }
    }
    
}
