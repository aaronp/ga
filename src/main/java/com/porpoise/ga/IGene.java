package com.porpoise.ga;

/**
 * 
 * @param <T>
 */
public interface IGene<T> {

    /**
     * The value represented by this gene
     * 
     * @return the gene's value
     */
    T getValue();

    /**
     * The type of this gene.
     * 
     * @return the gene's type
     */
    IGenotype<T> getType();

    /**
     * @return the zero-based position (index) of this gene in its gene sequence
     */
    int getPosition();

    /**
     * 
     * @param random
     * @return a new gene based on a mutation from this gene
     */
    IGene<T> mutate(float random);

    /**
     * @return a copy of this gene
     */
    IGene<T> copy();
}
