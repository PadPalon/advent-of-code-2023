package ch.neukom.advent2023.day11;

import ch.neukom.advent2023.util.InputArrayReader;
import ch.neukom.advent2023.util.InputArrayReader.Symbol;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

import java.util.HashSet;
import java.util.Set;

public class Observation {
    private Observation() {
    }

    public static Set<Position> parse(InputArrayReader reader, long scale) {
        long lineCount = reader.getLineCount();
        int columnCount = reader.getFirstLine().length();
        Character[][] observation = reader.readIntoArray(Symbol::symbol, Character.class);

        Set<Long> emptyLines = new HashSet<>();
        for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
            boolean galaxyFound = false;
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                char symbol = observation[lineIndex][columnIndex];
                if (symbol == '#') {
                    galaxyFound = true;
                    break;
                }
            }
            if (!galaxyFound) {
                emptyLines.add((long) lineIndex);
            }
        }


        Set<Long> emptyColumns = new HashSet<>();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            boolean galaxyFound = false;
            for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
                char symbol = observation[lineIndex][columnIndex];
                if (symbol == '#') {
                    galaxyFound = true;
                    break;
                }
            }
            if (!galaxyFound) {
                emptyColumns.add((long) columnIndex);
            }
        }

        Set<Position> galaxies = new HashSet<>();
        long adjustment = scale - 1;
        long expandedLineCount = lineCount + emptyLines.size() * adjustment;
        long expandedColumnCount = columnCount + emptyColumns.size() * adjustment;
        long lineOffset = 0;
        for (long lineIndex = 0; lineIndex < expandedLineCount; lineIndex++) {
            if (emptyLines.contains(lineIndex - lineOffset)) {
                lineOffset += adjustment;
                lineIndex += adjustment;
                continue;
            }
            long columnOffset = 0;
            for (long columnIndex = 0; columnIndex < expandedColumnCount; columnIndex++) {
                if (emptyColumns.contains(columnIndex - columnOffset)) {
                    columnOffset += adjustment;
                    columnIndex += adjustment;
                    continue;
                }
                char symbol = observation[(int) (lineIndex - lineOffset)][(int) (columnIndex - columnOffset)];
                if (symbol == '#') {
                    galaxies.add(new Position(symbol, lineIndex, columnIndex));
                }
            }
        }

        return galaxies;
    }

    public static long calculateShortestPath(Set<Position> galaxies) {
        Preconditions.checkState(galaxies.size() == 2);
        Observation.Position first = Iterables.get(galaxies, 0);
        Observation.Position second = Iterables.get(galaxies, 1);
        long lineDifference = Math.abs(first.lineIndex() - second.lineIndex());
        long columnDifference = Math.abs(first.columnIndex() - second.columnIndex());
        return lineDifference + columnDifference;
    }

    public record Position(char symbol, long lineIndex, long columnIndex) {
    }
}
