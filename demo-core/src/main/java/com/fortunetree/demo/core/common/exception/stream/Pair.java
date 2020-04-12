package com.fortunetree.demo.core.common.exception.stream;

/**
 * @ClassName Pair
 * @Description TODO
 * @Author jb.zhou
 * @Date 2020/1/7
 * @Version 1.0
 */
public class Pair<F,S> {
    public final F fst;
    public final S snd;
    private Pair(F fst, S snd) {
        this.fst = fst;
        this.snd = snd;
    }
    public static <F,S> Pair<F,S> of(F fst, S snd) {
        return new Pair<>(fst,snd);
    }
}