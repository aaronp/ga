package com.porpoise.ga;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests for the {@link GeneSequence} class
 */
public class GeneSequenceTest {

    @Test
    public void test_swap() {
        final GeneSequencer sequencer = TestSequencers.alphaNumeric(10);
        final GeneSequence a = sequencer.create();
        final GeneSequence b = sequencer.create();
        a.crossBySwap(Probability.getInstance(), b);

    }

    /**
     * Test for the {@link GeneSequence#diff(GeneSequence)} method
     */
    @Test
    public void test_diff() {
        final GeneSequencer sequencer = TestSequencers.alphaNumeric();

        //
        // use our sequencer to create two sequences
        //
        final GeneSequence seq1 = sequencer.create();
        final GeneSequence seq2 = seq1.mutate();

        final Collection<Pair<Integer, IGene<?>>> differences = seq1.diff(seq2);
        Assert.assertEquals(1, differences.size());
        System.out.println(seq1);
        System.out.println(seq2);
        System.out.println(differences);

    }

    /**
     * Test for the {@link GeneSequence#cross(int, GeneSequence)} method
     */
    @Test
    public void test_cross() {
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
        final Offspring offspring = seq1.cross(2, seq2);

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
