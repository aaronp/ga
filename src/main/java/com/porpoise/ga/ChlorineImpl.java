package com.porpoise.ga;

import java.util.Iterator;

class ChlorineImpl implements IChlorine {

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
                    newPool.populate(seqOne.mutate(probability));
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
     * @param seqTwo
     * @return
     */
    private Offspring getOffspring(final GeneSequence seqOne, final GeneSequence seqTwo) {
        final Offspring offspring;
        if (probability.nextCross()) {
            offspring = seqOne.cross(probability, seqTwo);
        } else {
            offspring = new Offspring(seqOne, seqTwo);
        }
        return offspring;
    }
}
