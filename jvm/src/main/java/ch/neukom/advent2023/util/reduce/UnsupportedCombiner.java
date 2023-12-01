package ch.neukom.advent2023.util.reduce;

import java.util.function.BinaryOperator;

public class UnsupportedCombiner<P> implements BinaryOperator<P> {
    @Override
    public P apply(P left, P right) {
        throw new UnsupportedOperationException("Do not run this in parallel");
    }
}
