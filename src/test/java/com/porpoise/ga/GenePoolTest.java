package com.porpoise.ga;

import org.junit.Assert;
import org.junit.Test;

public class GenePoolTest {

    @Test
    public void test_sortedIter() {
        final IGeneEvaluation criteria = TestEvaluations.increasing();
        final IGenePool pool = TestGenePools.alphaNumeric(criteria, 10);

        float last = Float.MAX_VALUE;
        for (final GeneSequence seq : pool) {
            Assert.assertTrue(String.format("scores should be in decreasing order: %f > %f", seq.getScore(criteria),
                    last), seq.getScore(criteria) < last);
            last = seq.getScore(criteria);
        }

    }
}
