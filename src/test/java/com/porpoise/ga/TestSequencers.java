package com.porpoise.ga;


public enum TestSequencers {
    ;// uninstantiable

    public static GeneSequencer alphaNumeric() {
        //
        // create two genotypes (one of strings, one of letters)
        //
        final Genotype<Integer> genotype1 = Genotype.of(1, 2, 3, 4, 5, 6);
        final Genotype<String> genotype2 = Genotype.of("a", "b", "c");

        //
        // our gene sequences will have, say, two numbers, then a letter, then a number:
        //
        final GeneSequencer sequencer = new GeneSequencer(genotype1, genotype1, genotype2, genotype1);

        return sequencer;
    }

}
