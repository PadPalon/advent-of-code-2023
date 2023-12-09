package ch.neukom.advent2023.day9;

import ch.neukom.advent2023.util.InputResourceReader;

import java.io.IOException;
import java.util.Arrays;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        long result = reader.readInput()
            .map(line -> line.split(" "))
            .map(parts -> Arrays.stream(parts).mapToLong(Long::parseLong).toArray())
            .mapToLong(Part2::findPreviousValue)
            .sum();
        System.out.printf("All previous values added together is %s", result);
    }

    private static long findPreviousValue(long[] numbers) {
        if (allTheSame(numbers)) {
            return numbers[0];
        } else {
            long firstNumber = numbers[0];
            long[] differences = new long[numbers.length - 1];
            for (int i = 0; i < differences.length; i++) {
                long difference = numbers[i + 1] - numbers[i];
                differences[i] = difference;
            }
            long previousValueDifference = findPreviousValue(differences);
            return firstNumber - previousValueDifference;
        }
    }

    private static boolean allTheSame(long[] numbers) {
        Long firstNumber = numbers[0];
        return Arrays.stream(numbers).allMatch(firstNumber::equals);
    }
}
