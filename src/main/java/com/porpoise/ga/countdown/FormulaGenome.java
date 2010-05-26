package com.porpoise.ga.countdown;

import java.util.List;

import com.porpoise.common.Lists;
import com.porpoise.ga.IGene;
import com.porpoise.ga.impl.GeneData;
import com.porpoise.ga.impl.Genotypes;

public class FormulaGenome {
    private final int                           numberCount;
    private final Genotypes<GeneData<Operator>> operatorPool;
    private final Genotypes<GeneData<Integer>>  numberPool;

    private final int                           poolSize = 40;

    // mutable state
    private int                                 count    = 0;

    public FormulaGenome(final int... numberValues) {
        final List<Integer> numbers = Lists.newArrayList();
        for (final int i : numberValues) {
            numbers.add(Integer.valueOf(i));
        }

        operatorPool = Genotypes.<Operator> of(Operator.values());
        numberPool = Genotypes.<Integer> of(numbers);
        numberCount = numbers.size();
    }

    public boolean hasNext() {
        return count < poolSize;
    }

    public List<IGene<?>> nextSequence() {
        final List<IGene<?>> sequence = Lists.newArrayList();

        // we start with a number
        final IGene<GeneData<Integer>> firstNumber = numberPool.next();
        sequence.add(firstNumber);

        // remember, we're already a number down, so go over the rest,
        // filling in <operator> <number> <operator> <number> ...
        for (int i = numberCount - 1; --i >= 0;) {
            final IGene<GeneData<Operator>> nextOperator = operatorPool.next();
            sequence.add(nextOperator);

            final IGene<GeneData<Integer>> nextNumber = numberPool.next();
            sequence.add(nextNumber);
        }

        count++;
        return sequence;
    }

}
