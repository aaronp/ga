package com.porpoise.ga;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 
 * Creates gene sequences based on given {@link Genotype} patterns
 */
public final class GeneSequencer {

    private final List<Genotype<?>> genotypes;

    public GeneSequencer(final Genotype<?>... genotypeValues) {
        genotypes = Lists.newArrayList(genotypeValues);
    }

    public boolean addGenotype(final Genotype<?> type) {
        return genotypes.add(type);
    }

    public GeneSequence create() {
        final GeneSequence seq = newSequence();
        for (final Genotype<?> type : genotypes) {
            seq.addGene(type);
        }
        return seq;
    }

    /**
     * @return
     */
    protected GeneSequence newSequence() {
        final GeneSequence seq = new GeneSequence();
        return seq;
    }

    /**
     * @param criteria
     *            the criteria to use when evaluating the gene pool
     * @param size
     *            the initial size
     * @return a new gene pool of the given size
     */
    public <T extends Comparable<T>> IGenePool newPool(final IGeneEvaluation<T> criteria, final int size) {
        final GenePool<T> pool = new GenePool<T>(criteria);
        for (int i = 0; i < size; i++) {
            pool.populate(create());
        }
        return pool;
    }

}
