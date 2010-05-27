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
     * 
     * @param random
     * @return a new gene based on a mutation from this gene
     */
    IGene<T> mutate(float random);
}
