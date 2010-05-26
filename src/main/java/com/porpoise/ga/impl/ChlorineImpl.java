package com.porpoise.ga.impl;

import java.util.Iterator;

import com.porpoise.ga.GeneSequence;
import com.porpoise.ga.IChlorine;
import com.porpoise.ga.IGeneEvaluation;
import com.porpoise.ga.IGenePool;
import com.porpoise.ga.Probability;

public class ChlorineImpl implements IChlorine {

    private final IGeneEvaluation eval;
    private final Probability     probability;

    public ChlorineImpl(final IGeneEvaluation evalFormula, final Probability p) {
        eval = evalFormula;
        probability = p;
    }

    /**
     * convert one gene pool into another
     */
    @Override
    public IGenePool evolve(final IGenePool pool) {
        final GenePool newPool = new GenePool(eval);

        final Iterator<GeneSequence> iter = pool.iterator();
        while (iter.hasNext()) {
            final GeneSequence seqOne = iter.next();

            // if we have an odd number in our gene pool then we may need to terminate early
            if (!iter.hasNext()) {
                if (probability.nextMutate()) {
                    newPool.populate(seqOne.mutate());
                } else {
                    newPool.populate(seqOne);
                }
                continue;
            }

            final GeneSequence seqTwo = iter.next();

            // potentially cross the two sequences
            final Offspring offspring = getOffspring(seqOne, seqTwo);

            // potentially mutate the offspring
            newPool.populate(offspring.getOne(probability.nextMutate()));
            newPool.populate(offspring.getTwo(probability.nextMutate()));
        }
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
            offspring = seqTwo.cross(seqTwo);
        } else {
            offspring = new Offspring(seqOne, seqTwo);
        }
        return offspring;
    }
}
