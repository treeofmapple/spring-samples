package com.tom.first.elastic.service.search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Component;

import com.tom.first.elastic.models.vehicle.VehicleDocument;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VehicleSearchUtil {

	private final ElasticsearchOperations elasticsearchOperations;

	public Page<VehicleDocument> searchByCriteria(String plate, String brand, String model, String color,
			Pageable pageable) {
		Criteria criteria = new Criteria();

		if (brand != null && !brand.trim().isEmpty()) {
			criteria = criteria.and("brand").contains(brand.toLowerCase());
		}

		if (model != null && !model.trim().isEmpty()) {
			criteria = criteria.and("model").contains(model.toLowerCase());
		}

		if (color != null && !color.trim().isEmpty()) {
			criteria = criteria.and("color").contains(color.toLowerCase());
		}

		if (plate != null && !plate.trim().isEmpty()) {
			criteria = criteria.and("plate").contains(plate.toLowerCase());
		}

		CriteriaQuery query = new CriteriaQuery(criteria);
		query.setPageable(pageable);

		SearchHits<VehicleDocument> searchHits = elasticsearchOperations.search(query, VehicleDocument.class);

		List<VehicleDocument> content = searchHits.map(SearchHit::getContent).toList();

		return new PageImpl<>(content, pageable, searchHits.getTotalHits());

	}

}
