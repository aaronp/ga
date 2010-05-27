package com.porpoise.ga.countdown;

import com.google.common.base.Joiner;
import com.porpoise.ga.GeneSequencer;
import com.porpoise.ga.GeneticAlgorithm;
import com.porpoise.ga.Genotype;
import com.porpoise.ga.IGeneEvaluation;
import com.porpoise.ga.IGenePool;
import com.porpoise.ga.Result;

public class Main {

    @SuppressWarnings("boxing")
    public static void main(final String[] args) {
        // final int target = 42;
        // final Integer[] numbers = { 7, 8, 2, 1 };
        final int target = 592;
        final Integer[] numbers = { 7, 8, 2, 1, 9, 3 };

        doit(target, numbers);
    }

    /**
     * @param target
     * @param numbers
     */
    private static void doit(final int target, final Integer[] numbers) {
        final long start = System.currentTimeMillis();
        System.out.println(String.format("Looking for %d in %s:", target, Joiner.on(",").join(numbers)));
        final Result result = solve(target, numbers);
        final long done = System.currentTimeMillis() - start;
        System.out.println(String.format("took %dms", done));
        System.out.println(result);
    }

    /**
     * @param target
     * @param numbers
     * @return
     */
    private static Result solve(final int target, final Integer[] numbers) {
        final IGeneEvaluation<Integer> eval = new CountdownEvaluation(target);

        final GeneSequencer seq = createSequencer(numbers);

        final int poolSize = numbers.length * Operator.values().length;
        final IGenePool original = seq.newPool(eval, poolSize);
        final Result result = new GeneticAlgorithm<Integer>(eval).solve(original);
        return result;
    }

    /**
     * @param numbers
     * @return
     */
    private static GeneSequencer createSequencer(final Integer[] numbers) {
        final Genotype<Integer> numberType = Genotype.of(numbers);
        final Genotype<Operator> operatorType = Genotype.of(Operator.values());

        final GeneSequencer seq = new GeneSequencer(numberType);
        final int count = numbers.length - 1;
        for (int i = 0; i < count; i++) {
            seq.addGenotype(operatorType);
            seq.addGenotype(numberType);
        }
        return seq;
    }
}
