package ch.neukom.advent2023.day6;

import ch.neukom.advent2023.util.InputResourceReader;

import java.io.IOException;
import java.util.List;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        List<String> lines = reader.readInput().toList();
        long time = Long.parseLong(lines.get(0).substring(5).replace(" ", ""));
        long distance = Long.parseLong(lines.get(1).substring(9).replace(" ", ""));
        Race race = new Race(time, distance);
        long waysToWin = race.calculateWinningDistances().count();
        System.out.printf("The amount of ways to win is %s", waysToWin);
    }
}
