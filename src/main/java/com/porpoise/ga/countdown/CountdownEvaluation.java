package com.porpoise.ga.countdown;

import com.porpoise.ga.GeneSequence;
import com.porpoise.ga.IGeneEvaluation;

class CountdownEvaluation implements IGeneEvaluation {

    private final int target;

    public CountdownEvaluation(final int targetValue) {
        target = targetValue;
    }

    @Override
    public float score(final GeneSequence sequence) {
        final Integer result = FormulaDecoder.getValue(sequence);
        if (result == null) {
            return Float.MAX_VALUE;
        }

        final int resultValue = result.intValue();
        return score(resultValue);
    }

    /**
     * @param resultValue
     * @return
     */
    final float score(final int resultValue) {
        final int diff = Math.abs(target - resultValue);
        if (diff == 0) {
            return 0F;
        }
        return 1 / diff;
    }

}
