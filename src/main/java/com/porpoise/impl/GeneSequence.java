package com.porpoise.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.porpoise.IGene;
import com.porpoise.IMutator;
import com.porpoise.common.Iterators;
import com.porpoise.common.Lists;

public class GeneSequence<T> {
    private final List<? extends IGene<T>>     genes;
    private final Iterator<? extends IGene<T>> geneIter;
    private int                                size;

    public static <T> GeneSequence<GeneData<T>> of(final Collection<T> values) {
        final List<IGene<GeneData<T>>> geneValues = Lists.newArrayListWithExpectedSize(values.size());
        final GeneSequence<GeneData<T>> pool = new GeneSequence<GeneData<T>>(geneValues);
        int index = 0;
        for (final T value : values) {
            final GeneImpl<T> next = new GeneImpl<T>(pool, new GeneData<T>(index++, value));
            geneValues.add(next);
        }

        return pool;
    }

    public static <T> GeneSequence<GeneData<T>> of(final T... values) {
        return of(Arrays.asList(values));
    }

    private GeneSequence(final IGene<T>... values) {
        this(Arrays.asList(values));
    }

    private GeneSequence(final List<? extends IGene<T>> values) {
        size = values.size();
        assert size > 0;
        genes = values;
        geneIter = Iterators.cycle(genes);
    }

    public IGene<T> next(final IMutator m, final boolean cross) {

        final IGene<T> gene1 = choose();
        m.mutate(gene1);

        final IGene<T> gene;
        if (cross) {
            final IGene<T> gene2 = choose();
            gene = gene1.cross(gene2);
            m.mutate(gene2);
        } else {
            gene = gene1;
        }

        return gene;
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
