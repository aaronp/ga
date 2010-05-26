package com.porpoise.common;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.collect.Lists;

/**
 * A {@code ProportionalIterator} iterates over its elements in a pseudo-random manor. It returns elements from its
 * given {@link Iterable} based on that iterable's sort order with a preference to use elements from parts of the
 * iterable based on the proportions given.
 * 
 * For example, given a list of increasing integers between 0 and 10 and a proportion which states that it will try to
 * return elements from the top 20% of the list 90% of the time, then you would expect that the elements may be returned
 * something like this:
 * <ol>
 * <li>2</li>
 * <li>1</li>
 * <li>0</li>
 * <li>8</li>
 * <li>5</li>
 * <li>3</li>
 * <li>4</li>
 * <li>7</li>
 * <li>10</li>
 * <li>9</li>
 * </ol>
 * 
 * @param <T>
 */
public class ProportionalIterator<T> implements Iterator<T> {

    private final List<T>    sortedListBestFirst;
    private final Proportion proportion;

    public ProportionalIterator(final Proportion prop, final Iterable<T> sortedListBestFirstValue) {
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
