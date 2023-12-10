package ch.neukom.advent2023.day10;

import ch.neukom.advent2023.day10.Pipes.PipePosition;
import ch.neukom.advent2023.util.InputResourceReader;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        Pipes pipes = Pipes.parse(reader);
        Set<PipePosition> loop = new HashSet<>();
        loop.add(pipes.getStartingPosition());
        int stepCount = 0;
        boolean loopComplete = false;
        while (!loopComplete) {
            Set<PipePosition> connecting = loop.stream()
                .map(pipes::getConnectingPosition)
                .flatMap(Collection::stream)
                .filter(position -> !loop.contains(position))
                .collect(toSet());
            if (loop.containsAll(connecting)) {
                loopComplete = true;
            } else {
                stepCount++;
                loop.addAll(connecting);
            }
        }
        System.out.printf("The farthest tile is %s moves away", stepCount);
    }
}
