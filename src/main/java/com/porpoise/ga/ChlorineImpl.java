package com.porpoise.ga;

/**
 * Implementation of {@link IChlorine} This class is able to take a n {@link IGenePool} and transform ('evolve') it into another
 */
public final class ChlorineImpl extends AbstractChlorine
{

    /**
     * @param p
     */
    public ChlorineImpl(final Probability p)
    {
        super(p);
    }

    /**
     * @param seqOne
     * @return
     */
    @Override
    protected GeneSequence doMutate(final GeneSequence seqOne)
    {
        return seqOne.mutate(getProbability());
    }

    @Override
    protected Offspring doCross(final GeneSequence seqOne, final GeneSequence seqTwo)
    {
        return seqOne.cross(getProbability(), seqTwo);
    }
}
