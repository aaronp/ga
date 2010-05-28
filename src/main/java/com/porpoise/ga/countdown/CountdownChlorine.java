package com.porpoise.ga.countdown;

import com.porpoise.ga.AbstractChlorine;
import com.porpoise.ga.GeneImpl;
import com.porpoise.ga.GeneSequence;
import com.porpoise.ga.IGene;
import com.porpoise.ga.IGenotype;
import com.porpoise.ga.Offspring;
import com.porpoise.ga.Pair;
import com.porpoise.ga.Probability;

class CountdownChlorine extends AbstractChlorine {

    public CountdownChlorine(final Probability p) {
        super(p);
    }

    /**
     * Mutate the gene sequence by changing one gene in the sequence.
     * 
     * If the gene we've changed is a number, then we may have just created an 'invalid' gene sequence since the
     * mutation will now contain duplicate numbers (which isn't allowed by our problem space).
     */
    @Override
    protected GeneSequence doMutate(final GeneSequence original) {

        // create the mutation
        final GeneSequence mutation = original.mutate(getProbability());

        return swapIfNumberNotUnique(original, mutation);
    }

    /**
     * @param original
     * @param mutation
     * @return
     */
    final GeneSequence swapIfNumberNotUnique(final GeneSequence original, final GeneSequence mutation) {
        // there will be only one difference between the original and the mutation
        final Pair<Integer, IGene<?>> newGeneByIndex = original.onlyDiff(mutation);
        final IGene<?> mutatedGene = newGeneByIndex.getSecond();
        assert newGeneByIndex.getFirst().intValue() == mutatedGene.getPosition();

        // check to see if we've mutated a number. If so, we need to swap
        final IGene<Integer> firstNumber = original.getGene(0);
        final IGenotype<Integer> numberType = firstNumber.getType();
        if (mutatedGene.getType().equals(numberType)) {

            final Integer mutatedValue = (Integer) mutatedGene.getValue();
            final int mutatedValueIndex = newGeneByIndex.getFirst().intValue();

            // find the other gene with the same flipped value and swap it for the mutated gene's old value
            for (final IGene<?> genesByValue : mutation.getGenesByValue(mutatedValue)) {
                if (genesByValue.getPosition() != mutatedValueIndex) {

                    final int previousValue = original.getGeneIntValue(mutatedValueIndex);
                    final IGene<?> swap = new GeneImpl<Integer>(numberType, genesByValue.getPosition(), Integer
                            .valueOf(previousValue));
                    mutation.setGene(genesByValue.getPosition(), swap);
                    break;
                }
            }
        }
        return mutation;
    }

    @Override
    protected Offspring doCross(final GeneSequence seqOne, final GeneSequence seqTwo) {
        final Offspring offspring = seqOne.crossBySwapUniqueValuesByType(getProbability(), seqTwo);
        return offspring;
    }

}
