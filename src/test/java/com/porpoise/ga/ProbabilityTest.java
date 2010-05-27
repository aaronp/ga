package com.porpoise.ga;

import org.junit.Assert;
import org.junit.Test;

public class ProbabilityTest {

    /**
     * Test a probability of 1.0 will *always* return true
     */
    @Test
    public void test_always() {
        final Probability p = new Probability(Probability.ALWAYS, Probability.ALWAYS);
        for (int i = 100000; --i >= 0;) {
            Assert.assertTrue(p.nextCross());
            Assert.assertTrue(p.nextMutate());
        }
    }

    /**
     * Test a probability of 1.0 will *never* return true
     */
    @Test
    public void test_never() {
        final Probability p = new Probability(Probability.NEVER, Probability.NEVER);
        for (int i = 100000; --i >= 0;) {
            Assert.assertFalse(p.nextCross());
            Assert.assertFalse(p.nextMutate());
        }
    }

    @Test
    public void test_toString() {
        Assert.assertEquals("cross change=50.00%, mutatbility change=35.00%", new Probability(0.5F, 0.35F).toString());
    }

    /**
     * test for {@link Probability#nextNumberToThin(float, float, int)}
     */
    @Test
    public void test_nextNumberToThin() {
        final float min = .4F;
        final float max = .45F;
        final Probability p = Probability.neverCrossNeverMutate();
        for (int i = 0; i < 1000; i++) {
            // call method under test
            final int result = p.nextNumberToThin(min, max, 100);
            Assert.assertTrue("result should be >= 20: " + result, result >= 40);
            Assert.assertTrue("result should be <= 30: " + result, result <= 45);
        }
    }
}
