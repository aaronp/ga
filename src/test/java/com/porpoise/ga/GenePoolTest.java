package com.porpoise.ga;

import java.util.SortedSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GenePoolTest {

    private IGeneEvaluation<Integer> criteria = TestEvaluations.increasing();
    private GenePool<Integer>        pool     = TestGenePools.alphaNumeric(criteria, 10);

    @Before
    public void setup() {
        criteria = TestEvaluations.increasing();
        pool = TestGenePools.alphaNumeric(criteria, 10);
    }

    /**
     */
    @Test
    public void test_getIndicesToRemove() {
        final SortedSet<Integer> indices = pool.getIndicesToRemove();
        Integer last = null;
        for (final Integer index : indices) {
            Assert.assertTrue("indices should be descending", last == null || last.intValue() > index.intValue());
            last = index;
        }
    }

    @Test
    public void test_sortedIter() {

        IScore<Integer> last = Scores.valueOf(Integer.MIN_VALUE);
        for (final GeneSequence seq : pool) {
            final IScore<Integer> s1 = seq.getScore(criteria);
            System.out.println(s1);
            Assert.assertTrue(String.format("scores should be in decreasing order: %s > %s", s1, last), s1
                    .compareTo(last) > 0);
            last = s1;
        }

    }
}
