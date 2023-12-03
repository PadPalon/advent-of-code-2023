package ch.neukom.advent2023.day3;

import ch.neukom.advent2023.util.InputResourceReader;

import java.io.IOException;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        long sum = Schematic.parse(reader).getGearRatios().sum();
        System.out.printf("The sum of gear ratios is %s", sum);
    }
}
