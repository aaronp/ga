package com.porpoise.ga;

import java.util.SortedSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GenePoolTest
{

    private IGeneEvaluation<Integer> criteria;
    private GenePool<Integer>        pool;

    @Before
    public void setup()
    {
        this.criteria = TestEvaluations.increasing();
        this.pool = TestGenePools.alphaNumeric(this.criteria, 10);
    }

    /**
     */
    @Test
    public void test_getIndicesToRemove()
    {
        final SortedSet<Integer> indices = this.pool.getIndicesToRemove();
        Integer last = null;
        for (final Integer index : indices)
        {
            Assert.assertTrue("indices should be descending", last == null || last.intValue() > index.intValue());
            last = index;
        }
    }

    /**
     * test the best (lowest) scores appear first in the list
     */
    @Test
    public void test_sortedIter()
    {

        IScore<Integer> last = Scores.valueOf(Integer.MIN_VALUE);
        for (final GeneSequence seq : this.pool.cacheSequences())
        {
            final IScore<Integer> s1 = seq.getScore(this.criteria);
            Assert.assertTrue(String.format("scores should be in decreasing order: %s > %s", s1, last), s1.compareTo(last) > 0);
            last = s1;
        }
    }
}
