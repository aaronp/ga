package com.porpoise.ga;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.porpoise.common.Lists;

public class GeneSequence implements Iterable<IGene<?>> {
    private final List<IGene<?>> genes;

    public GeneSequence(final IGene<?>... geneValues) {
        this(Arrays.asList(geneValues));
    }

    public GeneSequence(final List<IGene<?>> geneValues) {
        genes = Lists.newArrayList(geneValues);
    }

    /**
     * @return the genes
     */
    public List<IGene<?>> getGenes() {
        return ImmutableList.<IGene<?>> copyOf(genes);
    }

    @Override
    public Iterator<IGene<?>> iterator() {
        return genes.iterator();
    }

    public GeneSequence cross(final GeneSequence seqTwo) {
        return null;

    }

}
