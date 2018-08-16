package com.fih.idx.deriklibrary.custom;

/**
 * Created by derik on 18-8-10.
 */

public interface Consumer<T> {
    /**
     * Performs this operation on the given argument.
     *
     * 由于jdk中的这个接口对API有要求，所以直接代码里定义一个样的
     *
     * @param t the input argument
     */
    void accept(T t);
}
