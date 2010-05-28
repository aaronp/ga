package com.porpoise.ga;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

/**
 * A GeneSequence represents a specific sequence of {@link IGene}s which may be decoded to represent a specific
 * solution.
 * 
 * The sequence is expected to be interpreted by an {@link IGeneEvaluation} object, resulting in a {@link IScore} being
 * given to this sequence by which it can be rated against other gene sequence strains.
 */
public final class GeneSequence implements Iterable<IGene<?>> {

    private IScore<?>                              cachedScore;
    private final List<IGene<?>>                   genes;
    private final Multimap<Object, IGene<?>>       genesByValue;
    private final Multimap<IGenotype<?>, IGene<?>> genesByType;

    /**
     * @return a new gene sequence comprised entirely of the given values
     */
    public static <T> GeneSequence of(final T... values) {
        final IGenotype<T> type = Genotype.withFixedOrder(values);
        final GeneSequencer seq = new GeneSequencer();
        for (int i = values.length; --i >= 0;) {
            seq.addGenotype(type);
        }
        return seq.create();
    }

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
        genes = Lists.newArrayList();
        genesByValue = ArrayListMultimap.create();
        genesByType = ArrayListMultimap.create();

        for (final IGene<?> gene : geneValues) {
            putGeneInternal(gene);
        }
    }

    /**
     * @param index
     * @return the gene at the given index
     */
    @SuppressWarnings("unchecked")
    public <T> IGene<T> getGene(final int index) {
        return (IGene<T>) genes.get(index);
    }

    /**
     * @param value
     * @return the genes in this sequence which have the given value
     */
    public Collection<IGene<?>> getGenesByValue(final Object value) {
        return genesByValue.get(value);
    }

    /**
     * @param type
     * @param value
     * @return the genes which have the given type and value
     */
    public <T> Collection<IGene<?>> getGenesByTypeAndValue(final IGenotype<T> type, final Object value) {
        final Collection<IGene<?>> byValue = getGenesByValue(value);
        final Collection<IGene<?>> byType = getGenesByType(type);
        return Collections2.filter(byValue, Predicates.in(byType));
    }

    /**
     * the single gene of the given type and value. If no gene exists with the specified type and value, then a null
     * value will be returned. If multiple genes exist with the given type and value, then an
     * {@link IllegalArgumentException} will be thrown.
     * 
     * @param type
     * @param value
     * @return the single gene of the given type and value
     * @throws IllegalArgumentException
     */
    @SuppressWarnings("unchecked")
    public <T> IGene<T> getGeneOfTypeAndValue(final IGenotype<T> type, final Object value) {
        final Collection<IGene<?>> genesByTypeAndValue = getGenesByTypeAndValue(type, value);
        if (genesByTypeAndValue.isEmpty()) {
            return null;
        }
        return (IGene<T>) Iterables.getOnlyElement(genesByTypeAndValue);
    }

    /**
     * @param type
     * @return a collection of all genes of the given type
     */
    public Collection<IGene<?>> getGenesByType(final IGenotype<?> type) {
        return genesByType.get(type);
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
        return cross(pos, other);
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
        return cross(pos, other);
    }

    /**
     * cross this sequence with another at the given index
     * 
     * @param pos
     *            the position at which the sequences will be crossed
     * @param other
     *            the other sequence with which to cross with this one
     * 
     * @return the offspring from the cross
     */
    public Offspring cross(final int pos, final GeneSequence other) {
        assert size() == other.size();

        final GeneSequence copyA = copySequence();
        final GeneSequence copyB = other.copySequence();

        for (int index = pos; index < size(); index++) {
            final IGene<?> a = getGene(index).copy();
            final IGene<?> b = other.getGene(index).copy();
            copyA.setGene(index, b);
            copyB.setGene(index, a);
        }

        return new Offspring(copyA, copyB);
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
        final float zeroToOne = probability.nextFloat();
        final List<IGene<?>> copy = Lists.newArrayList(genes);
        final IGene<?> mutated = copy.get(pos).mutate(zeroToOne);
        final IGene<?> previous = copy.set(pos, mutated);
        if (previous.getValue().equals(mutated.getValue())) {
            final String format = "the gene's mutate method is broken. The mutated value is the same as the original value: %s == %s in %s for %d and %s";
            throw new IllegalStateException(String.format(format, previous.getValue(), mutated.getValue(), copy,
                    Integer.valueOf(pos), Float.valueOf(zeroToOne)));
        }
        return new GeneSequence(copy);
    }

    /**
     * @param <T>
     * @param type
     */
    final <T> void addGene(final IGenotype<T> type) {
        markDirty();
        final int position = genes.size();
        final IGene<?> newGene = type.createGene(position);
        putGeneInternal(newGene);
    }

    /**
     * @param newGene
     */
    private void putGeneInternal(final IGene<?> newGene) {
        genes.add(newGene);
        updateReferences(newGene);

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
     * IllegalArgumentException is thrown
     * 
     * @param mutation
     * @return the single difference
     * @throws IllegalArgumentException
     */
    public Pair<Integer, IGene<?>> onlyDiff(final GeneSequence other) throws IllegalArgumentException {
        final Collection<Pair<Integer, IGene<?>>> diff = diff(other);
        if (diff.size() != 1) {
            throw new IllegalArgumentException(String.format("diff between %s and %s => %s", this, other, diff));
        }
        return Iterables.getOnlyElement(diff);
    }

    @SuppressWarnings("unchecked")
    public <T> IGene<T> setGene(final int index, final IGene<T> newGene) {
        final GeneImpl<T> copy = copyGene(index, newGene);
        final IGene<?> replaced = genes.set(index, copy);
        genesByType.remove(replaced.getType(), replaced);
        genesByValue.remove(replaced.getValue(), replaced);

        updateReferences(copy);

        return (IGene<T>) replaced;
    }

    /**
     * @param <T>
     * @param copy
     */
    private <T> void updateReferences(final IGene<T> copy) {
        genesByType.put(copy.getType(), copy);
        genesByValue.put(copy.getValue(), copy);
    }

    /**
     * @param <T>
     * @param index
     * @param newGene
     * @return
     */
    private <T> GeneImpl<T> copyGene(final int index, final IGene<T> newGene) {
        final GeneImpl<T> newG = new GeneImpl<T>(newGene.getType(), index, newGene.getValue());
        return newG;
    }

    public Offspring crossBySwap(final Probability probability, final GeneSequence other) {
        final int position = probability.nextInt(size());
        return crossBySwap(position, other);
    }

    /**
     * @param pos
     * @param other
     * @return
     */
    public Offspring crossBySwap(final int pos, final GeneSequence other) {
        final GeneSequence copyA = copySequence();
        final GeneSequence copyB = other.copySequence();

        final IGene<?> geneToSwap = copyA.getGene(pos);
        for (final IGene<?> candidate : copyB.getGenesByType(geneToSwap.getType())) {
            if (!candidate.getValue().equals(geneToSwap.getValue())) {
                copyA.setGene(pos, candidate);
                copyB.setGene(candidate.getPosition(), geneToSwap);
                break;
            }
        }

        return new Offspring(copyA, copyB);
    }

    /**
     * swap two type values, maintaining value-uniqueness across the type.
     * 
     * consider the two sequences:
     * 
     * <pre>
     * 2-1-a-3-4
     * 3-1-b-2-4
     * </pre>
     * 
     * a normal {@link #crossBySwap(int, GeneSequence)} of the fourth gene would look like this:
     * 
     * <pre>
     * 2-1-a-3-4
     * 3-1-b-2-4
     * 
     * yields:
     * 
     *       |
     * 2-1-a-2-4
     * 3-1-b-3-2
     *       |
     * </pre>
     * 
     * But if we are to maintain unique values across type (and assuming the numeric genes all came from the same genome
     * and hence are the same 'type'), the same swap then triggers:
     * 
     * <pre>
     * 2-1-a-3-4
     * 3-1-b-2-4
     * 
     * yields:
     * 
     *       |
     * 2-1-a-2-4
     * 3-1-b-3-2
     *       |
     *       
     * then 
     * 
     * |     
     * 3-1-a-4-4
     * 2-1-b-3-2
     * |
     * 
     * then
     *  
     *         |     
     * 3-1-a-4-2
     * 2-1-b-3-4
     *         |
     * 
     * </pre>
     * 
     * @param pos
     * @param other
     * @return the offspring
     */
    public Offspring crossBySwapUniqueValuesByType(final int pos, final GeneSequence other) {
        final GeneSequence copyA = copySequence();
        final GeneSequence copyB = other.copySequence();

        return swapUniqueRecursive(copyA, copyB, pos, other);
    }

    /**
     * see {@link #crossBySwapUniqueValuesByType(int, GeneSequence)}
     * 
     * @param probability
     *            the probability used to find a random pivot point at which to cross
     * @param other
     *            the other sequence with which to cross
     * @return the offspring
     */
    public Offspring crossBySwapUniqueValuesByType(final Probability probability, final GeneSequence other) {
        final int index = probability.nextInt(size());
        return crossBySwapUniqueValuesByType(index, other);
    }

    /**
     * @param pos
     * @param other
     * @return
     */
    private Offspring swapUniqueRecursive(final GeneSequence copyA, final GeneSequence copyB, final int pos,
            final GeneSequence other) {
        //
        // 1) pick the gene to swap 'A' from this sequence
        //
        final IGene<?> sourceGeneToSwap = copyA.getGene(pos);

        //
        // 2) get the gene in the same position from the other sequence
        // (assert it is of the same type)
        //
        final IGene<?> targetGeneToSwap = copyB.getGene(pos);
        assert sourceGeneToSwap.getType().equals(targetGeneToSwap.getType());

        //
        // 3) if the gene to swap has the same value, then we're done (nowt to do)
        //
        if (sourceGeneToSwap.getValue().equals(targetGeneToSwap.getValue())) {
            return new Offspring(copyA, copyB);
        }

        //
        // 4) find the other gene in 'other' with the same value and type as the source gene we're swapping
        //
        final IGene<?> targetGeneOfSameValue = copyB.getGeneOfTypeAndValue(sourceGeneToSwap.getType(), sourceGeneToSwap
                .getValue());
        if (targetGeneOfSameValue != null) {
            assert targetGeneOfSameValue.getType().equals(sourceGeneToSwap.getType());
            assert targetGeneOfSameValue.getValue().equals(sourceGeneToSwap.getValue());
            assert targetGeneOfSameValue.getPosition() != targetGeneToSwap.getPosition();
        }

        //
        // 5) swap the genes
        //
        final int index = sourceGeneToSwap.getPosition();
        assert index == targetGeneToSwap.getPosition();
        copyA.setGene(index, targetGeneToSwap);
        copyB.setGene(sourceGeneToSwap.getPosition(), sourceGeneToSwap);

        //
        // 6) if the swapped gene wasn't already in the target sequence, then we won't have a duplicate
        //
        if (targetGeneOfSameValue == null) {
            return new Offspring(copyA, copyB);
        }

        //
        // 7) if all values are unique in either sequence, then we're done
        //
        if (copyA.hasUniqueValuesForType(sourceGeneToSwap.getType())
                && copyB.hasUniqueValuesForType(sourceGeneToSwap.getType())) {
            assert copyB.hasUniqueValuesForType(sourceGeneToSwap.getType());
            return new Offspring(copyA, copyB);
        }

        //
        // 8) otherwise, recurse on step #4's position
        //
        return swapUniqueRecursive(copyA, copyB, targetGeneOfSameValue.getPosition(), other);
    }

    /**
     * @param type
     * @return true if all genes of the given type have unique values
     */
    public final boolean hasUniqueValuesForType(final IGenotype<?> type) {
        final Collection<IGene<?>> gbt = getGenesByType(type);
        final Collection<Object> values = Collections2.transform(gbt, new Function<IGene<?>, Object>() {
            @Override
            public Object apply(final IGene<?> arg0) {
                return arg0.getValue();
            }
        });
        final boolean uniqueValues = Sets.newHashSet(values).size() == gbt.size();
        return uniqueValues;
    }

    final GeneSequence copySequence() {
        final List<IGene<?>> geneCopy = newGenes();
        for (int index = 0; index < size(); index++) {
            final IGene<?> a = getGene(index).copy();
            geneCopy.add(a);
        }
        return new GeneSequence(geneCopy);
    }

    private List<IGene<?>> newGenes() {
        return Lists.newArrayListWithExpectedSize(size());
    }
}
