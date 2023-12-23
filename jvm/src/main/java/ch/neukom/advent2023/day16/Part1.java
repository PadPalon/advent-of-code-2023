package ch.neukom.advent2023.day16;

import java.io.IOException;

import ch.neukom.advent2023.day16.Contraption.Position;
import ch.neukom.advent2023.util.InputArrayReader;

import static ch.neukom.advent2023.day16.Contraption.Direction.EAST;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputArrayReader reader = new InputArrayReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputArrayReader reader) {
        Contraption contraption = Util.setupContraption(reader);
        int energizedTiles = Util.runLaser(contraption, new Position(0, 0), EAST);
        System.out.printf("%s tiles are energized", energizedTiles);
    }
}
