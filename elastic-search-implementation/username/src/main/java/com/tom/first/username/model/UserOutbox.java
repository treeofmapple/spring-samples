package com.tom.first.username.model;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.tom.first.username.model.enums.EventType;

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
@Table(name = "username_outbox")
public class UserOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", 
    		nullable = false, 
    		length = 20)
    private EventType eventType;

    @Column(name = "payload", 
    		nullable = false, 
    		columnDefinition = "jsonb")
    private String payload;

    @CreationTimestamp
    @Column(name = "created_at", 
    		updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "processed", 
    		nullable = false)
    private boolean processed = false;
	
}
