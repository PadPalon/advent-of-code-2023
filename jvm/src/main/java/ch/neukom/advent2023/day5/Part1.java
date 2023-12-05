package ch.neukom.advent2023.day5;

import ch.neukom.advent2023.util.InputResourceReader;
import ch.neukom.advent2023.util.reduce.UnsupportedCombiner;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        InputParser inputParser = reader.readInput().collect(collectingAndThen(joining("\n"), InputParser::new));
        List<CategoryMapper> categoryMappers = inputParser.parseCategoryMapper();
        long minimalLocation = inputParser.parseSeeds()
            .stream()
            .mapToLong(seed -> categoryMappers.stream().reduce(seed, (value, categoryMapper) -> categoryMapper.map(value), new UnsupportedCombiner<>()))
            .min()
            .orElseThrow();
        System.out.printf("The lowest location number is %s", minimalLocation);
    }
}
