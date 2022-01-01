package br.com.sysve.converter;


import br.com.sysve.dtos.Dto;
import br.com.sysve.dtos.DtoChild;
import br.com.sysve.dtos.DtoChildWithNoAnnotation;
import br.com.sysve.dtos.DtoChildWithNoSameName;
import br.com.sysve.entities.Entity;
import br.com.sysve.entities.EntityChild;
import br.com.sysve.entities.EntityChildWithNoAnnotation;
import br.com.sysve.entities.EntityChildWithNoSameName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(dto.getNomeProduto(),entity.getNome());
        assertEquals(dto.getCaminhoImagem(), entity.getCaminhoImagem());
        assertEquals(dto.getQuantidadeEmbalagem(),entity.getQuantidadeEmbalagem());
        assertEquals(dto.getValorVenda(),entity.getValorVenda());
        assertEquals(dto.getCodigoBarra(),entity.getCodigoBarra());
    }

    @Test
    void entityToDtoWithSuper() throws Exception {
        IConverter<Entity, Dto> dtoIConverter = new Converter<>();
        Entity entity = new Entity();
        Dto dto;
        entity.setId(1L);
        entity.setAtivo(true);
        entity.setDataEdicao(LocalDateTime.of(2021,12,30,12,30,10));
        entity.setDataCadastro(LocalDateTime.of(2021,12,30,12,30,10));
        entity.setUsuarioEdicao(12L);
        entity.setUuid(UUID.randomUUID());
        entity.setVersao(10);
        dto = dtoIConverter.entityToDto(entity,1);
        assertEquals(dto.getId(),entity.getId());
        assertEquals(dto.getDataEdicao(),entity.getDataEdicao());
        assertEquals(dto.getDataCadastro(),entity.getDataCadastro());
        assertEquals(dto.getUsuarioEdicao(),entity.getUsuarioEdicao());
        assertEquals(dto.getUuid(),entity.getUuid());
        assertEquals(dto.getVersao(),entity.getVersao());
    }

    @Test
    void entityToDtoWithChildList() throws Exception {
        IConverter<Entity, Dto> dtoIConverter = new Converter<>();
        EntityChild entityChild = new EntityChild();
        List<EntityChild> childList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        nameList.add("test");
        entityChild.setName("ChildName");
        childList.add(entityChild);
        Entity entity = new Entity();
        Dto dto;
        entity.setChildList(childList);
        entity.setNameList(nameList);
        dto = dtoIConverter.entityToDto(entity,1);
        assertEquals(dto.getChildList().get(0).getName(),entity.getChildList().get(0).getName());
        assertEquals(dto.getNameList().get(0),entity.getNameList().get(0));
    }

    @Test
    void entityToDtoWithChildListWithNoAnnotation() throws Exception {
        IConverter<Entity, Dto> dtoIConverter = new Converter<>();
        EntityChildWithNoAnnotation entityChildWithNoAnnotation = new EntityChildWithNoAnnotation();
        List<EntityChildWithNoAnnotation> childList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        nameList.add("test");
        entityChildWithNoAnnotation.setName("ChildName");
        childList.add(entityChildWithNoAnnotation);
        Entity entity = new Entity();
        Dto dto;
        entity.setChildWithNoAnnotation(childList);
        entity.setNameList(nameList);
        dto = dtoIConverter.entityToDto(entity,1);
        Assertions.assertNull(dto.getChildWithNoAnnotation());
        assertEquals(dto.getNameList().get(0),entity.getNameList().get(0));
    }

    @Test
    void entityToDtoWithChildListWithNoSameFieldName() throws Exception {
        IConverter<Entity, Dto> dtoIConverter = new Converter<>();
        EntityChildWithNoSameName entityChildWithNoSameName = new EntityChildWithNoSameName();
        List<EntityChildWithNoSameName> childList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        nameList.add("test");
        entityChildWithNoSameName.setName("ChildName");
        childList.add(entityChildWithNoSameName);
        Entity entity = new Entity();
        Dto dto;
        entity.setEntityChildWithNoSameName(childList);
        entity.setNameList(nameList);
        dto = dtoIConverter.entityToDto(entity,1);
        assertEquals("ChildName" ,dto.getDtoChildWithNoSameName().get(0).getName());
        assertEquals("ChildName" ,entity.getEntityChildWithNoSameName().get(0).getName());
        assertEquals(dto.getNameList().get(0),entity.getNameList().get(0));
    }

    @Test
    void entityToDtoWithAll() throws Exception {
        IConverter<Entity, Dto> dtoIConverter = new Converter<>();
        Entity entity = new Entity();
        Dto dto;
        entity.setNome("Test");
        entity.setCaminhoImagem("http://teste.com");
        entity.setCodigoBarra(123456787L);
        entity.setQuantidadeEmbalagem(new BigDecimal("1512"));
        entity.setValorVenda(new BigDecimal("12"));
        entity.setId(1L);
        entity.setAtivo(true);
        entity.setDataEdicao(LocalDateTime.of(2021,12,30,12,30,10));
        entity.setDataCadastro(LocalDateTime.of(2021,12,30,12,30,10));
        entity.setUsuarioEdicao(12L);
        entity.setUuid(UUID.randomUUID());
        entity.setVersao(10);

        EntityChild entityChild = new EntityChild();
        List<EntityChild> childList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        nameList.add("test");
        entityChild.setName("ChildName");
        childList.add(entityChild);
        entity.setChildList(childList);
        entity.setNameList(nameList);

        EntityChildWithNoAnnotation entityChildWithNoAnnotation = new EntityChildWithNoAnnotation();
        List<EntityChildWithNoAnnotation> childList2 = new ArrayList<>();
        List<String> nameList2 = new ArrayList<>();
        nameList2.add("test");
        entityChildWithNoAnnotation.setName("ChildName");
        childList2.add(entityChildWithNoAnnotation);
        entity.setChildWithNoAnnotation(childList2);
        entity.setNameList(nameList2);

        EntityChildWithNoSameName entityChildWithNoSameName = new EntityChildWithNoSameName();
        List<EntityChildWithNoSameName> childList3 = new ArrayList<>();
        List<String> nameList3 = new ArrayList<>();
        nameList3.add("test");
        entityChildWithNoSameName.setName("ChildName");
        childList3.add(entityChildWithNoSameName);
        entity.setEntityChildWithNoSameName(childList3);
        entity.setNameList(nameList3);

        dto = dtoIConverter.entityToDto(entity,1);

        assertEquals(dto.getNomeProduto(),entity.getNome());
        assertEquals(dto.getCaminhoImagem(), entity.getCaminhoImagem());
        assertEquals(dto.getQuantidadeEmbalagem(),entity.getQuantidadeEmbalagem());
        assertEquals(dto.getValorVenda(),entity.getValorVenda());
        assertEquals(dto.getCodigoBarra(),entity.getCodigoBarra());

        assertEquals(dto.getId(),entity.getId());
        assertEquals(dto.getDataEdicao(),entity.getDataEdicao());
        assertEquals(dto.getDataCadastro(),entity.getDataCadastro());
        assertEquals(dto.getUsuarioEdicao(),entity.getUsuarioEdicao());
        assertEquals(dto.getUuid(),entity.getUuid());
        assertEquals(dto.getVersao(),entity.getVersao());

        assertEquals(dto.getChildList().get(0).getName(),entity.getChildList().get(0).getName());
        assertEquals(dto.getNameList().get(0),entity.getNameList().get(0));

        Assertions.assertNull(dto.getChildWithNoAnnotation());
        assertEquals(dto.getNameList().get(0),entity.getNameList().get(0));

        assertEquals("ChildName" ,dto.getDtoChildWithNoSameName().get(0).getName());
        assertEquals("ChildName" ,entity.getEntityChildWithNoSameName().get(0).getName());
        assertEquals(dto.getNameList().get(0),entity.getNameList().get(0));
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
        assertEquals(dto.getNomeProduto(),entity.getNome());
        assertEquals(dto.getCaminhoImagem(),entity.getCaminhoImagem());
        assertEquals(dto.getQuantidadeEmbalagem(),entity.getQuantidadeEmbalagem());
        assertEquals(dto.getValorVenda(),entity.getValorVenda());
        assertEquals(dto.getCodigoBarra(),entity.getCodigoBarra());
    }

    @Test
    void dtoToEntityWithSuper() throws Exception {
        IConverter<Dto, Entity> dtoIConverter = new Converter<>();
        Entity entity;
        Dto dto = new Dto();
        dto.setId(1L);
        dto.setAtivo(true);
        dto.setDataEdicao(LocalDateTime.of(2021,12,30,12,30,10));
        dto.setDataCadastro(LocalDateTime.of(2021,12,30,12,30,10));
        dto.setUsuarioEdicao(12L);
        dto.setUuid(UUID.randomUUID());
        dto.setVersao(10);
        entity = dtoIConverter.dtoToEntity(dto,0);
        assertEquals(dto.getId(),entity.getId());
        assertEquals(dto.getDataEdicao(),entity.getDataEdicao());
        assertEquals(dto.getDataCadastro(),entity.getDataCadastro());
        assertEquals(dto.getUsuarioEdicao(),entity.getUsuarioEdicao());
        assertEquals(dto.getUuid(),entity.getUuid());
        assertEquals(dto.getVersao(),entity.getVersao());
    }

    @Test
    void dtoToEntityWithChildList() throws Exception {
        IConverter<Dto, Entity> entityIConverter = new Converter<>();
        DtoChild dtoChild = new DtoChild();
        List<DtoChild> childList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        nameList.add("test");
        dtoChild.setName("ChildNameDto");
        childList.add(dtoChild);
        Dto dto = new Dto();
        dto.setChildList(childList);
        dto.setNameList(nameList);
        Entity entity = entityIConverter.dtoToEntity(dto,1);
        assertEquals(entity.getChildList().get(0).getName(),dto.getChildList().get(0).getName());
        assertEquals(entity.getNameList().get(0),dto.getNameList().get(0));
    }

    @Test
    void dtoToEntityWithChildListWithNoAnnotation() throws Exception {
        IConverter<Dto, Entity> entityConverter = new Converter<>();
        DtoChildWithNoAnnotation dtoChildWithNoAnnotation = new DtoChildWithNoAnnotation();
        List<DtoChildWithNoAnnotation> childList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        nameList.add("test");
        dtoChildWithNoAnnotation.setName("ChildNameDto");
        childList.add(dtoChildWithNoAnnotation);
        Entity entity;
        Dto dto = new Dto();
        dto.setChildWithNoAnnotation(childList);
        dto.setNameList(nameList);
        entity = entityConverter.dtoToEntity(dto,1);
        Assertions.assertNull(entity.getChildWithNoAnnotation());
        assertEquals(entity.getNameList().get(0),dto.getNameList().get(0));
    }

    @Test
    void dtoToEntityWithChildListWithNoSameFieldName() throws Exception {
        IConverter<Dto, Entity> dtoIConverter = new Converter<>();
        DtoChildWithNoSameName dtoChildWithNoSameName = new DtoChildWithNoSameName();
        List<DtoChildWithNoSameName> childList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        nameList.add("test");
        dtoChildWithNoSameName.setName("ChildName");
        childList.add(dtoChildWithNoSameName);
        Dto dto = new Dto();
        Entity entity;
        dto.setDtoChildWithNoSameName(childList);
        dto.setNameList(nameList);
        entity = dtoIConverter.dtoToEntity(dto,1);
        assertEquals("ChildName" ,dto.getDtoChildWithNoSameName().get(0).getName());
        assertEquals("ChildName" ,entity.getEntityChildWithNoSameName().get(0).getName());
        assertEquals(dto.getNameList().get(0),entity.getNameList().get(0));
    }

    @Test
    void dtoToEntityWithAll() throws Exception {
        IConverter<Dto, Entity> dtoIConverter = new Converter<>();
        Entity entity;
        Dto dto = new Dto();
        dto.setNomeProduto("Test");
        dto.setCaminhoImagem("http://teste.com");
        dto.setCodigoBarra(123456787L);
        dto.setQuantidadeEmbalagem(new BigDecimal("1512"));
        dto.setValorVenda(new BigDecimal("12"));

        dto.setId(1L);
        dto.setAtivo(true);
        dto.setDataEdicao(LocalDateTime.of(2021,12,30,12,30,10));
        dto.setDataCadastro(LocalDateTime.of(2021,12,30,12,30,10));
        dto.setUsuarioEdicao(12L);
        dto.setUuid(UUID.randomUUID());
        dto.setVersao(10);

        DtoChild dtoChild = new DtoChild();
        List<DtoChild> childList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        nameList.add("test");
        dtoChild.setName("ChildNameDto");
        childList.add(dtoChild);
        dto.setChildList(childList);
        dto.setNameList(nameList);

        DtoChildWithNoAnnotation dtoChildWithNoAnnotation = new DtoChildWithNoAnnotation();
        List<DtoChildWithNoAnnotation> childList2 = new ArrayList<>();
        List<String> nameList2 = new ArrayList<>();
        nameList2.add("test");
        dtoChildWithNoAnnotation.setName("ChildNameDto");
        childList2.add(dtoChildWithNoAnnotation);
        dto.setChildWithNoAnnotation(childList2);
        dto.setNameList(nameList2);

        DtoChildWithNoSameName dtoChildWithNoSameName = new DtoChildWithNoSameName();
        List<DtoChildWithNoSameName> childList3 = new ArrayList<>();
        List<String> nameList3 = new ArrayList<>();
        nameList3.add("test");
        dtoChildWithNoSameName.setName("ChildName");
        childList3.add(dtoChildWithNoSameName);
        dto.setDtoChildWithNoSameName(childList3);
        dto.setNameList(nameList3);

        entity = dtoIConverter.dtoToEntity(dto,1);

        assertEquals(dto.getNomeProduto(),entity.getNome());
        assertEquals(dto.getCaminhoImagem(),entity.getCaminhoImagem());
        assertEquals(dto.getQuantidadeEmbalagem(),entity.getQuantidadeEmbalagem());
        assertEquals(dto.getValorVenda(),entity.getValorVenda());
        assertEquals(dto.getCodigoBarra(),entity.getCodigoBarra());

        assertEquals(dto.getId(),entity.getId());
        assertEquals(dto.getDataEdicao(),entity.getDataEdicao());
        assertEquals(dto.getDataCadastro(),entity.getDataCadastro());
        assertEquals(dto.getUsuarioEdicao(),entity.getUsuarioEdicao());
        assertEquals(dto.getUuid(),entity.getUuid());
        assertEquals(dto.getVersao(),entity.getVersao());

        assertEquals(entity.getChildList().get(0).getName(),dto.getChildList().get(0).getName());
        assertEquals(entity.getNameList().get(0),dto.getNameList().get(0));

        Assertions.assertNull(entity.getChildWithNoAnnotation());
        assertEquals(entity.getNameList().get(0),dto.getNameList().get(0));

        assertEquals("ChildName" ,dto.getDtoChildWithNoSameName().get(0).getName());
        assertEquals("ChildName" ,entity.getEntityChildWithNoSameName().get(0).getName());
        assertEquals(dto.getNameList().get(0),entity.getNameList().get(0));
    }
}