package com.porpoise.ga.impl;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.Lists;
import com.porpoise.ga.GeneSequence;
import com.porpoise.ga.IGeneEvaluation;
import com.porpoise.ga.IGenePool;

public class GenePool implements IGenePool {

    private final Collection<GeneSequence> population;
    private final IGeneEvaluation          geneEvaluation;

    public GenePool(final IGeneEvaluation eval) {
        geneEvaluation = eval;
        population = Lists.newArrayList();
    }

    @Override
    public boolean hasSolution() {
        final GeneSequence solution = getSolution();
        return solution != null;
    }

    /**
     * @return the gene sequence solution
     */
    @Override
    public GeneSequence getSolution() {
        GeneSequence solution = null;
        for (final GeneSequence sequence : this) {
            final float result = geneEvaluation.score(sequence);
            if (result >= 1.0) {
                solution = sequence;
                break;
            }
        }
        return solution;
    }

    @Override
    public Iterator<GeneSequence> iterator() {
        return population.iterator();
    }

    public void populate(final GeneSequence seq) {
        population.add(seq);

    }

}
