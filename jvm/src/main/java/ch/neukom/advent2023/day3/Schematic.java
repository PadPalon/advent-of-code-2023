package ch.neukom.advent2023.day3;

import ch.neukom.advent2023.util.InputResourceReader;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;

public class Schematic {
    private final char[][] schematic;
    private final int lineCount;
    private final int columnCount;

    private Schematic(char[][] schematic, int lineCount, int columnCount) {
        this.schematic = schematic;
        this.lineCount = lineCount;
        this.columnCount = columnCount;
    }

    public static Schematic parse(InputResourceReader reader) {
        long lineCount = reader.getLineCount();
        int columnCount = reader.getFirstLine().length();
        char[][] schematic = new char[columnCount][(int) lineCount];
        Streams.mapWithIndex(
                reader.readInput(),
                (line, lineIndex) -> Streams.mapWithIndex(
                    line.chars(),
                    (symbol, columnIndex) -> new CharacterPosition((char) symbol, (int) lineIndex, (int) columnIndex)
                )
            )
            .flatMap(i -> i)
            .forEach(characterPosition -> schematic[characterPosition.lineIndex()][characterPosition.columnIndex()] = characterPosition.symbol());
        return new Schematic(schematic, (int) lineCount, columnCount);
    }

    public List<Integer> getNumbersAdjacentToSymbol() {
        return parseNumbers()
            .stream()
            .filter(part -> isAdjacentToSymbol(part.lineIndex(), part.startIndex(), part.endIndex()))
            .map(EnginePart::number)
            .toList();
    }

    public LongStream getGearRatios() {
        Multimap<CharacterPosition, Integer> numbers = ArrayListMultimap.create();
        parseNumbers().forEach(part ->
            getAdjacentSymbols(part.lineIndex(), part.startIndex(), part.endIndex())
                .forEach(adjacentSymbol -> numbers.put(adjacentSymbol, part.number()))
        );
        return numbers.asMap()
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey().symbol() == '*')
            .filter(entry -> entry.getValue().size() == 2)
            .mapToLong(entry -> entry.getValue().stream().reduce(Math::multiplyExact).orElseThrow());
    }

    private Set<EnginePart> parseNumbers() {
        Set<EnginePart> engineParts = Sets.newHashSet();
        for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
            Integer startIndex = null;
            Integer endIndex = null;

            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                char symbol = schematic[lineIndex][columnIndex];

                if (startIndex == null && Character.isDigit(symbol)) {
                    startIndex = columnIndex;
                }
                if (startIndex != null && !Character.isDigit(symbol)) {
                    endIndex = columnIndex;
                }

                if (startIndex != null && endIndex != null) {
                    Integer number = parseNumber(lineIndex, startIndex, endIndex);
                    engineParts.add(new EnginePart(number, lineIndex, startIndex, endIndex));
                    startIndex = null;
                    endIndex = null;
                }
            }

            if (startIndex != null) {
                Integer number = parseNumber(lineIndex, startIndex, columnCount);
                engineParts.add(new EnginePart(number, lineIndex, startIndex, columnCount));
            }
        }
        return engineParts;
    }

    private Integer parseNumber(int lineIndex, Integer startIndex, Integer endIndex) {
        return IntStream.range(startIndex, endIndex)
            .map(columnIndex -> schematic[lineIndex][columnIndex])
            .mapToObj(i -> (char) i)
            .map(String::valueOf)
            .collect(collectingAndThen(joining(), Integer::valueOf));
    }

    private boolean isAdjacentToSymbol(int lineIndex,
                                       int startIndex,
                                       int endIndex) {
        Set<CharacterPosition> adjacentSymbols = getAdjacentSymbols(lineIndex, startIndex, endIndex);
        return adjacentSymbols.stream().map(CharacterPosition::symbol).anyMatch(symbol -> symbol != '.');
    }

    private Set<CharacterPosition> getAdjacentSymbols(int lineIndex,
                                                      int startIndex,
                                                      int endIndex) {
        Set<CharacterPosition> adjacentSymbols = Sets.newHashSet();
        if (startIndex > 0) {
            adjacentSymbols.add(getSymbol(schematic, lineIndex, startIndex - 1));
            if (lineIndex > 0) {
                adjacentSymbols.add(getSymbol(schematic, lineIndex - 1, startIndex - 1));
            }
            if (lineIndex + 1 < lineCount) {
                adjacentSymbols.add(getSymbol(schematic, lineIndex + 1, startIndex - 1));
            }
        }
        if (endIndex < columnCount) {
            adjacentSymbols.add(getSymbol(schematic, lineIndex, endIndex));
            if (lineIndex > 0) {
                adjacentSymbols.add(getSymbol(schematic, lineIndex - 1, endIndex));
            }
            if (lineIndex + 1 < lineCount) {
                adjacentSymbols.add(getSymbol(schematic, lineIndex + 1, endIndex));
            }
        }
        if (lineIndex > 0) {
            IntStream.range(startIndex, endIndex)
                .mapToObj(index -> getSymbol(schematic, lineIndex - 1, index))
                .forEach(adjacentSymbols::add);
        }
        if (lineIndex + 1 < lineCount) {
            IntStream.range(startIndex, endIndex)
                .mapToObj(index -> getSymbol(schematic, lineIndex + 1, index))
                .forEach(adjacentSymbols::add);
        }
        return adjacentSymbols;
    }

    private static CharacterPosition getSymbol(char[][] schematic, int lineIndex, int columnIndex) {
        char symbol = schematic[lineIndex][columnIndex];
        return new CharacterPosition(symbol, lineIndex, columnIndex);
    }

    private record CharacterPosition(char symbol, int lineIndex, int columnIndex) {
    }

    private record EnginePart(Integer number, int lineIndex, int startIndex, int endIndex) {
    }
}
