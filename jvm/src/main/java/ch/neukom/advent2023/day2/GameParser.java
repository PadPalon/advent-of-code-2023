package ch.neukom.advent2023.day2;

import ch.neukom.advent2023.day2.Game.CubeCount;
import ch.neukom.advent2023.day2.Game.CubeSet;
import com.google.common.base.Splitter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ch.neukom.advent2023.util.collector.TransformingGathererCollector.transformAndGather;

public class GameParser {
    private static final Pattern LINE_PATTERN = Pattern.compile("Game (\\d+): (.*)");
    private static final Splitter SEMICOLON_SPLITTER = Splitter.on(';').trimResults().omitEmptyStrings();
    private static final Splitter COMMA_SPLITTER = Splitter.on(',').trimResults().omitEmptyStrings();

    public static Game parse(String line) {
        Matcher matcher = LINE_PATTERN.matcher(line);
        if (matcher.matches()) {
            int id = Integer.parseInt(matcher.group(1));
            List<CubeSet> cubeSets = parseCubeSet(matcher.group(2));
            return new Game(id, cubeSets);
        } else {
            throw new IllegalArgumentException("Malformed line encountered:\n%s".formatted(line));
        }
    }

    private static List<CubeSet> parseCubeSet(String line) {
        return SEMICOLON_SPLITTER.splitToStream(line)
            .map(cubes -> COMMA_SPLITTER.splitToStream(cubes)
                .collect(transformAndGather(CubeSet::new, GameParser::parseCubeCount, CubeSet::put))
            )
            .toList();
    }

    private static CubeCount parseCubeCount(String cubeCount) {
        Integer count = Integer.valueOf(cubeCount.substring(0, cubeCount.indexOf(' ')));
        String cubeColor = cubeCount.substring(cubeCount.indexOf(' ') + 1);
        Color color = Color.valueOf(cubeColor.toUpperCase());
        return new CubeCount(count, color);
    }
}
