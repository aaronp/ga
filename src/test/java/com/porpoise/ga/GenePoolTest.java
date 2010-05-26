package com.porpoise.ga;

import org.junit.Assert;
import org.junit.Test;

public class GenePoolTest {

    @Test
    public void test_sortedIter() {
        final IGeneEvaluation<Integer> criteria = TestEvaluations.increasing();
        final IGenePool pool = TestGenePools.alphaNumeric(criteria, 10);

        IScore<Integer> last = Scores.valueOf(Integer.MAX_VALUE);
        for (final GeneSequence seq : pool) {
            Assert.assertTrue(String.format("scores should be in decreasing order: %f > %f", seq.getScore(criteria),
                    last), seq.getScore(criteria).compareTo(last) < 0);
            last = seq.getScore(criteria);
        }

    }
}
