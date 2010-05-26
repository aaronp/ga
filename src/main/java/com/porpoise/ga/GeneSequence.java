package com.porpoise.ga;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.porpoise.common.Lists;
import com.porpoise.ga.impl.Offspring;

public class GeneSequence implements Iterable<IGene<?>> {
    private final List<IGene<?>> genes;

    public GeneSequence(final IGene<?>... geneValues) {
        this(Arrays.asList(geneValues));
    }

    private GeneSequence(final List<IGene<?>> geneValues) {
        genes = geneValues;
    }

    /**
     * @return the genes
     */
    public List<IGene<?>> getGenes() {
        return ImmutableList.<IGene<?>> copyOf(genes);
    }

    @Override
    public Iterator<IGene<?>> iterator() {
        return genes.iterator();
    }

    public int size() {
        return genes.size();
    }

    public Offspring cross(final GeneSequence other) {
        final Probability probability = Probability.getInstance();

        assert size() == other.size();
        final int os = probability.nextInt(size());

        Lists.newArrayList(genes);
        Lists.newArrayList(other.genes);

        return null;

    }

    /**
     * mutate the gene sequence, altering one gene in the sequence
     * 
     * @return the mutated gene sequence
     */
    public GeneSequence mutate() {
        final Probability probability = Probability.getInstance();
        return mutate(probability);
    }

    /**
     * @param probability
     * @return
     */
    GeneSequence mutate(final Probability probability) {
        final int pos = probability.nextInt(genes.size());
        final List<IGene<?>> copy = Lists.newArrayList(genes);
        final IGene<?> mutated = copy.get(pos).mutate(probability.nextFloat());
        copy.set(pos, mutated);
        return new GeneSequence(copy);
    }

}
