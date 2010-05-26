package com.porpoise.ga;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class GeneSequence implements Iterable<IGene<?>> {
    private final List<IGene<?>> genes;

    public GeneSequence(final Iterable<IGene<?>> geneValues) {
        this(Lists.newArrayList(geneValues));
    }

    public GeneSequence(final IGene<?>... geneValues) {
        this(Arrays.asList(geneValues));
    }

    private GeneSequence(final List<IGene<?>> geneValues) {
        genes = Lists.newArrayList(geneValues);
    }

    /**
     * @param index
     * @return the gene at the given index
     */
    public IGene<?> getGene(final int index) {
        return genes.get(index);
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
        final int pos = probability.nextInt(size());
        return cross(other, pos);
    }

    /**
     * @param other
     * @param pos
     * @return
     */
    Offspring cross(final GeneSequence other, final int pos) {
        assert size() == other.size();

        final List<IGene<?>> copyOne = Lists.newArrayList(genes);
        final List<IGene<?>> copyTwo = Lists.newArrayList(other.genes);
        for (int index = pos; index < size(); index++) {
            final IGene<?> a = copyOne.get(index);
            final IGene<?> b = copyTwo.get(index);

            copyOne.set(index, b);
            copyTwo.set(index, a);
        }

        return new Offspring(new GeneSequence(copyOne), new GeneSequence(copyTwo));
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (genes == null ? 0 : genes.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GeneSequence other = (GeneSequence) obj;
        if (genes == null) {
            if (other.genes != null) {
                return false;
            }
        } else if (!genes.equals(other.genes)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return Joiner.on("-").join(genes);
    }

    final <T> void addGene(final Genotype<T> type) {
        final int position = genes.size();
        genes.add(type.createGene(position));
    }

    /**
     * @param <T>
     * @param index
     * @return the gene value at the given index
     */
    public <T> T getGeneValue(final int index) {
        final IGene<?> g = getGene(index);
        return (T) g.getValue();
    }

    public int getGeneIntValue(final int index) {
        final Integer value = getGeneValue(index);
        return value.intValue();
    }
}
