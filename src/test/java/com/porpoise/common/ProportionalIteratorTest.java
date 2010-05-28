package com.porpoise.common;

import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * test for the {@link ProportionalIterator} class
 */
public class ProportionalIteratorTest
{

    /**
     * test a proportional iterator returns values from the ranges expected
     */
    @Test
    public void test_iteratorAscending()
    {
        // prepare some proportional representation
        final Proportion p = Proportion.with(70, 10).and(10, 60).build();

        // create a collection of increasing values to iterate over
        final int size = 1000;
        final List<Integer> values = Lists.newArrayListWithExpectedSize(size);
        for (int i = 0; i < size; i++)
        {
            values.add(Integer.valueOf(i));
        }

        // use a proportional iterator to iterate over the elements, asserting that all elements are
        // returned
        final Set<Integer> uniqueElms = Sets.newHashSet();
        final ProportionalIterator<Integer> iter = ProportionalIterator.<Integer> ascending(p, values);
        while (iter.hasNext())
        {
            final Integer elm = iter.next();
            final boolean added = uniqueElms.add(elm);
            Assert.assertTrue(added);
        }
        Assert.assertEquals(Integer.valueOf(size), Integer.valueOf(uniqueElms.size()));
    }
}
