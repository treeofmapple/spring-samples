package com.tom.first.library.model;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.tom.first.library.model.enums.EventType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_outbox")
public class BookOutbox {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", 
    		nullable = false, 
    		length = 20)
    private EventType eventType;

    @Column(name = "payload", 
    		nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String payload;

    @CreationTimestamp
    @Column(name = "created_at", 
    		updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "processed", 
    		nullable = false)
    private boolean processed = false;
	
}
