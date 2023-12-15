package ch.neukom.advent2023.day13;

import ch.neukom.advent2023.util.InputArrayReader;
import ch.neukom.advent2023.util.InputResourceReader;
import com.google.common.base.Splitter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;

public class Part1 {
    private static final Splitter PART_SPLITTER = Splitter.on("\n\n").omitEmptyStrings().trimResults();

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        int sum = reader.readInput()
            .collect(collectingAndThen(joining("\n"), PART_SPLITTER::splitToStream))
            .mapToInt(part -> {
                String[] lines = part.split("\n");
                int lineCount = lines.length;
                int columnCount = lines[0].length();
                Character[][] array = InputArrayReader.buildArray(InputArrayReader.Symbol::symbol, Character.class, part::lines, lineCount, columnCount);

                Set<Integer> columnMirrors = findColumnMirrors(columnCount, lineCount, array);
                Set<Integer> rowMirrors = findRowMirrors(lineCount, array);
                return Stream.concat(
                    columnMirrors.stream(),
                    rowMirrors.stream().map(i -> i * 100)
                ).mapToInt(i -> i).sum();
            })
            .sum();
        System.out.printf("The sum of all the mirrors is %s", sum);
    }

    private static Set<Integer> findColumnMirrors(int columnCount, int lineCount, Character[][] array) {
        List<Character[]> columns = new ArrayList<>();
        Set<Integer> potentialMirrors = new HashSet<>();
        for (int column = 0; column < columnCount; column++) {
            Character[] columnCharacters = new Character[lineCount];
            for (int line = 0; line < lineCount; line++) {
                columnCharacters[line] = array[line][column];
            }
            if (!columns.isEmpty() && Arrays.equals(columns.getLast(), columnCharacters)) {
                potentialMirrors.add(column);
            }
            columns.addLast(columnCharacters);
        }
        return checkPotentialMirrors(potentialMirrors, columns);
    }

    private static Set<Integer> findRowMirrors(int lineCount, Character[][] array) {
        List<Character[]> rows = new ArrayList<>();
        Set<Integer> potentialMirrors = new HashSet<>();
        for (int line = 0; line < lineCount; line++) {
            Character[] lineCharacters = array[line];
            if (!rows.isEmpty() && Arrays.equals(rows.getLast(), lineCharacters)) {
                potentialMirrors.add(line);
            }
            rows.addLast(lineCharacters);
        }
        return checkPotentialMirrors(potentialMirrors, rows);
    }

    private static Set<Integer> checkPotentialMirrors(Set<Integer> potentialMirrors, List<Character[]> data) {
        Set<Integer> validMirrors = new HashSet<>();
        for (Integer potentialMirror : potentialMirrors) {
            int leftIndex = potentialMirror - 1;
            int rightIndex = potentialMirror;
            boolean validMirror = true;
            while (validMirror && leftIndex >= 0 && rightIndex < data.size()) {
                Character[] left = data.get(leftIndex);
                Character[] right = data.get(rightIndex);
                if (!Arrays.equals(left, right)) {
                    validMirror = false;
                }
                leftIndex--;
                rightIndex++;
            }
            if (validMirror) {
                validMirrors.add(potentialMirror);
            }
        }
        return validMirrors;
    }
}
