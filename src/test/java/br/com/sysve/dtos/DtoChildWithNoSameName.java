package br.com.sysve.dtos;

import br.com.sysve.converter.annotations.ConverterReferenceEntity;
import br.com.sysve.entities.EntityChildWithNoSameName;
import lombok.Data;

@Data
@ConverterReferenceEntity(EntityChildWithNoSameName.class)
public class DtoChildWithNoSameName {
    private String name;
}

