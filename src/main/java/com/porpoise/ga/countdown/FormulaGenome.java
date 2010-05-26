package com.porpoise.ga.countdown;

import java.util.List;

import com.porpoise.common.Lists;
import com.porpoise.ga.impl.Genotype;

public class FormulaGenome {
    private final int                 numberCount;
    private final Genotype<Operator> operatorPool;
    private final Genotype<Integer>  numberPool;

    private final int                 poolSize = 40;

    // mutable state
    private final int                 count    = 0;

    public FormulaGenome(final int... numberValues) {
        final List<Integer> numbers = Lists.newArrayList();
        for (final int i : numberValues) {
            numbers.add(Integer.valueOf(i));
        }

        operatorPool = Genotype.<Operator> of(Operator.values());
        numberPool = Genotype.<Integer> of(numbers);
        numberCount = numbers.size();
    }

}
