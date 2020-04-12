package com.fortunetree.demo.core.common.exception.stream;

import java.util.Optional;
import java.util.function.Function;

/**
 * @ClassName Either
 * @Description TODO
 * @Author jb.zhou
 * @Date 2020/1/7
 * @Version 1.0
 */
public class Either<L, R> {

    private final L left;
    private final R right;
    private Either(L left, R right) {
        this.left = left;
        this.right = right;
    }
    public static <L,R> Either<L,R> Left( L value) {
        return new Either(value, null);
    }
    public static <L,R> Either<L,R> Right( R value) {
        return new Either(null, value);
    }
    public Optional<L> getLeft() {
        return Optional.ofNullable(left);
    }
    public Optional<R> getRight() {
        return Optional.ofNullable(right);
    }
    public boolean isLeft() {
        return left != null;
    }
    public boolean isRight() {
        return right != null;
    }
    public <T> Optional<T> mapLeft(Function<? super L, T> mapper) {
        if (isLeft()) {
            return Optional.of(mapper.apply(left));
        }
        return Optional.empty();
    }
    public <T> Optional<T> mapRight(Function<? super R, T> mapper) {
        if (isRight()) {
            return Optional.of(mapper.apply(right));
        }
        return Optional.empty();
    }

    @FunctionalInterface
    public interface CheckedFunction<T,R> {
        R apply(T t) throws Exception;
    }

    public static <T,R> Function<T, Either> toLift(CheckedFunction<T,R> function) {
        return t -> {
            try {
                return Either.Right(function.apply(t));
            } catch (Exception ex) {
                return Either.Left(ex);
            }
        };
    }

    public static <T,R> Function<T, Either> toLiftWithValue(CheckedFunction<T,R> function) {
        return t -> {
            try {
                return Either.Right(function.apply(t));
            } catch (Exception ex) {
                return Either.Left(Pair.of(ex,t));
            }
        };
    }

    @Override
    public String toString() {
        if (isLeft()) {
            return "Left(" + left +")";
        }
        return "Right(" + right +")";
    }
}
