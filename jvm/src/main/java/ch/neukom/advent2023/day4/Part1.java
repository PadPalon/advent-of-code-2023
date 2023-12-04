package ch.neukom.advent2023.day4;

import ch.neukom.advent2023.day4.Card.CardParser;
import ch.neukom.advent2023.util.InputResourceReader;

import java.io.IOException;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        double sum = reader.readInput()
            .map(CardParser::parse)
            .mapToDouble(Card::getPoints)
            .sum();
        System.out.printf("The pile of cards is worth %s points", sum);
    }
}
