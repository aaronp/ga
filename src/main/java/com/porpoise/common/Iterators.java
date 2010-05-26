package com.porpoise.common;

import java.util.Iterator;

public enum Iterators {
    ;

    public static <T> Iterator<T> cycle(final Iterable<T> iterable) {
        return com.google.common.collect.Iterators.cycle(iterable);
    }

}
