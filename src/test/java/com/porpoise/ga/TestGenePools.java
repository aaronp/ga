package com.porpoise.ga;

public enum TestGenePools
{

    ; // uninstantiable

    @SuppressWarnings("unchecked")
    public static GenePool<Integer> alphaNumeric(final IGeneEvaluation<Integer> criteria, final int size)
    {
        final GeneSequencer seq = TestSequencers.alphaNumeric();
        return (GenePool<Integer>) seq.newPool(criteria, size);
    }
}
