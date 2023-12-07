package ch.neukom.advent2023.day7.joker;

import java.util.Arrays;
import java.util.Optional;

public enum Card {
    JOKER("J"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("T"),
    QUEEN("Q"),
    KING("K"),
    ACE("A");

    private final String representation;

    Card(String representation) {
        this.representation = representation;
    }

    public static Optional<Card> getByRepresentation(String value) {
        return Arrays.stream(Card.values()).filter(c -> c.representation.equals(value)).findAny();
    }

    public String getRepresentation() {
        return representation;
    }
}
