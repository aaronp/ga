package com.porpoise.ga;

import org.junit.Test;

public class ChlorineImplTest {

    @Test
    public void test_evolve() {
        final IGeneEvaluation<Integer> criteria = TestEvaluations.constant(5);
        final Probability always = Probability.neverCrossAlwaysMutate();
        final IGenePool pool = TestGenePools.alphaNumeric(criteria, 7);

        final ChlorineImpl chlorineImpl = new ChlorineImpl(always);
        final IGenePool newPool = chlorineImpl.evolve(pool);
        System.out.println("before");
        System.out.println(pool);
        System.out.println("after");
        System.out.println(newPool);
    }

}
