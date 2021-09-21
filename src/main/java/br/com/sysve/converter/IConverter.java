package br.com.sysve.converter;

public interface IConverter<T> {

    T entityToDto(T entity, Integer deepEntity) throws Exception;
    T dtoToEntity(T dto, Integer deepEntity) throws Exception;
}
