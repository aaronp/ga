package com.porpoise.ga;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.porpoise.common.RandomizingIter;

/**
 * Genotype is a factory class, responsible for producing {@link IGene}s of a certain type
 * 
 * @param <T>
 */
public class Genotype<T> {

    private final List<T>     genes;
    private final Iterator<T> geneIter;
    private final int         size;
    private final Object      type;

    public static <T> Genotype<T> of(final Object type, final Collection<T> values) {
        final Genotype<T> pool = new Genotype<T>(type, values);
        return pool;
    }

    public static <T> Genotype<T> of(final Collection<T> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("One or more values must be supplied");
        }
        final Object type = Iterables.get(values, 0).getClass();
        return of(type, values);
    }

    public static Builder withType(final Object type) {
        return new Builder(type);
    }

    public static <T> Genotype<T> of(final T... values) {
        return of(Arrays.asList(values));
    }

    public static class Builder {
        private final Object type;

        public Builder(final Object typeParam) {
            this.type = typeParam;
        }

        public <T> Genotype<T> of(final T... values) {
            return new Genotype<T>(type, Arrays.asList(values));
        }
    }

    private Genotype(final Object genotypeType, final Collection<T> values) {
        size = values.size();
        assert size > 0;
        genes = Lists.newArrayList(values);
        geneIter = new RandomizingIter<T>(genes);
        if (genotypeType == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        type = genotypeType;
    }

    public int size() {
        return size;
    }

    public T create() {
        return geneIter.next();
    }

    public IGene<?> createGene(final int position) {
        return new GeneImpl<T>(this, position, create());
    }

    public Object getType() {
        return type;
    }

}
