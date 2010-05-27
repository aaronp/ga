package com.porpoise.ga.countdown;

import com.google.common.base.Joiner;
import com.porpoise.ga.ChlorineImpl;
import com.porpoise.ga.GeneImpl;
import com.porpoise.ga.GeneSequence;
import com.porpoise.ga.GeneSequencer;
import com.porpoise.ga.GeneticAlgorithm;
import com.porpoise.ga.Genotype;
import com.porpoise.ga.IChlorine;
import com.porpoise.ga.IGene;
import com.porpoise.ga.IGeneEvaluation;
import com.porpoise.ga.IGenePool;
import com.porpoise.ga.Pair;
import com.porpoise.ga.Probability;
import com.porpoise.ga.Result;

public class Main {

    @SuppressWarnings("boxing")
    public static void main(final String[] args) {
        // final int target = 42;
        // final Integer[] numbers = { 7, 8, 2, 1 };
        final int target = 96;
        final Integer[] numbers = { 7, 8, 2, 3 };

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

        //
        // the probability object is really a configuration or probabilities (cross rates, cull rates, mutations, ...).
        // here we use the default configuration
        //
        final Probability config = Probability.init(Probability.DEFAULT_CROSSOVER, 0.1F);

        // all our algorithm really needs is an initial gene pool,
        // as the gene pool knows how to maintain (cull) itself
        final IGenePool original;
        {
            //
            // We'll use a GeneSequencer to create our gene pool.
            // 
            // To create a sequencer, we will have to provide it with a IGeneEvaluation instance
            // which will be used to 'judge' which solutions are better than others
            //
            final IGeneEvaluation<Integer> eval = new CountdownEvaluation(target);

            //
            // create a gene sequencer which will be able to build
            // [num op]+ num sequences, such as 1 + 2 / 3 * 4 - 5
            //
            final int someInitialPoolSize = numbers.length * Operator.values().length;
            final GeneSequencer seq = createSequencer(numbers);
            // ... and use that sequence to create an initial gene pool of a given size
            original = seq.newPool(eval, someInitialPoolSize);
        }

        //
        // all is left to do is create a genetic algoritm instance and ask
        // it to solve our problem
        //
        final GeneticAlgorithm ga = newAlgorithm(config);
        final Result result = ga.solve(original);
        return result;
    }

    /**
     * @param config
     * @return a new genetic algorithm based on the given configuration
     */
    private static GeneticAlgorithm newAlgorithm(final Probability config) {

        // TODO - use the other one we created elsewhere
        final Genotype<Integer> numberType = Genotype.of(Integer.valueOf(1));

        // The algorithm uses an IChlorine instance which is responsible
        // for 'evolving' the gene pool through each generation
        final IChlorine chlorine = new ChlorineImpl(config) {
            @Override
            protected GeneSequence mutate(final GeneSequence seqOne) {
                if (true) {
                    return super.mutate(seqOne);
                }
                final GeneSequence mutation = super.mutate(seqOne);

                final Pair<Integer, IGene<?>> onlyDiff = seqOne.onlyDiff(mutation);
                final IGene<?> newGene = onlyDiff.getSecond();

                // if we've mutated a number, then swap it with the same number elsewhere
                if (newGene.getValue() instanceof Integer) {
                    final int mutatedValue = ((Integer) newGene.getValue()).intValue();
                    final int mutatedValueIndex = onlyDiff.getFirst().intValue();
                    for (int i = 0; i < mutation.size(); i += 2) {
                        final int value = mutation.getGeneIntValue(i);
                        if (value == mutatedValue && i != mutatedValueIndex) {
                            final int swapValue = seqOne.getGeneIntValue(mutatedValueIndex);
                            final IGene<?> swap = new GeneImpl<Integer>(numberType, i, Integer.valueOf(swapValue));
                            mutation.setGene(i, swap);
                            break;
                        }
                    }
                }
                return mutation;
            }
        };

        // new GeneticAlgorithm(config);
        return new GeneticAlgorithm(chlorine);
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
