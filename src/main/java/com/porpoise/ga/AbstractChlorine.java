package com.porpoise.ga;

import java.util.Iterator;

/**
 * Implementation of {@link IChlorine} This class is able to take a n {@link IGenePool} and transform ('evolve') it into another
 */
public abstract class AbstractChlorine implements IChlorine
{

    private final Probability probability;

    public AbstractChlorine(final Probability p)
    {
        this.probability = p;
    }

    /**
     * convert one gene pool into another
     */
    @Override
    public IGenePool evolve(final IGenePool pool)
    {
        final IGenePool newPool = pool;// new GenePool(eval);

        final Iterator<GeneSequence> iter = pool.iterator();
        while (iter.hasNext())
        {
            final GeneSequence seqOne = iter.next();

            // if we have an odd number in our gene pool then we may need to terminate early
            if (!iter.hasNext())
            {
                if (this.probability.nextMutate())
                {
                    final GeneSequence newSeq = doMutate(seqOne);
                    newPool.populate(newSeq);
                }
                else
                {
                    newPool.populate(seqOne);
                }
                continue;
            }

            final GeneSequence seqTwo = iter.next();

            // potentially cross the two sequences
            final Offspring offspring = cross(seqOne, seqTwo);

            //
            // Get the first offspring
            //
            GeneSequence one = offspring.getOffspringOne();
            if (this.probability.nextMutate())
            {
                one = doMutate(offspring.getOffspringOne());
            }
            newPool.populate(one);

            //
            // Get the second offspring
            //
            GeneSequence two = offspring.getOffspringTwo();
            if (this.probability.nextMutate())
            {
                two = doMutate(offspring.getOffspringTwo());
            }
            newPool.populate(two);
        }

        // thin out the pool
        newPool.thinPopulation();

        return newPool;
    }

    /**
     * @param seqOne
     * @return the mutated sequence
     */
    protected abstract GeneSequence doMutate(final GeneSequence seqOne);

    /**
     * @param seqOne
     * @param seqTwo
     * @return
     */
    protected Offspring cross(final GeneSequence seqOne, final GeneSequence seqTwo)
    {
        final Offspring offspring;
        if (this.probability.nextCross())
        {
            offspring = doCross(seqOne, seqTwo);
        }
        else
        {
            offspring = new Offspring(seqOne, seqTwo);
        }
        return offspring;
    }

    /**
     * @param seqOne
     * @param seqTwo
     * @return the offspring
     */
    protected abstract Offspring doCross(final GeneSequence seqOne, final GeneSequence seqTwo);

    /**
     * @return the probability
     */
    protected Probability getProbability()
    {
        return this.probability;
    }
}
