package ch.neukom.advent2023.day7.joker;

import java.util.ArrayList;
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

    public boolean matches(List<Integer> counts, int jokerCount) {
        ArrayList<Integer> copiedCounts = new ArrayList<>(counts);
        if (jokerCount == 5) { // all jokers, what's the odd? these can be any type, so just return true
            return true;
        } else if (jokerCount > 0) {
            /*
             * we just add the jokers to the first count
             * since there's no flushes and straights in this game, there's never a better option than just having more
             * of card you already have the most of
             */
            Integer firstCount = copiedCounts.removeFirst();
            copiedCounts.addFirst(firstCount + jokerCount);
        }
        List<Integer> limitedCounts = copiedCounts.subList(0, cardCounts.size());
        return cardCounts.equals(limitedCounts);
    }
}
