package com.porpoise.ga.countdown;

import java.util.Set;

import com.google.common.collect.Sets;
import com.porpoise.ga.GeneSequence;

/**
 * Class used to decode a {@link GeneSequence} into a formula and return the result
 */
public class FormulaDecoder
{

    /**
     * Convert the sequence into a total. Returns null if the equation is invalid (e.g. has a divide by zero)
     * 
     * @param seq
     *            the sequence to decode
     * @return the result of the gene sequence as interpreted as an equation
     */
    public static Integer getValue(final GeneSequence seq)
    {
        int result = seq.getGeneIntValue(0);

        for (int index = 1; index < seq.size(); index += 2)
        {
            final Operator op = seq.getGeneValue(index);
            final int value = seq.getGeneIntValue(index + 1);

            if (!op.canApply(result, value))
            {
                return null;
            }

            result = op.apply(result, value);
        }
        return Integer.valueOf(result);
    }

    /**
     * a gene sequence is valid if it doesn't contain any duplicate numbers
     * 
     * @param seq
     *            the sequence to evaluate
     * @return true if the sequence is a valid equation
     */
    public static boolean isValid(final GeneSequence seq)
    {
        final Set<Integer> uniqueValues = Sets.newHashSet();

        // only need to check the genes at even positions
        for (int index = 0; index < seq.size(); index += 2)
        {
            final int value = seq.getGeneIntValue(index);
            if (!uniqueValues.add(Integer.valueOf(value)))
            {
                return false;
            }
        }

        return true;
    }
}
