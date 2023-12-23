package ch.neukom.advent2023.day14;

import ch.neukom.advent2023.util.InputArrayReader;
import ch.neukom.advent2023.util.InputArrayReader.ArrayWithInfo;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Part2 {
    public static final int CYCLE_COUNT = 1000000000;

    public static void main(String[] args) throws IOException {
        try (InputArrayReader reader = new InputArrayReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputArrayReader reader) {
        ArrayWithInfo<RockType> input = Util.readRocks(reader);
        int lineCount = input.lineCount();
        int columnCount = input.columnCount();

        RockType[][] initialPositions = input.array();
        Set<RockType[][]> cycle = findCycle(initialPositions, lineCount, columnCount);
        RockType[][] finalPositions = calculateFinalPosition(initialPositions, lineCount, columnCount, cycle);
        int load = Util.calculateLoad(finalPositions, lineCount, columnCount);

        System.out.printf("The total load is %s", load);
    }

    private static Set<RockType[][]> findCycle(RockType[][] input, int lineCount, int columnCount) {
        Set<RockType[][]> cycles = new HashSet<>();
        RockType[][] positions = Util.deepCopy(input);
        Cache<String, RockType[][]> cache = CacheBuilder.newBuilder().build();
        for (int step = 0; step < CYCLE_COUNT; step++) {
            String cacheKey = getCacheKey(positions, lineCount, columnCount);
            RockType[][] cached = cache.getIfPresent(cacheKey);
            if (cached == null) {
                RockType[][] cycled = runCycle(positions, lineCount, columnCount);
                cache.put(cacheKey, cycled);
                positions = cycled;
            } else {
                if (cycles.contains(cached)) {
                    break;
                } else {
                    cycles.add(cached);
                    positions = cached;
                }
            }
        }
        return cycles;
    }

    private static String getCacheKey(RockType[][] positions, int lineCount, int columnCount) {
        StringBuilder cacheKeyBuilder = new StringBuilder();
        for (int line = 0; line < lineCount; line++) {
            for (int column = 0; column < columnCount; column++) {
                RockType rock = positions[line][column];
                switch (rock) {
                    case ROUND -> cacheKeyBuilder.append("O");
                    case CUBE -> cacheKeyBuilder.append("#");
                    case NONE -> cacheKeyBuilder.append(".");
                }
            }
        }
        return cacheKeyBuilder.toString();
    }

    private static RockType[][] calculateFinalPosition(RockType[][] input, int lineCount, int columnCount, Set<RockType[][]> cycles) {
        RockType[][] finalPositions = Util.deepCopy(input);
        for (int step = 0; step < CYCLE_COUNT; step++) {
            RockType[][] cycled = runCycle(finalPositions, lineCount, columnCount);
            finalPositions = cycled;
            if (cycles.stream().anyMatch(cycle -> Arrays.deepEquals(cycle, cycled))) {
                // skip forward through cycle until remaining steps is smaller than cycle size
                step = CYCLE_COUNT - ((CYCLE_COUNT - step) % cycles.size());
            }
        }
        return finalPositions;
    }

    private static RockType[][] runCycle(RockType[][] positions, int lineCount, int columnCount) {
        RockType[][] northTilt = Util.tiltNorth(positions, lineCount, columnCount);
        RockType[][] westTilt = Util.tiltWest(northTilt, lineCount, columnCount);
        RockType[][] southTilt = Util.tiltSouth(westTilt, lineCount, columnCount);
        return Util.tiltEast(southTilt, lineCount, columnCount);
    }
}
