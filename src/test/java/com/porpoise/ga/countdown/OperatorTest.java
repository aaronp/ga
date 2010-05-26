package com.porpoise.ga.countdown;

import org.junit.Assert;
import org.junit.Test;

public class OperatorTest {
    @Test
    public void test_applyDivideCanApply() {
        Assert.assertTrue(Operator.DIVIDE.canApply(12, 4));
        Assert.assertTrue(Operator.DIVIDE.canApply(12, 1));
    }

    @Test
    public void test_applyDivideCantApply() {
        Assert.assertFalse(Operator.DIVIDE.canApply(12, 5));
        Assert.assertFalse(Operator.DIVIDE.canApply(12, 0));
    }

}
