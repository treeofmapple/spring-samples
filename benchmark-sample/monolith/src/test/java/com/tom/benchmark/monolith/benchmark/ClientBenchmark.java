package com.tom.benchmark.monolith.benchmark;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
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
public class ClientBenchmark {

	@Value("${application.test.client-count:1000}")
    private int CLIENT_COUNT;
	
	private ConfigurableApplicationContext context;
    private ClientRepository clientRepository;
    
    private List<Long> clientIds;
    private Random random = new Random();
    private Faker faker;
	
    @Setup(Level.Trial)
    public void setup() {
        long startTime = System.currentTimeMillis();
    	
        context = new SpringApplicationBuilder(MonolithApplication.class)
                .web(WebApplicationType.NONE)
                .run();
        
        clientRepository = context.getBean(ClientRepository.class);
        faker = new Faker();
        
        List<Client> clientsToSave = new ArrayList<>(CLIENT_COUNT);
        for (int i = 0; i < CLIENT_COUNT; i++) {
            clientsToSave.add(new Client(
            		null, 
            		faker.name().fullName(), 
            		faker.number().digits(11)));
        }
        clientIds = clientRepository.saveAll(clientsToSave).stream().map(Client::getId).toList();
        
        long endTime = System.currentTimeMillis();
        long durationInMs = endTime - startTime;
        
        ServiceLogger.info("Total Data Generation Setup Took: " + durationInMs + " ms");
    }
	
    // crud
    
    @TearDown(Level.Trial)
    public void tearDown() {
        if (context != null) {
            clientRepository.deleteAll();
            context.close();
        }
    }
    
}
