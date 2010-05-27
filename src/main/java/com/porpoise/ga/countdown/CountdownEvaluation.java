package com.porpoise.ga.countdown;

import com.porpoise.ga.GeneSequence;
import com.porpoise.ga.IGeneEvaluation;
import com.porpoise.ga.IScore;
import com.porpoise.ga.Scores;
import com.porpoise.ga.Scores.ComparableScore;

class CountdownEvaluation implements IGeneEvaluation<Integer> {

    private final int target;

    public CountdownEvaluation(final int targetValue) {
        target = targetValue;
    }

    @Override
    public IScore<Integer> score(final GeneSequence sequence) {
        final Integer result = FormulaDecoder.getValue(sequence);
        if (result == null) {
            return Scores.invalidInt();
        }

        final int resultValue = result.intValue();
        return score(resultValue).setValid(FormulaDecoder.isValid(sequence));
    }

    /**
     * @param resultValue
     * @return
     */
    final ComparableScore<Integer> score(final int resultValue) {
        final int diff = Math.abs(resultValue - target);
        return Scores.valueOf(diff).setComplete(diff == 0);
    }

}