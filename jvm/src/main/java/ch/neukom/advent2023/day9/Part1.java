package ch.neukom.advent2023.day9;

import ch.neukom.advent2023.util.InputResourceReader;

import java.io.IOException;
import java.util.Arrays;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        long result = reader.readInput()
            .map(line -> line.split(" "))
            .map(parts -> Arrays.stream(parts).mapToLong(Long::parseLong).toArray())
            .mapToLong(Part1::findNextValue)
            .sum();
        System.out.printf("All next values added together is %s", result);
    }

    private static long findNextValue(long[] numbers) {
        if (allTheSame(numbers)) {
            return numbers[0];
        } else {
            long lastNumber = numbers[numbers.length - 1];
            long[] differences = new long[numbers.length - 1];
            for (int i = 0; i < differences.length; i++) {
                long difference = numbers[i + 1] - numbers[i];
                differences[i] = difference;
            }
            long nextValueDifference = findNextValue(differences);
            return lastNumber + nextValueDifference;
        }
    }

    private static boolean allTheSame(long[] numbers) {
        Long firstNumber = numbers[0];
        return Arrays.stream(numbers).allMatch(firstNumber::equals);
    }
}
