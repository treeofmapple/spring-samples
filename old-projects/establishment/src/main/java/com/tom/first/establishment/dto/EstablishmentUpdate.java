package com.tom.first.establishment.dto;

public record EstablishmentUpdate(
        String name, 
        String cnpj, 
        String address, 
        String phone,
        int motorcycleSpotCount, 
        int carSpotCount
) {
}
