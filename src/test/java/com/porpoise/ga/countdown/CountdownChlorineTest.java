package com.porpoise.ga.countdown;

import org.junit.Assert;
import org.junit.Test;

import com.porpoise.ga.GeneSequence;
import com.porpoise.ga.Probability;

public class CountdownChlorineTest
{

    @Test
    public void test_doMutate()
    {
        final Probability config = Probability.alwaysCrossAlwaysMutate();
        final CountdownChlorine chlorine = new CountdownChlorine(config);
        final GeneSequence original = GeneSequence.of(1, 2, 3, 4);
        GeneSequence seq = original;

        for (int i = 0; i < 100; i++)
        {
            seq = assertMutationProducesUniqueResults(chlorine, seq);
        }
    }

    /**
     * @param chlorine
     * @param original
     * @return
     */
    private GeneSequence assertMutationProducesUniqueResults(final CountdownChlorine chlorine, final GeneSequence original)
    {
        // call the method under test
        final GeneSequence mutated = chlorine.doMutate(original);

        // assert the sequences are both valid (have unique numbers)
        Assert.assertTrue(FormulaDecoder.isValid(original));
        Assert.assertTrue(FormulaDecoder.isValid(mutated));

        // best double check our equals works...
        Assert.assertEquals(original, original);
        Assert.assertEquals(mutated, mutated);

        // ... before we assert they are different
        Assert.assertFalse(original.equals(mutated));

        // and assert there are two differences (the swapped numbers)
        Assert.assertEquals(Integer.valueOf(2), Integer.valueOf(original.diff(mutated).size()));

        return mutated;
    }
}
