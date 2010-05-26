package com.porpoise.ga;

/**
 * A gene pool holds a number of GeneSequences. It should also be able to inspect those gene sequences for a 'solution'
 */
public interface IGenePool extends Iterable<GeneSequence> {

    boolean hasSolution();

    GeneSequence getSolution();

    void populate(GeneSequence seq);

}
