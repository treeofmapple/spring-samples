package com.tom.service.datagen.service.interfaces;

import java.util.List;

@FunctionalInterface
public interface BatchCallback<T> {
	void onBatchComplete(List<T> batch, int batchNum, int totalBatches);
}
