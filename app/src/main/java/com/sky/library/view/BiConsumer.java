package com.sky.library.view;

/**
 * Created by derik on 18-8-10.
 */

public interface BiConsumer<T, U> {
    /**
     * Performs this operation on the given arguments.
     *
     * 由于jdk中的这个接口对API有要求，所以直接代码里定义一个样的
     *
     * @param t the first input argument
     * @param u the second input argument
     */
    void accept(T t, U u);
}
