package com.tom.service.datagen.service.interfaces;

import java.util.List;

import com.tom.service.datagen.model.Employee;

@FunctionalInterface
public interface BatchCallback {
	void onBatchComplete(List<Employee> batch, int batchNum, int totalBatches);
}
