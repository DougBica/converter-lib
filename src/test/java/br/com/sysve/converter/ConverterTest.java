package br.com.sysve.converter;


import br.com.sysve.dtos.Dto;
import br.com.sysve.entities.Entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ConverterTest {

    @Test
    void entityToDto() throws Exception {
        IConverter<Entity, Dto> dtoIConverter = new Converter<>();
        Entity entity = new Entity();
        Dto dto;
        entity.setNome("Test");
        entity.setCaminhoImagem("http://teste.com");
        entity.setCodigoBarra(123456787L);
        entity.setQuantidadeEmbalagem(new BigDecimal("1512"));
        entity.setValorVenda(new BigDecimal("12"));
        dto = dtoIConverter.entityToDto(entity,0);
        Assertions.assertEquals(dto.getNomeProduto(),entity.getNome());
        Assertions.assertEquals(dto.getCaminhoImagem(),entity.getCaminhoImagem());
        Assertions.assertEquals(dto.getQuantidadeEmbalagem(),entity.getQuantidadeEmbalagem());
        Assertions.assertEquals(dto.getValorVenda(),entity.getValorVenda());
        Assertions.assertEquals(dto.getCodigoBarra(),entity.getCodigoBarra());
    }

    @Test
    void dtoToEntity() throws Exception {
        IConverter<Dto, Entity> dtoIConverter = new Converter<>();
        Entity entity;
        Dto dto = new Dto();
        dto.setNomeProduto("Test");
        dto.setCaminhoImagem("http://teste.com");
        dto.setCodigoBarra(123456787L);
        dto.setQuantidadeEmbalagem(new BigDecimal("1512"));
        dto.setValorVenda(new BigDecimal("12"));
        entity = dtoIConverter.dtoToEntity(dto,0);
        Assertions.assertEquals(dto.getNomeProduto(),entity.getNome());
        Assertions.assertEquals(dto.getCaminhoImagem(),entity.getCaminhoImagem());
        Assertions.assertEquals(dto.getQuantidadeEmbalagem(),entity.getQuantidadeEmbalagem());
        Assertions.assertEquals(dto.getValorVenda(),entity.getValorVenda());
        Assertions.assertEquals(dto.getCodigoBarra(),entity.getCodigoBarra());
    }
}