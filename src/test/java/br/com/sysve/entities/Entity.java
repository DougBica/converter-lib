package br.com.sysve.entities;

import br.com.sysve.converter.annotations.ConverterReferenceDto;
import br.com.sysve.converter.annotations.ConverterReferenceField;
import br.com.sysve.dtos.Dto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@ConverterReferenceDto(Dto.class)
public class Entity extends SuperClassEntity{
    @ConverterReferenceField("nomeProduto")
    private String nome;
    private BigDecimal quantidadeEmbalagem;
    private BigDecimal valorVenda;
    private Long codigoBarra;
    private String caminhoImagem;
}
