package com.porpoise.ga;

import junit.framework.Assert;

import org.junit.Test;

import com.porpoise.ga.impl.Genotype;
import com.porpoise.ga.impl.Offspring;

public class GeneSequenceTest {

    @Test
    public void test() {
        //
        // create two genotypes (one of strings, one of letters)
        //
        final Genotype<Integer> genotype1 = Genotype.of(1, 2, 3, 4, 5, 6);
        final Genotype<String> genotype2 = Genotype.of("a", "b", "c");

        //
        // our gene sequences will have, say, two numbers, then a letter, then a number:
        //
        final GeneSequencer sequencer = new GeneSequencer(genotype1, genotype1, genotype2, genotype1);

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
