package ch.neukom.advent2023.day10;

import java.util.Arrays;
import java.util.Set;

import static ch.neukom.advent2023.day10.Tile.Connection.*;

public enum Tile {
    VERTICAL('|', NORTH, SOUTH),
    HORIZONTAL('-', WEST, EAST),
    NORTH_EAST('L', NORTH, EAST),
    NORTH_WEST('J', NORTH, WEST),
    SOUTH_WEST('7', SOUTH, WEST),
    SOUTH_EAST('F', SOUTH, EAST),
    GROUND('.'),
    START('S', NORTH, SOUTH, WEST, EAST);

    private final char representation;
    private final Set<Connection> connections;

    Tile(char representation, Connection... connections) {
        this.representation = representation;
        this.connections = Set.of(connections);
    }

    public static Tile getByRepresentation(char representation) {
        return Arrays.stream(Tile.values())
            .filter(tile -> tile.representation == representation)
            .findFirst()
            .orElseThrow();
    }

    public boolean matches(Tile other, Connection direction) {
        return connections.contains(direction) && other.connections.contains(getOpposingConnection(direction));
    }

    public boolean hasConnections(Set<Connection> connections) {
        return this.connections.equals(connections);
    }

    private Connection getOpposingConnection(Connection connection) {
        return switch (connection) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case WEST -> EAST;
            case EAST -> WEST;
        };
    }

    public enum Connection {
        NORTH, SOUTH, WEST, EAST
    }
}
