package ch.neukom.advent2023.day2;

import ch.neukom.advent2023.util.InputResourceReader;

import java.io.IOException;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        int sumOfIds = reader.readInput()
            .map(GameParser::parse)
            .filter(game -> game.matches(12, Color.RED))
            .filter(game -> game.matches(13, Color.GREEN))
            .filter(game -> game.matches(14, Color.BLUE))
            .mapToInt(Game::getId)
            .sum();
        System.out.printf("The sum of ids of possible games is %s", sumOfIds);
    }
}
