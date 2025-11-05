package com.tom.management.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Solicitacoes")
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_espaco", nullable = false)
    private EspacoFisico espaco;

    @ManyToOne
    @JoinColumn(name = "id_solicitante", nullable = false)
    private Usuario solicitante;
    
    @ManyToOne
    @JoinColumn(name = "id_avaliador", nullable = true)
    private Avaliador avaliador;

    @Column(nullable = false)
    private String nome;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;
    
    @Column(name = "data_solicitacao", nullable = false)
    private LocalDate dataSolicitacao;
    
    @Column(name = "data_avaliacao", nullable = false)
    private LocalDate dataStatus;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String justificativa;
    
    @PrePersist
    public void prePersist() {
        if (dataSolicitacao == null) {
            this.dataSolicitacao = LocalDate.now();
        }
    }
}
