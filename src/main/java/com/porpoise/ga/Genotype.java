package com.porpoise.ga;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.collect.Lists;
import com.porpoise.common.RandomizingIter;

/**
 * Genotype is a factory class, responsible for producing {@link IGene}s of a certain type
 * 
 * @param <T>
 */
public class Genotype<T> extends AbstractGenotype<T> {

    public static <T> IGenotype<T> of(final Collection<T> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("One or more values must be supplied");
        }
        return new Genotype<T>(values);
    }

    /**
     * @param <T>
     * @param values
     * @return a genotype which will repeat the given values in a random order
     */
    public static <T> IGenotype<T> of(final T... values) {
        return of(Arrays.asList(values));
    }

    /**
     * @param <T>
     * @param values
     * @return a genotype which will repeat the given values in a random order
     */
    public static <T> IGenotype<T> ofFixedOrder(final T... values) {
        return of(Arrays.asList(values));
    }

    private Genotype(final Iterable<T> values) {
        super(new RandomizingIter<T>(Lists.newArrayList(values)));
    }
}
