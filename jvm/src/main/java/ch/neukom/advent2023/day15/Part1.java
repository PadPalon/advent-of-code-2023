package ch.neukom.advent2023.day15;

import ch.neukom.advent2023.util.InputResourceReader;
import com.google.common.base.Splitter;

import java.io.IOException;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;

public class Part1 {
    private static final Splitter COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        int result = reader.readInput().collect(collectingAndThen(joining(), COMMA_SPLITTER::splitToStream))
            .mapToInt(Hash::calculateHash)
            .sum();
        System.out.printf("The sum of hashes is %s", result);
    }
}
