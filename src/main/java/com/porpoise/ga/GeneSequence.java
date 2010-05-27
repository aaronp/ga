package com.porpoise.ga;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * A GeneSequence represents a specific sequence of {@link IGene}s which may be decoded to represent a specific
 * solution.
 * 
 * The sequence is expected to be interpreted by an {@link IGeneEvaluation} object, resulting in a {@link IScore} being
 * given to this sequence by which it can be rated against other gene sequence strains.
 */
public final class GeneSequence implements Iterable<IGene<?>> {

    private IScore<?>            cachedScore;
    private final List<IGene<?>> genes;

    /**
     * @param geneValues
     *            the gene values used to build this sequence
     */
    public GeneSequence(final Iterable<IGene<?>> geneValues) {
        this(Lists.newArrayList(geneValues));
    }

    /**
     * @param geneValues
     *            the gene values used to build this sequence
     */
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

    /**
     * @return an iterator which may iterate over the genes in this list
     */
    @Override
    public Iterator<IGene<?>> iterator() {
        return getGenes().iterator();
    }

    /**
     * @return the number of genes in this sequence
     */
    public int size() {
        return genes.size();
    }

    /**
     * cross this sequence with another, resulting in two offspring
     * 
     * @param rand
     * @param other
     *            the other sequence with which to cross with this sequence
     * @return the offspring resulting from the 'breeding' (crossing) of these two sequences
     */
    public Offspring cross(final Random rand, final GeneSequence other) {
        final int pos = rand.nextInt(size());
        return cross(other, pos);
    }

    /**
     * cross this sequence with another, resulting in two offspring
     * 
     * @param probability
     * @param other
     *            the other sequence with which to cross with this sequence
     * @return the offspring resulting from the 'breeding' (crossing) of these two sequences
     */
    public Offspring cross(final Probability probability, final GeneSequence other) {
        final int pos = probability.nextInt(size());
        return cross(other, pos);
    }

    /**
     * cross this sequence with another at the given index
     * 
     * @param other
     *            the other sequence with which to cross with this one
     * @param pos
     *            the position at which the sequences will be crossed
     * @return the offspring from the cross
     */
    public Offspring cross(final GeneSequence other, final int pos) {
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
     * mutate the gene sequence, altering one gene in the sequence randomly
     * 
     * @return a new mutated gene sequence created from this sequence
     */
    public GeneSequence mutate() {
        final Probability probability = Probability.getInstance();
        return mutate(probability);
    }

    /**
     * @param probability
     *            the probability (random) object used to determine the gene which will be mutated
     * @return a new mutated gene sequence created from this sequence
     */
    public GeneSequence mutate(final Probability probability) {
        final int pos = probability.nextInt(genes.size());
        final List<IGene<?>> copy = Lists.newArrayList(genes);
        final IGene<?> mutated = copy.get(pos).mutate(probability.nextFloat());
        copy.set(pos, mutated);
        return new GeneSequence(copy);
    }

    /**
     * 
     * @param <T>
     * @param type
     */
    final <T> void addGene(final Genotype<T> type) {
        markDirty();
        final int position = genes.size();
        genes.add(type.createGene(position));
    }

    private void markDirty() {
        cachedScore = null;
    }

    /**
     * @param <T>
     * @param index
     * @return the gene value at the given index
     */
    @SuppressWarnings("unchecked")
    public <T> T getGeneValue(final int index) {
        final IGene<?> g = getGene(index);
        return (T) g.getValue();
    }

    public int getGeneIntValue(final int index) {
        final Integer value = getGeneValue(index);
        return value.intValue();
    }

    @SuppressWarnings("unchecked")
    public <T extends Comparable<T>> IScore<T> getScore(final IGeneEvaluation<T> eval) {
        if (cachedScore == null) {
            cachedScore = eval.score(this);
        }
        return (IScore<T>) cachedScore;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (genes == null ? 0 : genes.hashCode());
        return result;
    }

    /**
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

    /**
     * @return the string representation of these sequence
     */
    @Override
    public String toString() {
        final String separator = "-";
        return toString(separator);
    }

    /**
     * @param separator
     * @return a string representation of this sequence
     */
    public String toString(final String separator) {
        final String string = Joiner.on(separator).join(genes);
        return cachedScore == null ? string : String.format("[%s] %s", cachedScore, string);
    }

    /**
     * return the genes which differ between this and the given strain.
     * 
     * The first object in the difference ({@link Pair}) will be the index of the difference. The second object in the
     * difference ({@link Pair}) will be the different gene from the 'other' parameter's sequence.
     * 
     * @param other
     *            the gene sequence to compare to
     * @return a collection of differences
     */
    public Collection<Pair<Integer, IGene<?>>> diff(final GeneSequence other) {
        final List<Pair<Integer, IGene<?>>> diff = Lists.newArrayList();
        assert size() == other.size();
        for (int i = 0; i < size(); i++) {
            final IGene<?> geneA = getGene(i);
            final IGene<?> geneB = other.getGene(i);
            if (!geneA.equals(geneB)) {
                diff.add(new Pair<Integer, IGene<?>>(Integer.valueOf(i), geneB));
            }
        }
        return diff;
    }

    /**
     * method for getting the single difference between two genes. If there are any fewer or more than one difference an
     * IllegalArgumentException is thown
     * 
     * @param mutation
     * @return the single difference
     * @throws IllegalArgumentException
     */
    public Pair<Integer, IGene<?>> onlyDiff(final GeneSequence other) throws IllegalArgumentException {
        final Collection<Pair<Integer, IGene<?>>> diff = diff(other);
        return Iterables.getOnlyElement(diff);
    }

    public void setGene(final int index, final IGene<?> newGene) {
        genes.set(index, newGene);
    }
}
