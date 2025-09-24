package com.tom.service.datagen.old;

public class OldTooling {

	/*
	
	public boolean isClientConnected(HttpServletRequest request) {
		try {
			if (!request.isAsyncStarted() && request.getInputStream().available() == 0) {
				ServiceLogger.warn("Client aborted the request before processing");
				return false;
			}
		} catch (Exception e) {
			ServiceLogger.error("Error checking client connection", e);
			return false;
		}
		return true;
	}

	@PostMapping(value = "/employee/progress/{quantity}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> dataGenerationWithProgress(@PathVariable int quantity) {
		return service.generateEmployeeDataWithProgress(quantity).map(data -> "Progress: " + data)
				.concatWith(Mono.just("Completed. Download your file at: /api/v1/employee/download/"));
	}

	public Flux<String> generateEmployeeDataWithProgress(int quantity) {
		return Flux.create(sink -> {
			AtomicInteger progress = new AtomicInteger(0);

			Flux.range(0, (int) Math.ceil((double) quantity / batchSize)).flatMap(i -> {
				List<Employee> batch = new ArrayList<>(batchSize);
				for (int j = 0; j < batchSize && (i * batchSize + j) < quantity; j++) {
					batch.add(data.generateSingleEmployee());
				}
				return Flux.just(batch);
			}).doOnNext(batch -> {
				byte[] csvData = operations.convertToCSV(batch);
				String batchId = operations.generateRandomUUID();
				csvStorage.put(batchId, csvData);
				progress.addAndGet(batch.size());
				sink.next("Progress: " + progress.get() + "/" + quantity);
			}).doOnComplete(() -> {
				sink.complete();
				ServiceLogger.info("Completed data generation.");
			}).subscribe();
		});
	}

	*/
	
}

