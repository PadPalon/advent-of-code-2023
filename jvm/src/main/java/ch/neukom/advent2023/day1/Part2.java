package ch.neukom.advent2023.day1;

import java.io.IOException;
import java.util.Map;
import java.util.stream.IntStream;

import ch.neukom.advent2023.util.InputResourceReader;
import ch.neukom.advent2023.util.reduce.UnsupportedCombiner;

public class Part2 {
    /**
     * highly cursed, but adding the first and last character to the digit makes sure we do not break any adjacent
     * written out numbers
     */
    private static final Map<String, String> DIGIT_REPLACEMENTS = Map.of(
            "one", "o1e",
            "two", "t2o",
            "three", "t3e",
            "four", "f4r",
            "five", "f5e",
            "six", "s6x",
            "seven", "s7n",
            "eight", "e8t",
            "nine", "n9e"
    );

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        double sum = reader.readInput()
                .map(Part2::filterDigits)
                .mapToInt(Part2::combineFirstAndLastDigit)
                .sum();
        System.out.printf("The sum of the first and last digit on each line is %s%n", sum);
    }

    private static IntStream filterDigits(String line) {
        String digitLine = DIGIT_REPLACEMENTS.entrySet()
                .stream()
                .reduce(
                        line,
                        (string, replacement) -> string.replace(replacement.getKey(), replacement.getValue()),
                        new UnsupportedCombiner<>()
                );
        return digitLine.chars().filter(Character::isDigit).map(Part2::digitToInt);
    }

    private static int digitToInt(int digit) {
        return digit - '0';
    }

    private static Integer combineFirstAndLastDigit(IntStream line) {
        int[] array = line.toArray();
        int length = array.length;
        return Integer.valueOf("%s%s".formatted(array[0], array[length - 1]));
    }
}