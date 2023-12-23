package ch.neukom.advent2023.day16;

import ch.neukom.advent2023.day16.Contraption.Direction;
import ch.neukom.advent2023.day16.Contraption.Position;

public class Laser {
    private Position position;

    private Direction direction;

    public Laser(Position position, Direction direction) {
        this.position = position;
        this.direction = direction;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Laser move(Position newPosition) {
        return new Laser(newPosition, direction);
    }

    public Laser split(Direction newDirection) {
        return new Laser(position, newDirection);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Laser laser = (Laser) o;

        if (!position.equals(laser.position)) return false;
        return direction == laser.direction;
    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + direction.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "%s/%s -> %s".formatted(position.line(), position.column(), direction.name());
    }
}
