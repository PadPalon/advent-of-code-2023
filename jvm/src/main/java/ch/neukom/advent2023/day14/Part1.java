package ch.neukom.advent2023.day14;

import ch.neukom.advent2023.util.InputArrayReader;
import ch.neukom.advent2023.util.InputArrayReader.ArrayWithInfo;

import java.io.IOException;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputArrayReader reader = new InputArrayReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputArrayReader reader) {
        ArrayWithInfo<RockType> input = Util.readRocks(reader);
        int lineCount = input.lineCount();
        int columnCount = input.columnCount();
        RockType[][] positions = Util.tiltNorth(input.array(), lineCount, columnCount);
        int load = Util.calculateLoad(positions, lineCount, columnCount);
        System.out.printf("The total load is %s", load);
    }
}
