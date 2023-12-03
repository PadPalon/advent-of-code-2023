package ch.neukom.advent2023.day3;

import ch.neukom.advent2023.util.InputResourceReader;

import java.io.IOException;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        long sum = Schematic.parse(reader).getNumbersAdjacentToSymbol()
            .stream()
            .mapToLong(i -> i)
            .sum();
        System.out.printf("The sum of engine parts is %s", sum);
    }
}
