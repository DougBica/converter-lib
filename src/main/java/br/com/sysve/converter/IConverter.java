package br.com.sysve.converter;

public interface IConverter<T,E> {
    
    E entityToDto(T entity, Integer deepEntity) throws Exception;

    E dtoToEntity(T dto, Integer deepEntity) throws Exception;
}
