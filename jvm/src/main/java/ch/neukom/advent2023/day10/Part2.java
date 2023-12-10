package ch.neukom.advent2023.day10;

import ch.neukom.advent2023.day10.Pipes.PipePosition;
import ch.neukom.advent2023.util.InputResourceReader;

import java.io.IOException;
import java.util.*;

import static ch.neukom.advent2023.day10.Tile.Connection;
import static java.util.stream.Collectors.toSet;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        Pipes pipes = Pipes.parse(reader);
        Set<PipePosition> loop = new HashSet<>();
        loop.add(pipes.getStartingPosition());
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
                loop.addAll(connecting);
            }
        }

        Set<PipePosition> outsideStart = pipes.getByPredicate(position -> !loop.contains(position)
            && (
            position.lineIndex() == 0
                || position.lineIndex() == pipes.getLineCount() - 1
                || position.columnIndex() == 0
                || position.columnIndex() == pipes.getColumnCount() - 1
        ));
        Set<PipePosition> outside = new HashSet<>(outsideStart);
        boolean outsideComplete = false;
        while (!outsideComplete) {
            Set<PipePosition> ground = outside.stream()
                .map(pipes::getAdjacentPositions)
                .flatMap(Collection::stream)
                .filter(position -> !loop.contains(position))
                .filter(position -> !outside.contains(position))
                .collect(toSet());
            if (outside.containsAll(ground)) {
                outsideComplete = true;
            } else {
                outside.addAll(ground);
            }
        }

        /*
            at this point we have found all tiles easily connected to the border, so now we check all potential inside
            tiles if they are actually inside the loop by counting how many times the loop crosses their line on their
            left
        */

        Set<PipePosition> inside = pipes.getByPredicate(position -> !loop.contains(position) && !outside.contains(position))
            .stream()
            .filter(position -> calculateCrossingsLeftOfPosition(position, pipes, loop) % 2 == 1)
            .collect(toSet());

        System.out.printf("%s tiles are enclosed by the loop", inside.size());
    }

    private static int calculateCrossingsLeftOfPosition(PipePosition source, Pipes pipes, Set<PipePosition> loop) {
        List<PipePosition> leftOfPosition = pipes.getByPredicate(position ->
                position.columnIndex() > source.columnIndex()
                    && position.lineIndex() == source.lineIndex()
                    && loop.contains(position)
            )
            .stream()
            .sorted(Comparator.comparingInt(PipePosition::columnIndex))
            .toList();

        int crossings = 0;
        Connection connectionNeeded = null;
        for (PipePosition position : leftOfPosition) {
            switch (position.tile()) {
                case VERTICAL -> crossings++;
                case NORTH_WEST, NORTH_EAST -> {
                    if (connectionNeeded == null) {
                        connectionNeeded = Connection.SOUTH;
                    } else if (connectionNeeded == Connection.NORTH) {
                        connectionNeeded = null;
                        crossings++;
                    } else {
                        connectionNeeded = null;
                    }
                }
                case SOUTH_WEST, SOUTH_EAST -> {
                    if (connectionNeeded == null) {
                        connectionNeeded = Connection.NORTH;
                    } else if (connectionNeeded == Connection.SOUTH) {
                        connectionNeeded = null;
                        crossings++;
                    } else {
                        connectionNeeded = null;
                    }
                }
                default -> {
                }
            }
        }
        return crossings;
    }
}
