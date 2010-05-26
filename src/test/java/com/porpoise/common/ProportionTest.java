package com.porpoise.common;

import org.junit.Assert;
import org.junit.Test;

public class ProportionTest {

    @Test
    public void test_builder() {
        // 80% chance of the top 20%,
        // 15% chance of the next 70%,
        // leaving 5% chance of the final 10%
        final Proportion prop = Proportion.with(0.20, .80).and(.70, .15).build();
        Assert.assertEquals(
                "20.00% size @ 80.00% likelihood, 70.00% size @ 15.00% likelihood, 10.00% size @ 5.00% likelihood",
                prop.toString(", "));
    }

    /**
     * test for {@link Proportion#nextFrom(int)}
     */
    @Test
    public void test_nextFrom() {
        final Proportion prop = Proportion.with(20, 80).and(70, 15).build();

        final int size = 1000;

        int topTwentyCount = 0;
        int middleSeventyCount = 0;
        int lastTenCount = 0;

        final int sampleSize = 100000;
        for (int i = 0; i < sampleSize; i++) {
            final int next = prop.nextFrom(size);
            if (next <= 200) {
                topTwentyCount++;
            } else if (next <= 900) {
                middleSeventyCount++;
            } else {
                lastTenCount++;
            }
        }

        // we should have, on average, sums which represent the proportion

        // assert each same range was within +/- 5% of what we suggested

        // == near 80% ==
        final double actualTop = (double) topTwentyCount / sampleSize;
        Assert.assertTrue(actualTop >= 0.75);
        Assert.assertTrue(actualTop <= 0.85);

        // == near 15% ==
        final double actualMiddle = (double) middleSeventyCount / sampleSize;
        Assert.assertTrue(actualMiddle >= 0.10);
        Assert.assertTrue(actualMiddle <= 0.20);

        // == near 5% ==
        final double actualBottom = (double) lastTenCount / sampleSize;
        Assert.assertTrue(actualBottom >= 0.0);
        Assert.assertTrue(actualBottom <= 0.10);
    }

    /**
     * test for {@link Proportion#nextFrom(int)}
     */
    @Test
    public void test_nextFromFixed() {
        final Proportion prop = Proportion.with(20, 80).and(70, 15).build();

        // given any 'random number (here we fix it at 0.7) between 0 and .8 (0 and 80%) should return a number in the
        // top 20% of the range
        final int range = 100;
        for (double i = 0.00; i < 0.80; i += 0.01) {
            final int nextFrom = prop.nextFrom(i, range);
            Assert.assertTrue(
                    "The top 80% of 'random' numbers should result in a number chosen from the top 20% of the range:"
                            + nextFrom, nextFrom >= 0 && nextFrom <= 20);
        }

        for (double i = 0.81; i < 0.95; i += 0.01) {
            final int nextFrom = prop.nextFrom(i, range);
            Assert.assertTrue(
                    "between 80% and 95% of 'random' numbers should result in a number chosen from between 20% and 90% of the range:"
                            + nextFrom, nextFrom >= 20 && nextFrom <= 90);
        }

        for (double i = 0.96; i <= 1.00; i += 0.01) {
            final int nextFrom = prop.nextFrom(i, range);
            Assert.assertTrue(
                    "between 95% and 100% of 'random' numbers should result in a number chosen from between 90% and 100% of the range:"
                            + nextFrom, nextFrom >= 90 && nextFrom <= range);
        }

    }
}
