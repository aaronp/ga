package com.porpoise;

public abstract class AbstractGene<T> implements IGene<T> {
    private final int geneId;

    public AbstractGene(final int id) {
        geneId = id;
    }

    public int getId() {
        return geneId;
    }

    @Override
    public String toString() {
        return Integer.toBinaryString(geneId);
    }

}
