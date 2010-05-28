package com.porpoise.ga.countdown;

import com.porpoise.ga.GeneSequence;
import com.porpoise.ga.GeneSequencer;
import com.porpoise.ga.GeneticAlgorithm;
import com.porpoise.ga.Genotype;
import com.porpoise.ga.IChlorine;
import com.porpoise.ga.IGeneEvaluation;
import com.porpoise.ga.IGenePool;
import com.porpoise.ga.IGenotype;
import com.porpoise.ga.Probability;
import com.porpoise.ga.Result;

/**
 * Countdown is a functional utility class which, given a collection of numbers and a target (goal), It returns a {@link Result} containing
 * the way those numbers can be combined to reach the goal.
 */
public enum Countdown
{
    ;//

    public static Result solve(final int target, final Integer[] numbers)
    {

        //
        // the probability object is really a configuration or probabilities (cross rates, cull rates, mutations, ...).
        // here we use the default configuration
        //
        final Probability config = Probability.getInstance();

        return solve(config, target, numbers);
    }

    public static Result solve(final Probability config, final int target, final Integer... numbers)
    {
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

            for (final GeneSequence s : original)
            {
                assert FormulaDecoder.isValid(s) : "invalid original sequence:" + s;
            }

        }

        //
        // all is left to do is create a genetic algorithm instance and ask
        // it to solve our problem
        //
        final GeneticAlgorithm ga = newAlgorithm(config);
        final Result result = ga.solve(original, 50);
        return result;
    }

    /**
     * @param config
     * @return a new genetic algorithm based on the given configuration
     */
    private static GeneticAlgorithm newAlgorithm(final Probability config)
    {
        // The algorithm uses an IChlorine instance which is responsible
        // for 'evolving' the gene pool through each generation
        final IChlorine chlorine = new CountdownChlorine(config);

        // new GeneticAlgorithm(config);
        return new GeneticAlgorithm(chlorine);
    }

    /**
     * @param numbers
     * @return
     */
    private static GeneSequencer createSequencer(final Integer[] numbers)
    {
        final IGenotype<Integer> numberType = Genotype.of(numbers);
        final IGenotype<Operator> operatorType = Genotype.of(Operator.values());

        final GeneSequencer seq = new GeneSequencer(numberType);
        final int count = numbers.length - 1;
        for (int i = 0; i < count; i++)
        {
            seq.addGenotype(operatorType);
            seq.addGenotype(numberType);
        }
        return seq;
    }

}
