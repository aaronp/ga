package com.porpoise.countdown;

import java.util.List;

import com.porpoise.IGene;
import com.porpoise.IGenome;
import com.porpoise.IMutator;
import com.porpoise.common.Lists;
import com.porpoise.impl.GeneData;
import com.porpoise.impl.GeneSequence;

public class FormulaGenome implements IGenome {
    private final int                              numberCount;
    private final GeneSequence<GeneData<Operator>> operatorPool;
    private final GeneSequence<GeneData<Integer>>  numberPool;

    private final int                              poolSize = 40;

    // mutable state
    private int                                    count    = 0;

    public FormulaGenome(final int... numberValues) {
        final List<Integer> numbers = Lists.newArrayList();
        for (final int i : numberValues) {
            numbers.add(Integer.valueOf(i));
        }

        operatorPool = GeneSequence.<Operator> of(Operator.values());
        numberPool = GeneSequence.<Integer> of(numbers);
        numberCount = numbers.size();
    }

    @Override
    public boolean hasNext() {
        return count < poolSize;
    }

    @Override
    public List<IGene<?>> nextSequence(final IMutator m, final boolean cross) {
        final List<IGene<?>> sequence = Lists.newArrayList();

        // we start with a number
        final IGene<GeneData<Integer>> firstNumber = numberPool.next(m, cross);
        sequence.add(firstNumber);

        // remember, we're already a number down, so go over the rest,
        // filling in <operator> <number> <operator> <number> ...
        for (int i = numberCount - 1; --i >= 0;) {
            final IGene<GeneData<Operator>> nextOperator = operatorPool.next(m, cross);
            sequence.add(nextOperator);

            final IGene<GeneData<Integer>> nextNumber = numberPool.next(m, cross);
            sequence.add(nextNumber);
        }

        count++;
        return sequence;
    }

}
