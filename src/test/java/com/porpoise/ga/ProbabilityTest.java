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
}
