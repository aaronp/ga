package com.porpoise.ga;

import junit.framework.Assert;

import org.junit.Test;


public class GeneSequenceTest {

    @Test
    public void test() {
        final GeneSequencer sequencer = TestSequencers.alphaNumeric();

        //
        // use our sequencer to create two sequences
        //
        final GeneSequence seq1 = sequencer.create();
        final GeneSequence seq2 = sequencer.create();
        Assert.assertFalse(seq1.equals(seq2));

        //
        // call the method under test - prove the two can "breed"
        // cross them after the second gene
        //
        final Offspring offspring = seq1.cross(seq2, 2);

        System.out.println(String.format("%s X %s => %s", seq1, seq2, offspring));

        Assert.assertEquals(seq1.getGene(0), offspring.getOneGene(0));
        Assert.assertEquals(seq1.getGene(1), offspring.getOneGene(1));
        Assert.assertEquals(seq1.getGene(2), offspring.getTwoGene(2));
        Assert.assertEquals(seq1.getGene(3), offspring.getTwoGene(3));

        Assert.assertEquals(seq2.getGene(0), offspring.getTwoGene(0));
        Assert.assertEquals(seq2.getGene(1), offspring.getTwoGene(1));
        Assert.assertEquals(seq2.getGene(2), offspring.getOneGene(2));
        Assert.assertEquals(seq2.getGene(3), offspring.getOneGene(3));

    }
}
