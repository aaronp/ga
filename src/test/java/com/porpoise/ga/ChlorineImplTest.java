package com.porpoise.ga;

import org.junit.Test;

public class ChlorineImplTest {

    @Test
    public void test_evolve() {
        final IGeneEvaluation criteria = TestEvaluations.constant(0.5F);
        final Probability always = Probability.neverCrossAlwaysMutate();
        final IGenePool pool = TestGenePools.alphaNumeric(criteria, 7);

        final ChlorineImpl chlorineImpl = new ChlorineImpl(criteria, always);
        final IGenePool newPool = chlorineImpl.evolve(pool);
        System.out.println("before");
        System.out.println(pool);
        System.out.println("after");
        System.out.println(newPool);
    }

}
