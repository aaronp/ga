package com.porpoise.ga.countdown;

import com.google.common.base.Joiner;
import com.porpoise.ga.Probability;
import com.porpoise.ga.Result;

/**
 * runner class for the countdown application
 */
public class Main
{

    @SuppressWarnings("boxing")
    public static void main(final String[] args)
    {
        Probability.init(Probability.DEFAULT_CROSSOVER, 0.1F);

        final int target = 4;
        final Integer[] numbers = { 7, 8, 2, 32, 10, 14, 9, 67 };
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
        if (result == null)
        {
            System.out.println("no solution found");
        }
        else
        {
            System.out.println(result);
            System.out.println();
            System.out.println("Workings:");
            System.out.println(FormulaDecoder.toString(result.getSolution()));

        }
    }

}
