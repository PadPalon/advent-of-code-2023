package ch.neukom.advent2023.day5;

import java.util.List;

public class CategoryMapper {
    private final String name;
    private final List<Range> ranges;

    public CategoryMapper(String name, List<Range> ranges) {
        this.name = name;
        this.ranges = ranges;
    }

    public long map(long value) {
        for (Range range : ranges) {
            if (range.contains(value)) {
                return range.map(value);
            }
        }
        return value;
    }

    public record Range(long sourceStart, long destinationStart, long length) {
        public boolean contains(long value) {
            return sourceStart <= value && sourceStart + length > value;
        }

        public long map(long value) {
            long distance = value - sourceStart;
            return destinationStart + distance;
        }
    }
}
