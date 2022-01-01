package br.com.sysve.converter;

import br.com.sysve.converter.exceptions.AnnotationException;
import br.com.sysve.converter.exceptions.TransferFieldDataException;
import br.com.sysve.converter.annotations.ConverterReferenceDto;
import br.com.sysve.converter.annotations.ConverterReferenceEntity;
import br.com.sysve.converter.annotations.ConverterReferenceField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Converter<T,E> implements IConverter<T,E> {
    private static final Logger LOGGER = LogManager.getLogger(Converter.class);

    private E destinyClass;

    @Override
    public E entityToDto(T entity, Integer deepEntity) throws TransferFieldDataException, AnnotationException {
        fillDestinyClass(entity, ConverterReferenceDto.class);
        return initConverter(entity, deepEntity);
    }

    @Override
    public E dtoToEntity(T dto, Integer deepEntity) throws TransferFieldDataException, AnnotationException {
        fillDestinyClass(dto, ConverterReferenceEntity.class);
        return initConverter(dto, deepEntity);
    }


    private E initConverter(T source, Integer deepEntity) throws TransferFieldDataException {
        sourceToDestiny(source,destinyClass, Arrays.asList(source.getClass().getDeclaredFields()));
        if(deepEntity != null){
            Class<?> ob = source.getClass();
            for (int i = 0; i < deepEntity; i++){
                if (ob != null && i!=0)
                    ob = ob.getSuperclass();
                if (ob != null && ob.getSuperclass() != null)
                    sourceToDestiny(source, destinyClass, Arrays.asList(ob.getSuperclass().getDeclaredFields()));
            }
        }
        return destinyClass;
    }

    @SuppressWarnings("unchecked")
    private void fillDestinyClass(T entity, Class<? extends Annotation> anote) throws AnnotationException {
        try {
            Annotation annotation = entity.getClass().getAnnotation(anote);
            Method method = annotation.getClass().getMethod("value");
            destinyClass = (E) method.invoke(annotation);
            destinyClass = ((Class<E>)destinyClass).getConstructor().newInstance();
        } catch (ReflectiveOperationException e ){
            e.printStackTrace();
            throw new AnnotationException("Error filling class by annotation. Check ConverterReferenceDto or ConverterReferenceEntity annotations.");
        }
    }

    private void sourceToDestiny(T source, E destiny, List<Field> sourceFieldList) throws TransferFieldDataException {
        for (Field sf : sourceFieldList){
            try {
                if (sf.getType().getName().contains("List") && sf.isAnnotationPresent(ConverterReferenceField.class)) {
                    setValueListToDestiny(getFieldNameFromAnnotation(sf), sf, source, destiny);
                } else if (sf.getType().getName().contains("List")){
                    setValueListToDestiny(sf.getName(), sf, source, destiny);
                } else if (sf.isAnnotationPresent(ConverterReferenceField.class)){
                    setValueToDestiny(getFieldNameFromAnnotation(sf), sf, source, destiny);
                }  else {
                    setValueToDestiny(sf.getName(), sf, source, destiny);
                }
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                throw new TransferFieldDataException("Error to transfer field: " + sf.getName() +" to destiny:"+ destiny.getClass().getName() + ".");
            }
        }
    }

    private String getFieldNameFromAnnotation(Field sf){
        try {
            Annotation annotation = sf.getAnnotation(ConverterReferenceField.class);
            Method method = annotation.getClass().getMethod("value");
            return (String) method.invoke(annotation,(Object []) null);
        } catch (Exception e){
            LOGGER.error("Error on retrieve value from ConverterReferenceField annotation in : {}", sf.getName());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void setValueListToDestiny(String name, Field sf, T source, E destiny) throws TransferFieldDataException {
        try {
            ArrayList<?> list = (ArrayList<?>) source.getClass().getMethod("get"+ firstCharUpperCase(sf.getName())).invoke(source);
            if (list != null && !list.isEmpty()){
                ArrayList<E> listChild = new ArrayList<>();
                IConverter<T,E> conv = new Converter<>();
                if (list.get(0).getClass().isAnnotationPresent(ConverterReferenceDto.class)){
                    for (Object t: list){
                        listChild.add(conv.entityToDto((T)t,0));
                    }
                } else if (list.get(0).getClass().isAnnotationPresent(ConverterReferenceEntity.class)){
                    for (Object t: list){
                        listChild.add(conv.dtoToEntity((T)t,0));
                    }
                } else if (sf.getGenericType().equals(destiny.getClass().getDeclaredField(name).getGenericType())) {
                    setValueToDestiny(name, sf, source, destiny);
                    return;
                } else {
                    LOGGER.warn("Annotation not set in child class: {}", sf.getGenericType());
                    return;
                }
                setValueToDestinyChild(name,listChild, destiny);
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            throw new TransferFieldDataException("Error to transfer field: " + sf.getName() +" to destiny:"+ destiny.getClass().getName() + ".");
        }
    }


    private void setValueToDestiny(String sourceFieldName, Field sourceField, T source, E destiny) throws InvocationTargetException, IllegalAccessException {
        if (sourceFieldName != null){
            try {
                Method mSet = destiny.getClass().getMethod("set" + firstCharUpperCase(sourceFieldName), sourceField.getType());
                Method mGet = source.getClass().getMethod("get" + firstCharUpperCase(sourceField.getName()));
                mSet.invoke(destiny, mGet.invoke(source));
            } catch (NoSuchMethodException e){
                LOGGER.warn("Method not found in destiny: {}",e.getMessage());
            }
        }
    }

    private void setValueToDestinyChild(String sourceFieldName, ArrayList<E> listChild, E destiny) throws InvocationTargetException, IllegalAccessException {
        if (sourceFieldName != null){
            try {
                Method mSet = destiny.getClass().getMethod("set" + firstCharUpperCase(sourceFieldName),
                        destiny.getClass().getDeclaredField(sourceFieldName).getType());
                mSet.invoke(destiny, listChild);
            } catch (NoSuchMethodException | NoSuchFieldException e){
                LOGGER.warn("Method not found in destiny: {}", e.getMessage());
            }
        }
    }

    private String firstCharUpperCase(String str){
        return  Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
