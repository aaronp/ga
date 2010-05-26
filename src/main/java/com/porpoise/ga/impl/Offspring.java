package com.porpoise.ga.impl;

import com.porpoise.ga.GeneSequence;

public class Offspring {

    private final GeneSequence offspringOne;
    private final GeneSequence offspringTwo;

    public Offspring(final GeneSequence son, final GeneSequence daughter) {
        offspringOne = son;
        offspringTwo = daughter;

    }

    /**
     * @return the offspringOne
     */
    public GeneSequence getOffspringOne() {
        return offspringOne;
    }

    /**
     * @return the offspringTwo
     */
    public GeneSequence getOffspringTwo() {
        return offspringTwo;
    }

    public GeneSequence getOne(final boolean mutate) {
        return get(offspringOne, mutate);
    }

    public GeneSequence getTwo(final boolean mutate) {
        return get(offspringTwo, mutate);
    }

    private GeneSequence get(final GeneSequence offspring, final boolean mutate) {
        return mutate ? offspring.mutate() : offspring;
    }

}
