package ch.neukom.advent2023.day2;

import ch.neukom.advent2023.util.InputResourceReader;

import java.io.IOException;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        int sumOfIds = reader.readInput()
            .map(GameParser::parse)
            .map(Game::getMinimalCubeSet)
            .mapToInt(Game.CubeSet::getPower)
            .sum();
        System.out.printf("The sum of powers of minimal cube set per game is %s", sumOfIds);
    }
}
