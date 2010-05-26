package com.porpoise.ga;

public interface IGenePool extends Iterable<GeneSequence> {

    boolean hasSolution();

    GeneSequence getSolution();

}
