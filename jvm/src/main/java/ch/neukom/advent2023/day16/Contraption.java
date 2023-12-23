package ch.neukom.advent2023.day16;

import java.util.Collection;
import java.util.List;

import static ch.neukom.advent2023.day16.Contraption.Direction.*;

public class Contraption {
    private final Type[][] grid;
    private final int lineCount;
    private final int columnCount;

    public Contraption(Type[][] grid) {
        this.grid = grid;
        this.lineCount = grid.length;
        this.columnCount = grid[0].length;
    }

    public Collection<Laser> visit(Laser laser) {
        Position newPosition = laser.getPosition().move(laser.getDirection());
        if (newPosition.isValid(lineCount, columnCount)) {
            Laser newLaser = laser.move(newPosition);
            return switch (grid[newPosition.line()][newPosition.column()]) {
                case EMPTY -> List.of(newLaser);
                case NE_SW -> List.of(switch (newLaser.getDirection()) {
                    case NORTH -> newLaser.split(EAST);
                    case EAST -> newLaser.split(NORTH);
                    case SOUTH -> newLaser.split(WEST);
                    case WEST -> newLaser.split(SOUTH);
                });
                case NW_SE -> List.of(switch (newLaser.getDirection()) {
                    case NORTH -> newLaser.split(WEST);
                    case EAST -> newLaser.split(SOUTH);
                    case SOUTH -> newLaser.split(EAST);
                    case WEST -> newLaser.split(NORTH);
                });
                case SPLITTER_VERTICAL -> switch (newLaser.getDirection()) {
                    case NORTH, SOUTH -> List.of(newLaser);
                    case EAST, WEST -> List.of(newLaser.split(NORTH), newLaser.split(SOUTH));
                };
                case SPLITTER_HORIZONTAL -> switch (newLaser.getDirection()) {
                    case NORTH, SOUTH -> List.of(newLaser.split(EAST), newLaser.split(WEST));
                    case EAST, WEST -> List.of(newLaser);
                };
            };
        } else {
            return List.of();
        }
    }

    public int getLineCount() {
        return lineCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public enum Type {
        EMPTY,
        NE_SW,
        NW_SE,
        SPLITTER_VERTICAL,
        SPLITTER_HORIZONTAL
    }

    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST;
    }

    public record Position(int line, int column) {
        public Position move(Direction direction) {
            return switch (direction) {
                case NORTH -> new Position(line - 1, column);
                case EAST -> new Position(line, column + 1);
                case SOUTH -> new Position(line + 1, column);
                case WEST -> new Position(line, column - 1);
            };
        }

        public boolean isValid(int lineCount, int columnCount) {
            if (line < 0) {
                return false;
            }
            if (column < 0) {
                return false;
            }
            if (line >= lineCount) {
                return false;
            }
            if (column >= columnCount) {
                return false;
            }
            return true;
        }
    }
}
