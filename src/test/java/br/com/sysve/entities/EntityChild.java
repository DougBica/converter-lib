package br.com.sysve.entities;

import br.com.sysve.converter.annotations.ConverterReferenceDto;
import br.com.sysve.dtos.DtoChild;
import lombok.Data;

@Data
@ConverterReferenceDto(DtoChild.class)
public class EntityChild {
    private String name;
}
