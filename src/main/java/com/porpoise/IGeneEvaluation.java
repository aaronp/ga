package com.porpoise;

import java.util.List;

public interface IGeneEvaluation {

    public float score(List<IGene<?>> sequence);

}
