package ch.neukom.advent2023.util.math;

import java.math.BigInteger;

public class MathHelper {
    private MathHelper() {
    }

    public static BigInteger lcm(BigInteger left, BigInteger right) {
        BigInteger gcd = left.gcd(right);
        BigInteger absProduct = left.multiply(right).abs();
        return absProduct.divide(gcd);
    }
}
