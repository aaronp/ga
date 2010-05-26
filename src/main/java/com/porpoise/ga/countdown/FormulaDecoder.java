package com.porpoise.ga.countdown;

import com.porpoise.ga.GeneSequence;

public class FormulaDecoder {

    /**
     * Convert the sequence into a total
     * 
     * @param seq
     * @return
     */
    public static Integer getValue(final GeneSequence seq) {
        int result = seq.getGeneIntValue(0);

        for (int index = 1; index < seq.size(); index += 2) {
            final Operator op = seq.getGeneValue(index);
            final int value = seq.getGeneIntValue(index + 1);

            if (!op.canApply(result, value)) {
                return null;
            }

            result = op.apply(result, value);
        }
        return Integer.valueOf(result);
    }
}
