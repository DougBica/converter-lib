package br.com.sysve.entities;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SuperClassEntity {
    private Long id;
    private UUID uuid;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataEdicao;
    private Long usuarioEdicao;
    private Integer versao;
    private Boolean ativo;
}
