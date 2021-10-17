package br.com.sysve.dtos;

import br.com.sysve.converter.annotations.ConverterReferenceEntity;
import br.com.sysve.converter.annotations.ConverterReferenceField;
import br.com.sysve.entities.Entity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    private UUID uuid;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataEdicao;
    private Long usuarioEdicao;
    private Integer versao;
    private List<DtoChild> childList;
    private List<DtoChildWithNoAnnotation> childWithNoAnnotation;
    @ConverterReferenceField("entityChildWithNoSameName")
    private List<DtoChildWithNoSameName> dtoChildWithNoSameName;
    private List<String> nameList;

}
