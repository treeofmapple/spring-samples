package com.tom.management.request.dto;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.tom.management.model.Status;

public class StatusDeserializer extends JsonDeserializer<Status> {

    @Override
    public Status deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().toUpperCase();
        try {
            return Status.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valor de status inválido: " + value);
        }
    }
}