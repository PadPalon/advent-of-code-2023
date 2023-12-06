package ch.neukom.advent2023.day6;

import ch.neukom.advent2023.util.InputResourceReader;
import com.google.common.base.Splitter;
import com.google.common.collect.Streams;

import java.io.IOException;
import java.util.List;
import java.util.stream.LongStream;

public class Part1 {
    private static final Splitter SPACE_SPLITTER = Splitter.on(' ').omitEmptyStrings().trimResults();

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        List<String> lines = reader.readInput().toList();
        List<Long> times = SPACE_SPLITTER.splitToStream(lines.get(0)).skip(1).map(Long::valueOf).toList();
        List<Long> distances = SPACE_SPLITTER.splitToStream(lines.get(1)).skip(1).map(Long::valueOf).toList();
        long result = Streams.zip(times.stream(), distances.stream(), Race::new)
            .map(Race::calculateWinningDistances)
            .mapToLong(LongStream::count)
            .reduce(Math::multiplyExact)
            .orElseThrow();
        System.out.printf("The amount of ways to win multiplied together is %s", result);
    }
}
