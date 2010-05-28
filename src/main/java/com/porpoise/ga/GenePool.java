package com.porpoise.ga;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.porpoise.common.Proportion;
import com.porpoise.common.ProportionalIterator;

class GenePool<T extends Comparable<T>> implements IGenePool
{

    private final Comparator<GeneSequence> seqComparator;
    private final List<GeneSequence>       population;
    private final IGeneEvaluation<T>       geneEvaluation;

    private GeneSequence                   cachedSolution = null;
    private List<GeneSequence>             cachedSortedPopulation;
    private Probability                    probability;

    public GenePool(final IGeneEvaluation<T> eval)
    {
        this(eval, Probability.getInstance());

    }

    public GenePool(final IGeneEvaluation<T> eval, final Probability prob)
    {
        this.geneEvaluation = eval;
        this.probability = prob;
        this.population = Lists.newLinkedList();
        final Comparator<GeneSequence> increasing = new Comparator<GeneSequence>() {
            @Override
            public int compare(final GeneSequence o1, final GeneSequence o2)
            {
                final IScore<T> score1 = o1.getScore(eval);
                final IScore<T> score2 = o2.getScore(eval);
                return score1.compareTo(score2);
            }
        };
        // should be ordered in decreasing order
        this.seqComparator = Ordering.from(increasing).reverse();
    }

    @Override
    public boolean hasSolution()
    {
        final GeneSequence solution = getSolution();
        return solution != null;
    }

    /**
     * @return the gene sequence solution
     */
    @Override
    public GeneSequence getSolution()
    {
        if (this.cachedSolution == null)
        {
            GeneSequence solution = null;
            for (final GeneSequence sequence : this)
            {
                final IScore<T> score = this.geneEvaluation.score(sequence);
                if (score.isComplete() && score.isValid())
                {
                    solution = sequence;
                    break;
                }
            }
            this.cachedSolution = solution;
        }
        return this.cachedSolution;
    }

    @Override
    public Iterator<GeneSequence> iterator()
    {
        if (this.cachedSortedPopulation == null)
        {
            this.cachedSortedPopulation = Lists.newArrayListWithExpectedSize(this.population.size());
            // sequences are sorted on score, so ensure they are all scored first
            for (final GeneSequence s : this.population)
            {
                s.getScore(this.geneEvaluation);
                this.cachedSortedPopulation.add(s);
            }
            Collections.sort(this.cachedSortedPopulation, this.seqComparator);
        }
        return ProportionalIterator.ascending(this.probability.getPoolProportion(), this.cachedSortedPopulation);
    }

    @Override
    public void thinPopulation()
    {

        final SortedSet<Integer> indices = getIndicesToRemove();

        // iterate over indices, largest first
        Integer last = null;
        for (final Integer index : indices)
        {
            assert last == null || last.intValue() > index.intValue();

            final GeneSequence remove = this.population.remove(index.intValue());
            assert remove != null;

            last = index;
        }
    }

    /**
     * @return a sorted set of indices to cull from the population, largest first
     */
    final SortedSet<Integer> getIndicesToRemove()
    {
        final int size = this.population.size();
        final int amountToCull = this.probability.nextNumberToThin(size);
        final Proportion proportion = this.probability.getPoolProportion();

        final SortedSet<Integer> indices = Sets.newTreeSet(Ordering.natural().reverse());
        while (indices.size() < amountToCull)
        {
            final int index = proportion.chooseAscending(size);
            indices.add(Integer.valueOf(index));
        }
        return indices;
    }

    /**
     */
    public void populate(final GeneSequence seq)
    {
        markDirty();
        this.population.add(seq);
    }

    /**
     * 
     */
    private void markDirty()
    {
        this.cachedSolution = null;
        this.cachedSortedPopulation = null;
    }

    @Override
    public String toString()
    {
        return Joiner.on(String.format("%n")).join(this.population);
    }
}
