package ch.neukom.advent2023.day2;

import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

public class Game {
    private final int id;
    private final List<CubeSet> cubeSets;

    public Game(int id, List<CubeSet> cubeSets) {
        this.id = id;
        this.cubeSets = cubeSets;
    }

    public int getId() {
        return id;
    }

    public boolean matches(int maxCount, Color color) {
        return cubeSets.stream()
            .map(set -> set.getCount(color))
            .filter(Objects::nonNull)
            .allMatch(actualCount -> maxCount >= actualCount);
    }

    public CubeSet getMinimalCubeSet() {
        CubeSet minimalCubeSet = new CubeSet();
        Arrays.stream(Color.values()).forEach(color -> cubeSets.stream()
            .map(cubeSet -> cubeSet.getCount(color))
            .filter(Objects::nonNull)
            .mapToInt(i -> i)
            .max()
            .ifPresent(count -> minimalCubeSet.put(count, color))
        );
        return minimalCubeSet;
    }

    public static class CubeSet {
        private final EnumMap<Color, Integer> cubes = Maps.newEnumMap(Color.class);

        public Integer getCount(Color color) {
            return cubes.get(color);
        }

        public void put(Integer count, Color color) {
            cubes.put(color, count);
        }

        public void put(CubeCount cubeCount) {
            put(cubeCount.count(), cubeCount.color());
        }

        public int getPower() {
            return cubes.values()
                .stream()
                .mapToInt(i -> i)
                .reduce((left, right) -> left * right)
                .orElse(0);
        }
    }

    public record CubeCount(Integer count, Color color) {
    }
}
