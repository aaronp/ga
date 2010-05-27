package com.porpoise.ga;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.porpoise.common.RandomizingIter;

/**
 * Genotype is a factory class, responsible for producing {@link IGene}s of a certain type
 * 
 * @param <T>
 */
public class Genotype<T> implements IGenotype<T> {

    private final List<T>     genes;
    private final Iterator<T> geneIter;
    private final int         size;

    public static <T> Genotype<T> of(final Collection<T> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("One or more values must be supplied");
        }
        final Genotype<T> pool = new Genotype<T>(values);
        return pool;
    }

    public static <T> Genotype<T> of(final T... values) {
        return of(Arrays.asList(values));
    }

    private Genotype(final Collection<T> values) {
        size = values.size();
        assert size > 0;
        genes = Lists.newArrayList(values);
        geneIter = new RandomizingIter<T>(genes);
    }

    public int size() {
        return size;
    }

    public T create() {
        return geneIter.next();
    }

    public IGene<T> createGene(final int position) {
        return new GeneImpl<T>(this, position, create());
    }
}
