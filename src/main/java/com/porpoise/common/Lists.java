package com.porpoise.common;

import java.util.List;

public enum Lists {
    ;

    public static <T> List<T> newArrayList() {
        return com.google.common.collect.Lists.newArrayList();
    }//

    public static <T> List<T> newArrayListWithExpectedSize(final int size) {
        return com.google.common.collect.Lists.newArrayListWithExpectedSize(size);
    }

    public static <T> List<T> newArrayList(final Iterable<T> values) {
        return com.google.common.collect.Lists.newArrayList(values);
    }
}
