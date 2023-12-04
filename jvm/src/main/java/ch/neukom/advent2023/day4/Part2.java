package ch.neukom.advent2023.day4;

import ch.neukom.advent2023.day4.Card.CardParser;
import ch.neukom.advent2023.util.InputResourceReader;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static ch.neukom.advent2023.util.collector.TransformingGathererCollector.transformAndGather;
import static com.google.common.base.Functions.identity;
import static java.util.stream.Collectors.toMap;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        HashMultiset<Card> cardPile = reader.readInput()
            .map(CardParser::parse)
            .collect(transformAndGather(HashMultiset::create, identity(), (set, card) -> set.add(card)));
        Map<Integer, Card> idMap = cardPile.elementSet().stream().collect(toMap(Card::id, identity()));
        Deque<Card> toHandle = new ArrayDeque<>(cardPile.elementSet());
        Stream.generate(toHandle::poll)
            .takeWhile(Objects::nonNull)
            .flatMapToInt(Card::getCardsWon)
            .mapToObj(idMap::get)
            .forEach(wonCard -> {
                cardPile.add(wonCard);
                toHandle.add(wonCard);
            });
        int cardCount = cardPile.entrySet().stream().mapToInt(Multiset.Entry::getCount).sum();
        System.out.printf("The pile contains %s cards", cardCount);
    }
}
