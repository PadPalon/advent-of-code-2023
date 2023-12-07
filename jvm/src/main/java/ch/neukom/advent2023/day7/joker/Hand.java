package ch.neukom.advent2023.day7.joker;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Streams;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class Hand implements Comparable<Hand> {
    private final List<Card> cards;
    private final Multiset<Card> cardCounts;
    private final int jokerCount;
    private final Type type;
    private final long bid;

    public Hand(List<Card> cards, long bid) {
        this.cards = cards;
        this.cardCounts = HashMultiset.create(cards);
        this.jokerCount = this.cardCounts.count(Card.JOKER);
        this.type = calculateType();
        this.bid = bid;
    }

    public static Hand parse(String line) {
        String[] parts = line.split(" ");
        List<Card> cards = parts[0].chars()
            .mapToObj(c -> (char) c)
            .map(String::valueOf)
            .map(Card::getByRepresentation)
            .map(Optional::orElseThrow)
            .toList();
        long bid = Long.parseLong(parts[1]);
        return new Hand(cards, bid);
    }

    private Type calculateType() {
        List<Integer> counts = cardCounts.entrySet()
            .stream()
            .filter(entry -> entry.getElement() != Card.JOKER)
            .map(Multiset.Entry::getCount)
            .sorted(Comparator.reverseOrder())
            .toList();
        Optional<Type> first = Arrays.stream(Type.values())
            .sorted(Comparator.reverseOrder())
            .filter(type -> type.matches(counts, jokerCount))
            .findFirst();
        return first
            .orElseThrow();
    }

    public Type getType() {
        return type;
    }

    public long getBid() {
        return bid;
    }

    @Override
    public String toString() {
        return "%s (%s) - %s".formatted(
            type.name(),
            cards.stream().map(Card::getRepresentation).collect(joining()),
            bid
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hand hand = (Hand) o;

        if (bid != hand.bid) return false;
        return cards.equals(hand.cards);
    }

    @Override
    public int hashCode() {
        int result = cards.hashCode();
        result = 31 * result + (int) (bid ^ (bid >>> 32));
        return result;
    }

    @Override
    public int compareTo(Hand other) {
        if (type == other.getType()) {
            return Streams.zip(cards.stream(), other.cards.stream(), Enum::compareTo)
                .filter(comparison -> comparison != 0)
                .findFirst()
                .orElse(0);
        } else {
            return type.compareTo(other.getType());
        }
    }
}
