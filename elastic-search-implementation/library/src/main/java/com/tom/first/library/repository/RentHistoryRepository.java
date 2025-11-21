package com.tom.first.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.first.library.model.RentHistory;

@Repository
public interface RentHistoryRepository extends JpaRepository<RentHistory, Long> {

}
