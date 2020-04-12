package com.fortunetree.demo.core.common.exception.stream;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @ClassName StreamException
 * @Description TODO
 * @Author jb.zhou
 * @Date 2020/1/7
 * @Version 1.0
 */
@Slf4j
public class Wrap {

    @FunctionalInterface
    public interface CheckedFunction<T,R> {
        R apply(T t) throws Exception;
    }

    public static <T,R> Function<T,R> of(CheckedFunction<T,R> checkedFunction) {
        return t -> {
            try {
                return checkedFunction.apply(t);
            } catch (Exception e) {
                log.error("Wrap Exception:",e);
                throw new RuntimeException(e);
            }
        };
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T, E extends Exception> {
        void accept(T t) throws E;
    }

    public static <T> Consumer<T> throwingConsumerWrapper(
            ThrowingConsumer<T, Exception> throwingConsumer) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public static <T, E extends Exception> Consumer<T> handlingConsumerWrapper(
            ThrowingConsumer<T, E> throwingConsumer, Class<E> exceptionClass) {

        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                try {
                    E exCast = exceptionClass.cast(ex);
                    log.error("Exception occured : " + exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }



    public static Consumer<Integer> wrapper(Consumer<Integer> consumer) {
        return i -> {
            try {
                consumer.accept(i);
            } catch (ArithmeticException e) {
                log.error("Arithmetic Exception occured : " + e.getMessage());
            }
        };
    }


    public static <T, E extends Exception> Consumer<T> consumerWrapper(Consumer<T> consumer, Class<E> clazz) {
        return i -> {
            try {
                consumer.accept(i);
            } catch (Exception ex) {
                try {
                    E exCast = clazz.cast(ex);
                    log.error("Exception occured : " + exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw ex;
                }
            }
        };
    }


    /**
     *
     * List<Integer> integers = Arrays.asList(3, 9, 7, 0, 10, 20);
     * integers.forEach(wrapper(i -> System.out.println(50 / i)));
     *
     * List<Integer> integers = Arrays.asList(3, 9, 7, 0, 10, 20);
     * integers.forEach(
     * consumerWrapper(
     * i -> System.out.println(50 / i),
     * ArithmeticException.class));
     */
}
