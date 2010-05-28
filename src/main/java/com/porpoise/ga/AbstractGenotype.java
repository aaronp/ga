package com.porpoise.ga;

import java.util.Iterator;

/**
 * Genotype is a factory class, responsible for producing {@link IGene}s of a certain type
 * 
 * @param <T>
 */
public abstract class AbstractGenotype<T> implements IGenotype<T>
{

    private final Iterator<T> geneIter;

    protected AbstractGenotype(final Iterator<T> valueIterator)
    {
        this.geneIter = valueIterator;
    }

    public T create()
    {
        return this.geneIter.next();
    }

    public IGene<T> createGene(final int position)
    {
        return new GeneImpl<T>(this, position, create());
    }
}
