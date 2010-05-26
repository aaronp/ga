package com.porpoise.common;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.collect.Lists;

public class ProportionalIterator<T> implements Iterator<T> {

    private final List<T>    sortedListBestFirst;
    private final Proportion proportion;

    public ProportionalIterator(final Proportion prop, final List<T> sortedListBestFirstValue) {
        this.proportion = prop;
        sortedListBestFirst = Lists.newArrayList(sortedListBestFirstValue);
    }

    @Override
    public boolean hasNext() {
        return !sortedListBestFirst.isEmpty();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("iterator is empty");
        }
        if (sortedListBestFirst.size() == 1) {
            return sortedListBestFirst.remove(0);
        }
        final int index = proportion.chooseAscending(sortedListBestFirst.size());
        assert index < sortedListBestFirst.size();
        return sortedListBestFirst.remove(index);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
