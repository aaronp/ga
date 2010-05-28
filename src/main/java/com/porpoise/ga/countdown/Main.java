package com.porpoise.ga.countdown;

import com.google.common.base.Joiner;
import com.porpoise.ga.Result;

/**
 * runner class for the countdown application
 */
public class Main
{

    @SuppressWarnings("boxing")
    public static void main(final String[] args)
    {
        final int target = 21;

        final Integer[] numbers = { 7, 8, 2, 3, 1 };
        run(target, numbers);
    }

    /**
     * @param target
     * @param numbers
     */
    @SuppressWarnings("boxing")
    public static void run(final int target, final Integer[] numbers)
    {
        final long start = System.currentTimeMillis();
        System.out.println(String.format("Looking for %d in %s:", target, Joiner.on(",").join(numbers)));

        final Result result = Countdown.solve(target, numbers);

        final long done = System.currentTimeMillis() - start;
        System.out.println(String.format("took %dms", done));
        System.out.println(result == null ? "no solution found" : result);
    }

}
