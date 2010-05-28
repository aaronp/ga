package com.porpoise.common;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;

/**
 * Iterator which will iterate through its elements, then cycle once finished. The second iteration through, however, will iterate through
 * the elements in a different order
 */
public final class RandomizingIter<T> extends AbstractIterator<T>
{
    private int           iteratorIndex = 0;
    private final List<T> elements;

    public RandomizingIter(final Iterable<T> values)
    {
        this.elements = Lists.newLinkedList(values);
    }

    @Override
    protected T computeNext()
    {
        if (this.iteratorIndex == this.elements.size())
        {
            Collections.shuffle(this.elements);
            this.iteratorIndex = 0;
        }

        final T next = this.elements.get(this.iteratorIndex);
        this.iteratorIndex++;
        return next;
    }
}