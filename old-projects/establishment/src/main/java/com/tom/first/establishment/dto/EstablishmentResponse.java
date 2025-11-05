package com.tom.first.establishment.dto;

public record EstablishmentResponse(
        String name,
        String cnpj,
        String address,
        String phone,
        int motorcycleSpotCount,
        int carSpotCount
) {
}