package com.porpoise.ga.impl;

import java.util.Iterator;
import java.util.Random;

import com.porpoise.ga.GeneSequence;
import com.porpoise.ga.IChlorine;
import com.porpoise.ga.IGeneEvaluation;
import com.porpoise.ga.IGenePool;

public class ChlorineImpl implements IChlorine {

    private final IGeneEvaluation eval;

    private final float           mutationProbability;
    private final float           crossProbability;
    private final Random          rand = new Random();

    public ChlorineImpl(final IGeneEvaluation eval) {
        this(eval, 0.0015F, 0.7F);
    }

    public ChlorineImpl(final IGeneEvaluation eval, final float mutation, final float cross) {
        this.eval = eval;
        mutationProbability = mutation;
        crossProbability = cross;
    }

    @Override
    public IGenePool evolve(final IGenePool pool) {
        final GenePool newPool = new GenePool(eval);

        final Iterator<GeneSequence> iter = pool.iterator();
        while (iter.hasNext()) {
            final GeneSequence seqOne = iter.next();

            final boolean cross = rand.nextFloat() < crossProbability;
            if (cross) {
                if (iter.hasNext()) {
                    final GeneSequence seqTwo = iter.next();
                    final GeneSequence offspring = seqOne.cross(seqTwo);

                    newPool.populate(offspring);
                }
            }

        }
        return newPool;
    }

}
