package com.porpoise.common;

import java.util.Iterator;
import java.util.List;

import com.porpoise.ga.GeneSequence;

public class ProportionalIterator<T> implements Iterator<GeneSequence> {

    private final List<T> sortedListBestFirst;

    public ProportionalIterator(final List<T> sortedListBestFirstValue) {
        sortedListBestFirst = sortedListBestFirstValue;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public GeneSequence next() {
        return null;
    }

    @Override
    public void remove() {

    }
}
