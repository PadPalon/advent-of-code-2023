package ch.neukom.advent2023.day16;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import ch.neukom.advent2023.util.InputArrayReader;
import ch.neukom.advent2023.util.InputResourceReader;

import static ch.neukom.advent2023.day16.Contraption.Direction.*;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputArrayReader reader = new InputArrayReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputArrayReader reader) {
        Contraption contraption = Util.setupContraption(reader);
        IntStream verticalLasers = IntStream.range(0, contraption.getLineCount())
            .mapMulti((line, consumer) -> {
                consumer.accept(Util.runLaser(contraption, new Contraption.Position(line, 0), EAST));
                consumer.accept(Util.runLaser(contraption, new Contraption.Position(line, contraption.getColumnCount() - 1), WEST));
            });
        IntStream horizontalLasers = IntStream.range(0, contraption.getColumnCount())
            .mapMulti((column, consumer) -> {
                consumer.accept(Util.runLaser(contraption, new Contraption.Position(0, column), SOUTH));
                consumer.accept(Util.runLaser(contraption, new Contraption.Position(contraption.getLineCount() - 1, column), NORTH));
            });
        int energizedTiles = IntStream.concat(
                verticalLasers,
                horizontalLasers
            ).max()
            .orElseThrow();
        System.out.printf("%s tiles are energized", energizedTiles);
    }
}
