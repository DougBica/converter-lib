package br.com.sysve.entities;

import br.com.sysve.converter.annotations.ConverterReferenceDto;
import br.com.sysve.dtos.DtoChildWithNoSameName;
import lombok.Data;

@Data
@ConverterReferenceDto(DtoChildWithNoSameName.class)
public class EntityChildWithNoSameName {
    private String name;
}
