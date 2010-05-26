package com.porpoise.ga;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.porpoise.common.ProportionalIterator;

class GenePool implements IGenePool {

    private final Comparator<GeneSequence> seqComparator;
    private final List<GeneSequence>       population;
    private final IGeneEvaluation          geneEvaluation;

    private GeneSequence                   cachedSolution = null;
    private List<GeneSequence>             cachedSortedPopulation;

    public GenePool(final IGeneEvaluation eval) {
        geneEvaluation = eval;
        population = Lists.newLinkedList();
        final Comparator<GeneSequence> increasing = new Comparator<GeneSequence>() {
            @Override
            public int compare(final GeneSequence o1, final GeneSequence o2) {
                final float score1 = o1.getScore(eval);
                final float score2 = o2.getScore(eval);
                return Float.compare(score1, score2);
            }
        };
        // should be ordered in decreasting order
        seqComparator = Ordering.from(increasing).reverse();
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
        if (cachedSortedPopulation == null) {
            cachedSortedPopulation = population;
            Collections.sort(cachedSortedPopulation, seqComparator);
        }
        return new ProportionalIterator(cachedSortedPopulation);
    }

    /**
     */
    public void populate(final GeneSequence seq) {
        markDirty();
        population.add(seq);
    }

    /**
     * 
     */
    private void markDirty() {
        cachedSolution = null;
        cachedSortedPopulation = null;
    }

    @Override
    public String toString() {
        return Joiner.on(String.format("%n")).join(population);
    }
}
