package ch.neukom.advent2023.day1;

import java.io.IOException;
import java.util.stream.IntStream;

import ch.neukom.advent2023.util.InputResourceReader;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        double sum = reader.readInput()
                .map(Part1::filterDigits)
                .mapToInt(Part1::combineFirstAndLastDigit)
                .sum();
        System.out.printf("The sum of the first and last digit on each line is %s%n", sum);
    }

    private static IntStream filterDigits(String line) {
        return line.chars().filter(Character::isDigit).map(Part1::digitToInt);
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