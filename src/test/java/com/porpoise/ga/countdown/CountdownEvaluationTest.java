package com.porpoise.ga.countdown;

import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.porpoise.ga.IScore;

public class CountdownEvaluationTest {

    @Test
    public void test_score() {
        final CountdownEvaluation eval = new CountdownEvaluation(100);
        final List<IScore<Integer>> scores = Lists.newArrayList();
        scores.add(assertScore(eval, 100, 0));
        scores.add(assertScore(eval, 50, 50));
        scores.add(assertScore(eval, 150, -50));
        scores.add(assertScore(eval, 50, 150));
        scores.add(assertScore(eval, 1, 99));

        Collections.sort(scores);

        final List<Integer> values = Lists.transform(scores, new Function<IScore<Integer>, Integer>() {
            @Override
            public Integer apply(final IScore<Integer> arg0) {
                return arg0.getValue();
            }
        });

        // assert the order
        Assert.assertEquals(Integer.valueOf(1), values.get(0));
        Assert.assertEquals(Integer.valueOf(50), values.get(1));
        Assert.assertEquals(Integer.valueOf(50), values.get(2));
        Assert.assertEquals(Integer.valueOf(100), values.get(3));
        Assert.assertEquals(Integer.valueOf(150), values.get(4));
    }

    /**
     * @param eval
     * @param expectedScoreValue
     * @param resultValue
     * @return
     */
    private IScore<Integer> assertScore(final CountdownEvaluation eval, final int expectedScoreValue,
            final int resultValue) {
        final IScore<Integer> s = eval.score(resultValue);
        Assert.assertEquals(Integer.valueOf(expectedScoreValue), s.getValue());
        return s;
    }
}
