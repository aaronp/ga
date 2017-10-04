package com.porpoise.ga.countdown;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.porpoise.ga.Probability;
import com.porpoise.ga.Result;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * main entry point the countdown application
 */
public class Main {

    public static void main(final String[] args) {
        if (args.length < 1) {
            printUsage(System.out);
            return;
        } else if (args.length == 1) {
            Path inputFile = Paths.get(args[0]);
            try {
                byte[] bytes = Files.readAllBytes(inputFile);
                String commandLine = new String(bytes);
                String[] fileArgs = commandLine.split(" ", -1);

                runWithUserArgs(fileArgs);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            runWithUserArgs(args);
        }
    }

    public static void runWithUserArgs(String[] args) {

        // initialise the 'Probability' class. This will provide a shared instance which
        // may be used within the Countdown application. It serves as a configuration,
        // detailing how over genes should 'cross' or 'mutate', as well as methods for
        // retrieving random numbers
        Probability.init(Probability.DEFAULT_CROSSOVER, 0.1F);

        final int target = Integer.parseInt(args[0]);
        final Integer[] numbers = getNumbers(args);
        run(target, numbers);
    }

    private static void printUsage(final PrintStream out) {
        out.println("Usage:");
        out.println("Main <target number> [<constituent numbers>]+");
        out.println("or");
        out.println("Main <input file>");
        out.println("");
        out.println("e.g.");
        out.println("Main 13 5 2 3");
        out.println("or");
        out.println("Main figureThisOneOut.puzzle");
    }

    private static Integer[] getNumbers(final String[] args) {
        Integer BadInput = Integer.valueOf(Integer.MIN_VALUE);
        List<Integer> numberList = Lists.newArrayList(Lists.transform(Arrays.asList(args), new Function<String, Integer>() {
            @Override
            public Integer apply(final String arg0) {
                if (arg0.trim().isEmpty()) {
                    return BadInput;
                } else {
                    return Integer.valueOf(arg0);
                }
            }
        })).subList(1, args.length);

        // filter out bad (empty) values
        while(numberList.remove(BadInput));

        final Integer[] numbers = numberList.toArray(new Integer[0]);
        return numbers;
    }

    /**
     * @param target
     * @param numbers
     */
    @SuppressWarnings("boxing")
    public static void run(final int target, final Integer[] numbers) {
        final long start = System.currentTimeMillis();
        System.out.println(String.format("Looking for %d in %s:", target, Joiner.on(",").join(numbers)));

        final Result result = Countdown.solve(target, numbers);

        final long done = System.currentTimeMillis() - start;
        System.out.println(String.format("took %dms", done));
        if (result == null) {
            System.out.println("no solution found");
        } else {
            System.out.println(result);
            System.out.println();
            System.out.println("Workings:");
            System.out.println(FormulaDecoder.toString(result.getSolution()));

        }
    }

}
