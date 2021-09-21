package br.com.sysve.converter;

import br.com.sysve.converter.annotations.ConverterReferenceDto;
import br.com.sysve.converter.annotations.ConverterReferenceEntity;
import br.com.sysve.converter.annotations.ConverterReferenceField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class Converter<T> implements IConverter<T> {
    private T superClass;
    private T destinyClass;

    @Override
    public T entityToDto(T entity, Integer deepEntity) throws Exception {
        fillDestinyClass(entity, ConverterReferenceDto.class);
        return initConverter(entity, deepEntity);
    }

    @Override
    public T dtoToEntity(T dto, Integer deepEntity) throws Exception {
        fillDestinyClass(dto, ConverterReferenceEntity.class);
        return initConverter(dto, deepEntity);
    }

    @SuppressWarnings("unchecked")
    private T initConverter(T source, Integer deepEntity) throws Exception {
        sourceToDestiny(source,destinyClass, getFields((Class<T>) source.getClass()));
        if(deepEntity != null){
            for (int i = 0; i < deepEntity; i++){
                superClass = (T) source.getClass().getSuperclass();
                sourceToDestiny(source, destinyClass, getFields((Class<T>) superClass));
            }
        }
        return destinyClass;
    }

    @SuppressWarnings("unchecked")
    private void fillDestinyClass(T entity, Class<? extends Annotation> anote) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Annotation annotation = entity.getClass().getAnnotation(anote);
        Method method = annotation.getClass().getMethod("value");
        destinyClass = (T) method.invoke(annotation,(Object[]) null);
        destinyClass = ((Class<T>) destinyClass).getConstructor().newInstance();
    }

    private void sourceToDestiny(T source, T destiny, List<Field> sourceFieldList) throws Exception {
        try {
            for (Field sf : sourceFieldList){
                if (sf.isAnnotationPresent(ConverterReferenceField.class)){
                    Annotation annotation = sf.getAnnotation(ConverterReferenceField.class);
                    Method method = annotation.getClass().getMethod("value");
                    String nomeCampo = (String) method.invoke(annotation,(Object []) null);
                    setValueToDestiny(nomeCampo, sf, source, destiny);
                } else {
                    setValueToDestiny(sf.getName(), sf, source, destiny);
                }
            }
        } catch (Exception e) {
            throw new Exception("Erro ao transferir campos da fonte ao destino.");
        }
    }


    private void setValueToDestiny(String sourceFieldName, Field sourceField, T source, T destiny) throws InvocationTargetException, IllegalAccessException {
        if (sourceFieldName != null){
            try {
                Method mSet = destiny.getClass().getDeclaredMethod("set" + firstCharUpperCase(sourceFieldName), sourceField.getType());
                Method mGet = source.getClass().getMethod("get" + firstCharUpperCase(sourceField.getName()));
                mSet.invoke(destiny, mGet.invoke(source));
            } catch (NoSuchMethodException e){
                System.out.println("WARNING"+e.getMessage());
            }
        }
    }

    private String firstCharUpperCase(String str){
        return  Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    private List<Field> getFields(Class<T> cl){
        return Arrays.asList(cl.getDeclaredFields());
    }
}
