package ch.neukom.advent2023.day7.basic;

import java.util.List;

public enum Type {
    HIGH_CARD(1),
    ONE_PAIR(2),
    TWO_PAIR(2, 2),
    THREE_OF_A_KIND(3),
    FULL_HOUSE(3, 2),
    FOUR_OF_A_KIND(4),
    FIVE_OF_A_KIND(5);

    private final List<Integer> cardCounts;

    Type(Integer... cardCounts) {
        this.cardCounts = List.of(cardCounts);
    }

    public boolean matches(List<Integer> counts) {
        List<Integer> limitedCounts = counts.subList(0, cardCounts.size());
        return cardCounts.equals(limitedCounts);
    }
}
