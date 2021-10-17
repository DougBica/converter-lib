package br.com.sysve.converter;

import br.com.sysve.converter.annotations.ConverterReferenceDto;
import br.com.sysve.converter.annotations.ConverterReferenceEntity;
import br.com.sysve.converter.annotations.ConverterReferenceField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converter<T,E> implements IConverter<T,E> {
    private E destinyClass;

    @Override
    public E entityToDto(T entity, Integer deepEntity) throws Exception {
        fillDestinyClass(entity, ConverterReferenceDto.class);
        return initConverter(entity, deepEntity);
    }

    @Override
    public E dtoToEntity(T dto, Integer deepEntity) throws Exception {
        fillDestinyClass(dto, ConverterReferenceEntity.class);
        return initConverter(dto, deepEntity);
    }

    @SuppressWarnings("unchecked")
    private E initConverter(T source, Integer deepEntity) throws Exception {
        sourceToDestiny(source,destinyClass, getFields((Class<T>) source.getClass()));
        if(deepEntity != null){
            for (int i = 0; i < deepEntity; i++){
                sourceToDestiny(source, destinyClass, getFields((Class<T>) source.getClass().getSuperclass()));
            }
        }
        return destinyClass;
    }

    @SuppressWarnings("unchecked")
    private void fillDestinyClass(T entity, Class<? extends Annotation> anote) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Annotation annotation = entity.getClass().getAnnotation(anote);
        Method method = annotation.getClass().getMethod("value");
        destinyClass = (E) method.invoke(annotation,(Object[]) null);
        destinyClass = ((Class<E>) destinyClass).getConstructor().newInstance();
    }

    private void sourceToDestiny(T source, E destiny, List<Field> sourceFieldList) throws Exception {
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
            } catch (Exception e) {
                throw new Exception("Error to transfer field: " + sf.getName() +" to destiny:"+ destiny.getClass().getName() + ".");
            }
        }
    }

    private String getFieldNameFromAnnotation(Field sf){
        try {
            Annotation annotation = sf.getAnnotation(ConverterReferenceField.class);
            Method method = annotation.getClass().getMethod("value");
            return (String) method.invoke(annotation,(Object []) null);
        } catch (Exception e){
            System.out.println("Error on retrieve value from ConverterReferenceField annotation in :" + sf.getName());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void setValueListToDestiny(String name, Field sf, T source, E destiny) throws Exception {
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
                    System.out.println("WARN annotation not set in child class: "+ sf.getGenericType());
                    return;
                }
                setValueToDestinyChild(name,listChild, destiny);
            }
        } catch (Exception e) {
            throw new Exception("Error to transfer field: " + sf.getName() +" to destiny:"+ destiny.getClass().getName() + ".");
        }
    }


    private void setValueToDestiny(String sourceFieldName, Field sourceField, T source, E destiny) throws InvocationTargetException, IllegalAccessException {
        if (sourceFieldName != null){
            try {
                Method mSet = destiny.getClass().getMethod("set" + firstCharUpperCase(sourceFieldName), sourceField.getType());
                Method mGet = source.getClass().getMethod("get" + firstCharUpperCase(sourceField.getName()));
                mSet.invoke(destiny, mGet.invoke(source));
            } catch (NoSuchMethodException e){
                System.out.println("WARN method not found in destiny: "+e.getMessage());
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
                System.out.println("WARN method not found in destiny: "+e.getMessage());
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
