package com.tom.management.request.dto;

import java.util.Set;

import com.tom.management.model.Equipamento;

public record DisplayEquipamentoDTO(Long Id, String Nome, String Tipo, Set<Equipamento> Equipamentos) {

}
