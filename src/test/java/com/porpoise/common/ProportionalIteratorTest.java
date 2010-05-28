package com.porpoise.common;

import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ProportionalIteratorTest {

    @Test
    public void test() {
        final Proportion p = Proportion.with(70, 10).and(10, 60).build();
        final int size = 1000;
        final List<Integer> values = Lists.newArrayListWithExpectedSize(size);
        for (int i = 0; i < size; i++) {
            values.add(Integer.valueOf(i));
        }
        final Set<Integer> uniqueElms = Sets.newHashSet();
        final ProportionalIterator<Integer> iter = ProportionalIterator.<Integer> ascending(p, values);
        while (iter.hasNext()) {
            final Integer elm = iter.next();
            System.out.println(elm);
            final boolean added = uniqueElms.add(elm);
            Assert.assertTrue(added);
        }
        Assert.assertEquals(Integer.valueOf(size), Integer.valueOf(uniqueElms.size()));
    }
}
