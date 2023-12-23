package ch.neukom.advent2023.day16;

import ch.neukom.advent2023.util.InputArrayReader;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import static ch.neukom.advent2023.day16.Contraption.Type.*;

public class Util {
    private Util() {}

    static Contraption setupContraption(InputArrayReader reader) {
        Contraption.Type[][] grid = reader.readIntoArray(symbol -> switch (symbol.symbol()) {
            case '.' -> EMPTY;
            case '|' -> SPLITTER_VERTICAL;
            case '-' -> SPLITTER_HORIZONTAL;
            case '/' -> NE_SW;
            case '\\' -> NW_SE;
            default -> throw new IllegalStateException("Unexpected value: %s".formatted(symbol.symbol()));
        }, Contraption.Type.class);
        return new Contraption(grid);
    }

    static int runLaser(Contraption contraption, Contraption.Position startPosition, Contraption.Direction startDirection) {
        Laser startingLaser = new Laser(startPosition, startDirection);
        Set<Contraption.Position> energized = new HashSet<>();
        Set<Laser> knownLasers = new HashSet<>();
        Deque<Laser> lasers = new ArrayDeque<>();
        lasers.add(startingLaser);
        while (!lasers.isEmpty()) {
            Laser laser = lasers.pop();
            if (!knownLasers.contains(laser)) {
                knownLasers.add(laser);
                energized.add(laser.getPosition());
                contraption.visit(laser).forEach(lasers::push);
            }
        }
        return energized.size();
    }
}
