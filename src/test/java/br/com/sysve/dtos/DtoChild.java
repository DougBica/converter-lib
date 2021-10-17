package br.com.sysve.dtos;

import br.com.sysve.converter.annotations.ConverterReferenceEntity;
import br.com.sysve.entities.EntityChild;
import lombok.Data;

@Data
@ConverterReferenceEntity(EntityChild.class)
public class DtoChild {
    private String name;
}

