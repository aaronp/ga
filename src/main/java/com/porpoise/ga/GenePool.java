package com.porpoise.ga;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

class GenePool implements IGenePool {

    private final Collection<GeneSequence> population;
    private final IGeneEvaluation          geneEvaluation;
    private GeneSequence                   cachedSolution = null;

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
        if (cachedSolution == null) {
            GeneSequence solution = null;
            for (final GeneSequence sequence : this) {
                final float result = geneEvaluation.score(sequence);
                if (result == 0.0) {
                    solution = sequence;
                    break;
                }
            }
            cachedSolution = solution;
        }
        return cachedSolution;
    }

    @Override
    public Iterator<GeneSequence> iterator() {
        return population.iterator();
    }

    void populate(final GeneSequence seq) {
        markDirty();
        population.add(seq);
    }

    /**
     * 
     */
    private void markDirty() {
        cachedSolution = null;
    }

    @Override
    public String toString() {
        return Joiner.on(String.format("%n")).join(population);
    }
}
