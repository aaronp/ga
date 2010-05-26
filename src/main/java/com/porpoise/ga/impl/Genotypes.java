package com.porpoise.ga.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.porpoise.common.Iterators;
import com.porpoise.common.Lists;
import com.porpoise.ga.IGene;

/**
 * Genotypes is a factory class, responsible for producing {@link IGene}s of a certain type
 * 
 * @param <T>
 */
public class Genotypes<T> {
    private final List<? extends IGene<T>>     genes;
    private final Iterator<? extends IGene<T>> geneIter;
    private int                                size;

    public static <T> Genotypes<GeneData<T>> of(final Collection<T> values) {
        final List<IGene<GeneData<T>>> geneValues = Lists.newArrayListWithExpectedSize(values.size());
        final Genotypes<GeneData<T>> pool = new Genotypes<GeneData<T>>(geneValues);
        int index = 0;
        for (final T value : values) {
            final GeneImpl<T> next = new GeneImpl<T>(pool, new GeneData<T>(index++, value));
            geneValues.add(next);
        }

        return pool;
    }

    public static <T> Genotypes<GeneData<T>> of(final T... values) {
        return of(Arrays.asList(values));
    }

    private Genotypes(final IGene<T>... values) {
        this(Arrays.asList(values));
    }

    private Genotypes(final List<? extends IGene<T>> values) {
        size = values.size();
        assert size > 0;
        genes = values;
        geneIter = Iterators.cycle(genes);
    }

    public IGene<T> next() {
        return choose();
    }

    /**
     * @return
     */
    private IGene<T> choose() {
        // TODO - choose based on fitness
        return geneIter.next();
    }

    public int size() {
        return size;
    }

    public IGene<T> get(final int index) {
        return genes.get(index);
    }

}
