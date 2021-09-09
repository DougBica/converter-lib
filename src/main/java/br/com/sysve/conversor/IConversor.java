package br.com.sysve.conversor;

import java.util.Map;

public interface IConversor<T> {
    public T entityToDto(Map<String, String> fieldMap, T entity, T dto, Integer deepEntity) throws Exception;
    public T dtoToEntity(Map<String, String> fieldMap, T dto, T entity, Integer deepEntity) throws Exception;
}
