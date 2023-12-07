package ch.neukom.advent2023.day7;

import ch.neukom.advent2023.day7.basic.Hand;
import ch.neukom.advent2023.util.InputResourceReader;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * parts only differentiate in which sub-package they use
 */
public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        AtomicInteger rankCounter = new AtomicInteger(1);
        long totalWinning = reader.readInput()
            .map(Hand::parse)
            .sorted()
            .mapToLong(hand -> hand.getBid() * rankCounter.getAndIncrement())
            .sum();
        System.out.printf("The total winnings are %s", totalWinning);
    }
}
