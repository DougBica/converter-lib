package br.com.sysve.conversor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Conversor<T> implements IConversor<T>{
    private List<Field> sourceFieldList;
    private List<Field> destinyFieldList;
    private Field f1;
    private Field f2;
    private T superClass;


    @Override
    public T entityToDto(Map<String, String> fieldMap, T entity, T dto, Integer deepEntity) throws Exception {
        sourceFieldList = setFields(entity.getClass());
        destinyFieldList = setFields(dto.getClass());
        if (fieldMap != null && !fieldMap.isEmpty()){
            sourceToDestiny(fieldMap, entity, dto);
        }
        sourceToDestiny(entity, dto);
        if(deepEntity != null){
            for (int i = 0; i < deepEntity; i++){
                if (superClass == null){
                    superClass = (T) entity.getClass().getSuperclass();
                    sourceFieldList = setFields((Class<T>) superClass);
                    sourceToDestiny(entity, dto);
                } else {
                    sourceToDestiny(entity, dto);
                }
            }
        }
        return dto;
    }

    @Override
    public T dtoToEntity(Map<String, String> fieldMap, T dto, T entity, Integer deepEntity) throws Exception {
        sourceFieldList = setFields(dto.getClass());
        destinyFieldList = setFields(entity.getClass());
        if (fieldMap != null && !fieldMap.isEmpty()){
            sourceToDestiny(fieldMap, dto, entity);
        }
        sourceToDestiny(dto,entity);
        return entity;
    }


    private void sourceToDestiny(Map<String, String> fieldMap, T source, T destiny) throws Exception {
        fillFields(fieldMap, source, destiny);
        fillFields(source,destiny);
    }

    private void sourceToDestiny(T source, T destiny) throws Exception {
        fillFields(source,destiny);
    }

    private void fillFields(Map<String, String> fieldMap, T source, T destiny) throws Exception {
        try {
            for(Map.Entry<String, String> entry : fieldMap.entrySet()){
                f1 = sourceFieldList.stream().filter(f -> f.getName().equals(entry.getKey())).findFirst().orElse(null);
                if (f1 != null){
                    setValueToDestiny(f1, source, destiny);
                }

            }
        } catch (Exception e) {
            throw new Exception("Erro ao transferir campos da fonte ao destino com mapeamento.");
        }
    }

    private void fillFields(T source, T destiny) throws Exception {
        try {
            for (Field sf : sourceFieldList){
                setValueToDestiny(sf, source, destiny);
            }
        } catch (Exception e ){
            throw new Exception("Erro ao transferir campos da fonte ao destino.");
        }
    }

    private void setValueToDestiny(Field sourceField, T source, T destiny) throws InvocationTargetException, IllegalAccessException {
        f2 = destinyFieldList.stream().filter(d -> d.getName().equals(sourceField.getName())).findFirst().orElse(null);
            if(f2 != null){
                Method mSet = Arrays.stream(destiny.getClass().getMethods()).filter(ms -> ms.getName().toLowerCase().equals(("set"+ f2.getName()).toLowerCase())).findFirst().orElse(null);
            if (mSet != null){
                Method mGet = Arrays.stream(source.getClass().getMethods()).filter(msf -> msf.getName().toLowerCase().equals(("get"+ sourceField.getName()).toLowerCase())).findFirst().orElse(null);
                if (mGet != null){
                    mSet.invoke(destiny, mGet.invoke(source));
                }
            }
        }
    }

    private <T> List<Field> setFields(Class<T> cl){
        return Arrays.asList(cl.getDeclaredFields());
    };


}
