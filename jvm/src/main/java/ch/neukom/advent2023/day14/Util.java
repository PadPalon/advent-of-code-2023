package ch.neukom.advent2023.day14;

import ch.neukom.advent2023.util.InputArrayReader;
import ch.neukom.advent2023.util.InputArrayReader.ArrayWithInfo;

import java.util.Arrays;

public class Util {
    private Util() {
    }

    static int calculateLoad(RockType[][] finalPositions, int lineCount, int columnCount) {
        int load = 0;
        for (int line = 0; line < lineCount; line++) {
            for (int column = 0; column < columnCount; column++) {
                RockType rock = finalPositions[line][column];
                load += switch (rock) {
                    case ROUND -> lineCount - line;
                    case CUBE, NONE -> 0;
                };
            }
        }
        return load;
    }

    // too lazy to generalize the different directions
    static RockType[][] tiltNorth(RockType[][] input, int lineCount, int columnCount) {
        RockType[][] positions = deepCopy(input);
        RockType[][] lastPositions;
        do {
            lastPositions = deepCopy(positions);
            for (int line = 0; line + 1 < lineCount; line++) {
                for (int column = 0; column < columnCount; column++) {
                    RockType rock = positions[line][column];
                    RockType southernRock = positions[line + 1][column];
                    if (rock == RockType.NONE && southernRock == RockType.ROUND) {
                        positions[line][column] = southernRock;
                        positions[line + 1][column] = RockType.NONE;
                    }
                }
            }
        } while (!Arrays.deepEquals(positions, lastPositions));
        return positions;
    }

    static RockType[][] tiltSouth(RockType[][] input, int lineCount, int columnCount) {
        RockType[][] positions = deepCopy(input);
        RockType[][] lastPositions;
        do {
            lastPositions = deepCopy(positions);
            for (int line = lineCount - 1; line - 1 >= 0; line--) {
                for (int column = 0; column < columnCount; column++) {
                    RockType rock = positions[line][column];
                    RockType northernRock = positions[line - 1][column];
                    if (rock == RockType.NONE && northernRock == RockType.ROUND) {
                        positions[line][column] = northernRock;
                        positions[line - 1][column] = RockType.NONE;
                    }
                }
            }
        } while (!Arrays.deepEquals(positions, lastPositions));
        return positions;
    }

    static RockType[][] tiltWest(RockType[][] input, int lineCount, int columnCount) {
        RockType[][] positions = deepCopy(input);
        RockType[][] lastPositions;
        do {
            lastPositions = deepCopy(positions);
            for (int line = 0; line < lineCount; line++) {
                for (int column = 0; column + 1 < columnCount; column++) {
                    RockType rock = positions[line][column];
                    RockType easternRock = positions[line][column + 1];
                    if (rock == RockType.NONE && easternRock == RockType.ROUND) {
                        positions[line][column] = easternRock;
                        positions[line][column + 1] = RockType.NONE;
                    }
                }
            }
        } while (!Arrays.deepEquals(positions, lastPositions));
        return positions;
    }

    static RockType[][] tiltEast(RockType[][] input, int lineCount, int columnCount) {
        RockType[][] positions = deepCopy(input);
        RockType[][] lastPositions;
        do {
            lastPositions = deepCopy(positions);
            for (int line = 0; line < lineCount; line++) {
                for (int column = columnCount - 1; column - 1 >= 0; column--) {
                    RockType rock = positions[line][column];
                    RockType westernRock = positions[line][column - 1];
                    if (rock == RockType.NONE && westernRock == RockType.ROUND) {
                        positions[line][column] = westernRock;
                        positions[line][column - 1] = RockType.NONE;
                    }
                }
            }
        } while (!Arrays.deepEquals(positions, lastPositions));
        return positions;
    }

    public static RockType[][] deepCopy(RockType[][] input) {
        RockType[][] result = new RockType[input.length][];
        for (int i = 0; i < input.length; i++) {
            result[i] = input[i].clone();
        }
        return result;
    }

    static ArrayWithInfo<RockType> readRocks(InputArrayReader reader) {
        return reader.readIntoArray(symbol -> switch (symbol.symbol()) {
            case '#' -> RockType.CUBE;
            case 'O' -> RockType.ROUND;
            case '.' -> RockType.NONE;
            default -> throw new IllegalArgumentException();
        }, RockType.class);
    }
}
