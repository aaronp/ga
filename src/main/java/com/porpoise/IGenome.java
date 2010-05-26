package com.porpoise;

import java.util.List;

public interface IGenome {

    public List<IGene<?>> nextSequence(IMutator m, boolean cross);

    public boolean hasNext();

}
