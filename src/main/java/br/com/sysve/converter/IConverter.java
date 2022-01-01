package br.com.sysve.converter;

import br.com.sysve.converter.exceptions.AnnotationException;
import br.com.sysve.converter.exceptions.TransferFieldDataException;

public interface IConverter<T,E> {
    
    E entityToDto(T entity, Integer deepEntity) throws TransferFieldDataException, AnnotationException;

    E dtoToEntity(T dto, Integer deepEntity) throws TransferFieldDataException, AnnotationException;
}
