package com.porpoise.ga.countdown;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.porpoise.ga.Result;

@RunWith(Parameterized.class)
public class CountdownTest
{
    private final Integer[] EMPTY = new Integer[0];
    private final int       solution;
    private final Integer[] numbers;

    public CountdownTest(final int soln, final Integer a, final Integer b, final Integer c, final Integer d)
    {
        this.solution = soln;
        this.numbers = new Integer[] { a, b, c, d };
    }

    @Parameters
    public static Collection<Object[]> getTestArgs()
    {
        return Arrays.asList(//
                new Object[] { 13, 5, 3, 2, 1 },//        
                new Object[] { 24, 16, 8, 7, 9 }//        
                );
    }

    @Test
    public void test_countdown()
    {
        final Result result = Countdown.solve(this.solution, this.numbers);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getSolution());
        Assert.assertEquals(Integer.valueOf(this.solution), FormulaDecoder.getValue(result.getSolution()));
        Assert.assertTrue(FormulaDecoder.isValid(result.getSolution()));
        System.out.println(result);
    }
}
