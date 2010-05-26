package com.porpoise.ga.countdown;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;

public class CountdownEvaluationTest {

    @Test
    public void test_score() {
        final CountdownEvaluation eval = new CountdownEvaluation(100);
        assertEquals(0, eval.score(0));
        assertEquals(0.5F, eval.score(50));
        assertEquals(0.75F, eval.score(75));
        assertEquals(1F, eval.score(100));
        assertEquals(.75F, eval.score(125));
        assertEquals(0.5F, eval.score(-50));
    }

    private void assertEquals(final float f, final float value) {
        final boolean equals = new BigDecimal(f).compareTo(new BigDecimal(value)) == 0;
        Assert.assertTrue(String.format("%s != %s", f, value), equals);

    }
}
