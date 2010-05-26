package com.porpoise.ga;

public enum TestGenePools {

    ;

    public static IGenePool alphaNumeric(final IGeneEvaluation criteria, final int size) {
        final GeneSequencer seq = TestSequencers.alphaNumeric();
        return seq.newPool(criteria, size);
    }// uninstantiable
}
