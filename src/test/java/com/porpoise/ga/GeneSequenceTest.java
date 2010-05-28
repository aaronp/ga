package com.porpoise.ga;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests for the {@link GeneSequence} class
 */
public class GeneSequenceTest {

    // important that we have two types with overlapping values (but they are different types),
    // and that their values are bound to repeat within a sequence ('cause they appear 3 times per sequence, but only
    // have 2 values to choose from)
    final Genotype<Integer> type1     = Genotype.of(1, 2);
    final Genotype<Integer> type2     = Genotype.of(2, 3);
    final Genotype<String>  type3     = Genotype.of("a", "b", "c");

    // create a sequencer which repeats - three of each type
    final GeneSequencer     sequencer = new GeneSequencer(//
                                              type1, type2, type3, //
                                              type1, type2, type3,// 
                                              type1, type2, type3//
                                      );

    /**
     * tests for {@link GeneSequence#getGenesByValue(Object)}
     */
    @Test
    public void test_getGenesByValue() {
        final GeneSequence seq1 = sequencer.create();
        final IGene<?> firstGene = seq1.getGene(0);
        final Object value = firstGene.getValue();
        final Collection<IGene<?>> genes = seq1.getGenesByValue(value);
        Assert.assertTrue(genes.contains(firstGene));
        final IGenotype type = firstGene.getType();
        assertTypeAndValue(genes, value, type);
    }

    /**
     * tests for {@link GeneSequence#getGenesByType(IGenotype)}
     */
    @Test
    public void test_getGenesByType() {
        final GeneSequence seq1 = sequencer.create();
        final IGene<?> firstGene = seq1.getGene(1);
        final IGenotype type = firstGene.getType();
        final Collection<IGene<?>> genes = seq1.getGenesByType(type);
        Assert.assertTrue(genes.contains(firstGene));
        for (final IGene<?> g : genes) {
            Assert.assertEquals(type, g.getType());
        }
    }

    /**
     * Test for the {@link GeneSequence#diff(GeneSequence)} method
     */
    @Test
    public void test_diff() {
        final GeneSequencer sqncr = TestSequencers.alphaNumeric();

        //
        // use our sequencer to create two sequences
        //
        final GeneSequence seq1 = sqncr.create();
        final GeneSequence seq2 = seq1.mutate();

        final Collection<Pair<Integer, IGene<?>>> differences = seq1.diff(seq2);
        Assert.assertEquals(1, differences.size());
        System.out.println(seq1);
        System.out.println(seq2);
        System.out.println(differences);

    }

    /**
     * test for {@link GeneSequence#getGeneOfTypeAndValue(IGenotype, Object)}
     */
    @Test
    public void test_getGenesByTypeAndValue() {
        final GeneSequence seq1 = sequencer.create();
        final IGene<?> firstGene = seq1.getGene(0);
        final Object value = firstGene.getValue();
        final IGenotype<?> type = firstGene.getType();
        final Collection<IGene<?>> genes = seq1.getGenesByTypeAndValue(type, value);
        Assert.assertTrue(genes.contains(firstGene));
        assertTypeAndValue(genes, value, type);
    }

    /**
     * @param genes
     * @param value
     * @param type
     */
    private void assertTypeAndValue(final Collection<IGene<?>> genes, final Object value, final IGenotype type) {
        for (final IGene<?> g : genes) {
            Assert.assertEquals(value, g.getValue());
            Assert.assertEquals(type, g.getType());
        }
    }

    @Test
    public void test_swap() {
        final GeneSequence seq1 = sequencer.create();
        final GeneSequence seq2 = sequencer.create();
        final Offspring swapped = seq1.crossBySwap(1, seq2);
        System.out.println(seq1);
        System.out.println(seq2);
        System.out.println(swapped);
    }

    @Test
    public void test_swapUnique() {

        final GeneSequence seq1 = new GeneSequence();
        int index = 0;
        seq1.addGene(new GeneImpl<Integer>(numbers, index++, Integer.valueOf(1)));
        final GeneSequence seq2 = new GeneSequence();

        //
        // between the two sequences, there will be one number shared. find that index
        //
        int pos = -1;
        final Collection<IGene<?>> g1 = seq1.getGenesByType(t1);
        for (final IGene<?> g : g1) {
            final IGene<?> gene = seq2.getGeneOfTypeAndValue(t1, g.getValue());
            if (gene != null) {
                pos = g.getPosition();
                break;
            }
        }
        if (pos == -1) {
            return;
        }
        System.out.println("********swap***********");
        System.out.println(seq1);
        System.out.println(seq2);
        System.out.println("swapping at pos " + pos);
        final Offspring swapped = seq1.crossBySwapUniqueValuesInType(pos, seq2);
        System.out.println(swapped);
        System.out.println("********swap***********");
    }

    /**
     * Test for the {@link GeneSequence#cross(int, GeneSequence)} method
     */
    @Test
    public void test_cross() {
        final GeneSequencer sqncr = TestSequencers.alphaNumeric();

        //
        // use our sequencer to create two sequences
        //
        final GeneSequence seq1 = sqncr.create();
        final GeneSequence seq2 = sqncr.create();
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
