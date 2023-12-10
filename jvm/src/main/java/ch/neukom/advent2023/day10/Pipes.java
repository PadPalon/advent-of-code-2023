package ch.neukom.advent2023.day10;

import ch.neukom.advent2023.day10.Tile.Connection;
import ch.neukom.advent2023.util.InputResourceReader;
import com.google.common.collect.Streams;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static ch.neukom.advent2023.day10.Tile.Connection.*;
import static java.util.stream.Collectors.toSet;

public class Pipes {
    private final PipePosition[][] pipePositions;

    private final int lineCount;
    private final int columnCount;

    public Pipes(PipePosition[][] pipePositions, int lineCount, int columnCount) {
        this.pipePositions = pipePositions;
        this.lineCount = lineCount;
        this.columnCount = columnCount;
    }

    public static Pipes parse(InputResourceReader reader) {
        long lineCount = reader.getLineCount();
        int columnCount = reader.getFirstLine().length();
        PipePosition[][] map = new PipePosition[(int) lineCount][columnCount];
        Streams.mapWithIndex(
                reader.readInput(),
                (line, lineIndex) -> Streams.mapWithIndex(
                    line.chars(),
                    (symbol, columnIndex) -> new Pipes.PipePosition(Tile.getByRepresentation((char) symbol), (int) lineIndex, (int) columnIndex)
                )
            )
            .flatMap(i -> i)
            .forEach(pipePosition -> map[pipePosition.lineIndex()][pipePosition.columnIndex()] = pipePosition);
        return new Pipes(map, (int) lineCount, columnCount);
    }

    public int getLineCount() {
        return lineCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public PipePosition getStartingPosition() {
        PipePosition startingPosition = Arrays.stream(pipePositions)
            .flatMap(Arrays::stream)
            .filter(position -> position.tile() == Tile.START)
            .findFirst()
            .orElseThrow();
        return replaceStartWithActualTile(startingPosition);
    }

    private PipePosition replaceStartWithActualTile(PipePosition startingPosition) {
        Set<Connection> connections = getConnectingPosition(startingPosition).stream()
            .map(startingPosition::calculateDirection)
            .collect(toSet());
        Tile replacementTile = Arrays.stream(Tile.values())
            .filter(tile -> tile.hasConnections(connections))
            .findAny()
            .orElseThrow();
        int startingLineIndex = startingPosition.lineIndex();
        int startingColumnIndex = startingPosition.columnIndex();
        PipePosition replacement = new PipePosition(replacementTile, startingLineIndex, startingColumnIndex);
        pipePositions[startingLineIndex][startingColumnIndex] = replacement;
        return replacement;
    }

    public Set<PipePosition> getByPredicate(Predicate<PipePosition> predicate) {
        return Arrays.stream(pipePositions)
            .flatMap(Arrays::stream)
            .filter(predicate)
            .collect(toSet());
    }

    public Set<PipePosition> getConnectingPosition(PipePosition position) {
        return getAdjacentPositions(position)
            .stream()
            .filter(position::matches)
            .collect(toSet());
    }

    public Set<PipePosition> getAdjacentPositions(PipePosition position) {
        Set<PipePosition> adjacent = new HashSet<>();
        if (position.lineIndex() > 0) {
            adjacent.add(pipePositions[position.lineIndex() - 1][position.columnIndex()]);
        }
        if (position.lineIndex() + 1 < lineCount) {
            adjacent.add(pipePositions[position.lineIndex() + 1][position.columnIndex()]);
        }
        if (position.columnIndex() > 0) {
            adjacent.add(pipePositions[position.lineIndex()][position.columnIndex() - 1]);
        }
        if (position.columnIndex() + 1 < columnCount) {
            adjacent.add(pipePositions[position.lineIndex()][position.columnIndex() + 1]);
        }
        return adjacent;
    }

    public record PipePosition(Tile tile, int lineIndex, int columnIndex) {
        public boolean matches(PipePosition other) {
            Connection direction = calculateDirection(other);
            return tile().matches(other.tile(), direction);
        }

        private Connection calculateDirection(PipePosition other) {
            if (lineIndex < other.lineIndex()) {
                return SOUTH;
            } else if (lineIndex > other.lineIndex()) {
                return NORTH;
            } else if (columnIndex < other.columnIndex()) {
                return EAST;
            } else if (columnIndex > other.columnIndex()) {
                return WEST;
            } else {
                throw new IllegalArgumentException("Trying to calculate direction from position to itself");
            }
        }
    }
}
