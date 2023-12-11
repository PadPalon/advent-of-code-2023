package ch.neukom.advent2023.day11;

import ch.neukom.advent2023.util.InputArrayReader;
import com.google.common.collect.Sets;

import java.io.IOException;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputArrayReader reader = new InputArrayReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputArrayReader reader) {
        long sumOfPaths = Sets.combinations(Observation.parse(reader, 1000000), 2)
            .stream()
            .mapToLong(Observation::calculateShortestPath)
            .sum();
        System.out.printf("The sum of shortest paths between galaxies is %s", sumOfPaths);
    }
}
