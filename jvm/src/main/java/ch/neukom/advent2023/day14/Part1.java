package ch.neukom.advent2023.day14;

import ch.neukom.advent2023.util.InputArrayReader;

import java.io.IOException;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputArrayReader reader = new InputArrayReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputArrayReader reader) {
        int lineCount = (int) reader.getLineCount();
        int columnCount = reader.getFirstLine().length();
        RockType[][] positions = Util.tiltNorth(Util.readRocks(reader), lineCount, columnCount);
        int load = Util.calculateLoad(positions, lineCount, columnCount);
        System.out.printf("The total load is %s", load);
    }
}
