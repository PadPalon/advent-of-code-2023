package ch.neukom.advent2023.day15;

public class Hash {
    private Hash() {
    }

    static int calculateHash(String step) {
        return step.chars().reduce(0, (left, right) -> (left + right) * 17 % 256);
    }
}
