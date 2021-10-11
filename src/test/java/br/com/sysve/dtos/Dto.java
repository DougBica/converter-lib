package br.com.sysve.dtos;

import br.com.sysve.converter.annotations.ConverterReferenceEntity;
import br.com.sysve.converter.annotations.ConverterReferenceField;
import br.com.sysve.entities.Entity;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ConverterReferenceEntity(Entity.class)
public class Dto {
    private Long id;
    @ConverterReferenceField("nome")
    private String nomeProduto;
    private BigDecimal quantidadeEmbalagem;
    private BigDecimal valorVenda;
    private Long codigoBarra;
    private String caminhoImagem;
    private Boolean ativo;

}
