package com.porpoise.ga;

public class GeneImpl<T> implements IGene<T> {

    private final int         index;
    private final T           data;
    private final Genotype<T> genotype;

    public GeneImpl(final Genotype<T> type, final int geneIndex, final T value) {
        this.genotype = type;
        this.index = geneIndex;
        this.data = value;
    }

    @Override
    public Object getType() {
        return genotype.getType();
    }

    @Override
    public T getValue() {
        return data;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IGene<T> mutate(final float random) {
        return (IGene<T>) genotype.createGene(index);
    }

    @Override
    public String toString() {
        return data.toString();
    }

    /**
     * @return the index
     */
    public int getPosition() {
        return this.index;
    }
}
