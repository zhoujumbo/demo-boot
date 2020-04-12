package com.fortunetree.demo.core.common.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fortunetree.basic.support.commons.business.jackson.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName BusinessUtil
 * @Description TODO
 * @Author jb.zhou
 * @Date 2020/1/7
 * @Version 1.0
 */
@Slf4j
public class BusinessUtil {

    public static <D,T> List<T> listCopy2List(Stream<D> list, Class<T> clazz){
        List<T> tList = list
                .filter(Objects::nonNull)
                .map(var -> {
                    T entity = null;
                    try {
                        entity = clazz.newInstance();
                        BeanUtils.copyProperties(var, entity);
                    } catch (InstantiationException e) {
                        log.error("参数转换异常：",e);
                    } catch (IllegalAccessException e) {
                        log.error("参数转换异常：",e);
                    }
                    return entity;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return Optional.ofNullable(tList).orElse(Collections.emptyList());
    }

    public static <D,T> List<T> listCopy2List(List<D> list, Class<T> clazz) throws IOException {
        String listString = JacksonUtil.toJson(list);
        JavaType javaTypeList = JacksonUtil.getCollectionType(List.class, clazz);
        List<T> tList = JacksonUtil.toBeanList(listString, javaTypeList);
        return Optional.ofNullable(tList).orElse(Collections.emptyList());
    }

    public static <D,T> T copyProperties(D d, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T t = null;
        t = clazz.newInstance();
        BeanUtils.copyProperties(d, t);
        return t;
    }

    /**
     * 打乱集合
     * @param list
     * @param <D>
     * @return
     */
    public static <D> List<D> shuffle(List<D> list) {
        D[] arrays = (D[]) list.toArray();
        arrays = shuffle(arrays);
        return Stream.of(arrays).collect(Collectors.toList());
    }

    public static <D> D[] shuffle(D[] array) {
        Random random = new Random();
        int length = array.length;
        for (int i = length; i > 1; i--) {
            // 把随机位置交换到当前位置，既然是随机，就应该保证位置可能不变的情况，因此 random.nextInt 取 i
            BusinessUtil.swap(array, i-1, random.nextInt(i));
        }
        return array;
    }

    /**
     * 数组元素互换位置
     * @param array
     * @param i
     * @param j
     */
    public static <D> void swap(D[] array, int i, int j) {
        if (i != j) {
            D temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}
