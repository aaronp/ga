package com.porpoise.ga;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests for the {@link GeneSequence} class
 */
public class GeneSequenceTest
{

    // important that we have two types with overlapping values (but they are different types),
    // and that their values are bound to repeat within a sequence ('cause they appear 3 times per sequence, but only
    // have 2 values to choose from)
    @SuppressWarnings("boxing")
    final IGenotype<Integer> type1     = Genotype.of(1, 2);
    @SuppressWarnings("boxing")
    final IGenotype<Integer> type2     = Genotype.of(2, 3);
    final IGenotype<String>  type3     = Genotype.of("a", "b", "c");

    // create a sequencer which repeats - three of each type
    final GeneSequencer      sequencer = new GeneSequencer(//
                                               this.type1, this.type2, this.type3, //
                                               this.type1, this.type2, this.type3,// 
                                               this.type1, this.type2, this.type3//
                                       );

    /**
     * tests for {@link GeneSequence#getGenesByValue(Object)}
     */
    @Test
    public void test_getGenesByValue()
    {
        final GeneSequence seq1 = this.sequencer.create();
        final IGene<?> firstGene = seq1.getGene(0);
        final Object value = firstGene.getValue();
        final Collection<IGene<?>> genes = seq1.getGenesByValue(value);
        Assert.assertTrue(genes.contains(firstGene));
        final IGenotype<?> type = firstGene.getType();
        assertTypeAndValue(genes, value, type);
    }

    /**
     * tests for {@link GeneSequence#getGenesByType(IGenotype)}
     */
    @Test
    public void test_getGenesByType()
    {
        final GeneSequence seq1 = this.sequencer.create();
        final IGene<?> firstGene = seq1.getGene(1);
        final IGenotype<?> type = firstGene.getType();
        final Collection<IGene<?>> genes = seq1.getGenesByType(type);
        Assert.assertTrue(genes.contains(firstGene));
        for (final IGene<?> g : genes)
        {
            Assert.assertEquals(type, g.getType());
        }
    }

    /**
     * Test for the {@link GeneSequence#diff(GeneSequence)} method
     */
    @Test
    public void test_diff()
    {
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
    public void test_getGenesByTypeAndValue()
    {
        final GeneSequence seq1 = this.sequencer.create();
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
    private void assertTypeAndValue(final Collection<IGene<?>> genes, final Object value, final IGenotype<?> type)
    {
        for (final IGene<?> g : genes)
        {
            Assert.assertEquals(value, g.getValue());
            Assert.assertEquals(type, g.getType());
        }
    }

    /**
     * test for {@link GeneSequence#swapUniqueRecursive(GeneSequence, GeneSequence, int)}
     */
    @SuppressWarnings("boxing")
    @Test
    public void test_swapUniqueRecursive()
    {

        //
        // create two sequences (we can be sure about the number order):
        //
        // 1-a-3-2-5
        // 2-b-1-3-4
        //
        final IGenotype<Integer> numbers = Genotype.withFixedOrder(//
                1, 3, 2, 5,//
                2, 1, 3, 4//
                );
        final GeneSequencer seqncr1 = new GeneSequencer(//
                numbers,// 
                Genotype.of("a", "b", "c"),//
                numbers,// 
                numbers,// 
                numbers// 
        );

        // assert the expected number order:
        // 1-a-3-2-5
        final GeneSequence seq1 = seqncr1.create();
        Assert.assertEquals(1, seq1.getGeneIntValue(0));
        Assert.assertEquals(3, seq1.getGeneIntValue(2));
        Assert.assertEquals(2, seq1.getGeneIntValue(3));
        Assert.assertEquals(5, seq1.getGeneIntValue(4));

        // 2-b-1-3-4
        final GeneSequence seq2 = seqncr1.create();
        Assert.assertEquals(2, seq2.getGeneIntValue(0));
        Assert.assertEquals(1, seq2.getGeneIntValue(2));
        Assert.assertEquals(3, seq2.getGeneIntValue(3));
        Assert.assertEquals(4, seq2.getGeneIntValue(4));

        //
        // now swap the genes at index 2
        //
        // 1-a-3-2-5
        // 2-b-1-3-4
        //
        // should become
        //
        // 2-a-1-3-5
        // 1-b-3-2-4
        //
        final int pos = 2;
        // call the method under test
        final Offspring swapped = GeneSequence.swapUniqueRecursive(seq1, seq2, pos);

        //
        // assert the expected result
        //
        final GeneSequence offspringOne = swapped.getOffspringOne();
        Assert.assertEquals(2, offspringOne.getGeneIntValue(0));
        Assert.assertEquals(seq1.getGeneValue(1), offspringOne.getGeneValue(1));
        Assert.assertEquals(1, offspringOne.getGeneIntValue(2));
        Assert.assertEquals(3, offspringOne.getGeneIntValue(3));
        Assert.assertEquals(5, offspringOne.getGeneIntValue(4));

        final GeneSequence offspringTwo = swapped.getOffspringTwo();
        Assert.assertEquals(1, offspringTwo.getGeneIntValue(0));
        Assert.assertEquals(seq2.getGeneValue(1), offspringTwo.getGeneValue(1));
        Assert.assertEquals(3, offspringTwo.getGeneIntValue(2));
        Assert.assertEquals(2, offspringTwo.getGeneIntValue(3));
        Assert.assertEquals(4, offspringTwo.getGeneIntValue(4));
    }

    /**
     * Test for the {@link GeneSequence#cross(int, GeneSequence)} method
     */
    @Test
    public void test_cross()
    {
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
