package com.porpoise.ga.impl;

import java.util.List;

import com.porpoise.ga.GeneSequence;
import com.porpoise.ga.IGene;

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

    public List<IGene<?>> getOffspringOneGenes() {
        return getOffspringOne().getGenes();
    }

    public List<IGene<?>> getOffspringTwoGenes() {
        return getOffspringTwo().getGenes();
    }

    public IGene<?> getOneGene(final int index) {
        return getOffspringOne().getGene(index);
    }

    public IGene<?> getTwoGene(final int index) {
        return getOffspringTwo().getGene(index);
    }

    @Override
    public String toString() {
        return String.format("Offspring:%n%s%n%s", getOffspringOne(), getOffspringTwo());
    }
}
