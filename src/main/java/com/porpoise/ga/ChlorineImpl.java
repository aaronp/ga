package com.porpoise.ga;

import java.util.Iterator;

/**
 * Implementation of {@link IChlorine}
 * 
 * This class is able to take a n {@link IGenePool} and transform ('evolve') it into another
 * 
 */
public class ChlorineImpl implements IChlorine {

    private final Probability probability;

    public ChlorineImpl(final Probability p) {
        probability = p;
    }

    /**
     * convert one gene pool into another
     */
    @Override
    public IGenePool evolve(final IGenePool pool) {
        final IGenePool newPool = pool;// new GenePool(eval);

        final Iterator<GeneSequence> iter = pool.iterator();
        while (iter.hasNext()) {
            final GeneSequence seqOne = iter.next();

            // if we have an odd number in our gene pool then we may need to terminate early
            if (!iter.hasNext()) {
                if (probability.nextMutate()) {
                    final GeneSequence newSeq = mutate(seqOne);
                    newPool.populate(newSeq);
                } else {
                    newPool.populate(seqOne);
                }
                continue;
            }

            final GeneSequence seqTwo = iter.next();

            // potentially cross the two sequences
            final Offspring offspring = getOffspring(seqOne, seqTwo);

            // potentially mutate the offspring
            newPool.populate(offspring.getOne(probability));
            newPool.populate(offspring.getTwo(probability));
        }

        // thin out the pool
        newPool.thinPopulation();

        return newPool;
    }

    /**
     * @param seqOne
     * @return
     */
    protected GeneSequence mutate(final GeneSequence seqOne) {
        return seqOne.mutate(probability);
    }

    /**
     * @param seqOne
     * @param seqTwo
     * @return
     */
    protected Offspring getOffspring(final GeneSequence seqOne, final GeneSequence seqTwo) {
        final Offspring offspring;
        if (probability.nextCross()) {
            offspring = seqOne.cross(probability, seqTwo);
        } else {
            offspring = new Offspring(seqOne, seqTwo);
        }
        return offspring;
    }
}