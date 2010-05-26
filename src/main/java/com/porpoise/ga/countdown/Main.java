package com.porpoise.ga.countdown;

import com.porpoise.ga.GeneSequencer;
import com.porpoise.ga.GeneticAlgorithm;
import com.porpoise.ga.Genotype;
import com.porpoise.ga.IGeneEvaluation;
import com.porpoise.ga.IGenePool;
import com.porpoise.ga.Result;

public class Main {

    @SuppressWarnings("boxing")
    public static void main(final String[] args) {
        final int target = 42;
        final Integer[] numbers = { 7, 8, 2, 1 };
        final IGeneEvaluation<Integer> eval = new CountdownEvaluation(target);

        final GeneSequencer seq = createSequencer(numbers);

        final IGenePool original = seq.newPool(eval, numbers.length * 4);
        final Result result = new GeneticAlgorithm<Integer>(eval).solve(original);
        System.out.println(result);
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
