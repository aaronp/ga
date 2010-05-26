package com.porpoise.ga;

import com.google.common.base.Joiner;

public class Result {

    private final int       generation;
    private final IGenePool finalPool;

    public Result(final int generationNumber, final IGenePool pool) {
        generation = generationNumber;
        finalPool = pool;
    }

    public GeneSequence getSolution() {
        return finalPool.getSolution();
    }

    public int getGeneration() {
        return generation;
    }

    @Override
    public String toString() {
        return Joiner.on(String.format("%n")).join(getSolution().getGenes());
    }

}
