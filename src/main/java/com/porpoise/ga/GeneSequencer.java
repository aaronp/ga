package com.porpoise.ga;

import java.util.Arrays;
import java.util.List;

import com.porpoise.ga.impl.Genotype;

/**
 * 
 * Creates gene sequences based on given {@link Genotype} patterns
 */
public class GeneSequencer {

    private final List<Genotype<?>> genotypes;

    public GeneSequencer(final Genotype<?>... genotypes) {
        this.genotypes = Arrays.asList(genotypes);
    }

    public GeneSequence create() {
        final GeneSequence seq = new GeneSequence();
        for (final Genotype<?> type : genotypes) {
            seq.addGene(type.create());
        }
        return seq;
    }

}
