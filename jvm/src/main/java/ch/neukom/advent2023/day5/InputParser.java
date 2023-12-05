package ch.neukom.advent2023.day5;

import ch.neukom.advent2023.day5.CategoryMapper.Range;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

public class InputParser {
    private static final Splitter PART_SPLITTER = Splitter.on("\n\n").trimResults().omitEmptyStrings();
    private static final Splitter SPACE_SPLITTER = Splitter.on(' ').trimResults().omitEmptyStrings();

    private static final Pattern SEEDS_PATTERN = Pattern.compile("seeds: ([\\d ]*)");
    private static final Pattern MAPPER_PATTERN = Pattern.compile("(\\d*) (\\d*) (\\d*)");

    private final List<String> inputParts;

    public InputParser(String input) {
        this.inputParts = PART_SPLITTER.splitToList(input);
    }

    public List<Long> parseSeeds() {
        String seeds = inputParts.getFirst();
        Matcher seedMatcher = SEEDS_PATTERN.matcher(seeds);
        if (seedMatcher.matches()) {
            return SPACE_SPLITTER.splitToStream(seedMatcher.group(1))
                .map(Long::valueOf)
                .toList();
        } else {
            throw new IllegalArgumentException("Malformed seeds line found");
        }
    }

    public List<LongStream> parseSeedRanges() {
        String line = inputParts.getFirst();
        Matcher seedMatcher = SEEDS_PATTERN.matcher(line);
        if (seedMatcher.matches()) {
            List<String> seedNumbers = new ArrayList<>(SPACE_SPLITTER.splitToList(seedMatcher.group(1)));
            List<LongStream> seeds = new ArrayList<>();
            while (!seedNumbers.isEmpty()) {
                long start = Long.parseLong(seedNumbers.removeFirst());
                long count = Long.parseLong(seedNumbers.removeFirst());
                seeds.add(LongStream.range(start, start + count));
            }
            return seeds;
        } else {
            throw new IllegalArgumentException("Malformed line line found");
        }
    }

    public List<CategoryMapper> parseCategoryMapper() {
        List<CategoryMapper> categoryMappers = new ArrayList<>();
        for (String inputPart : inputParts) {
            String[] partLines = inputPart.split("\n");
            if (partLines.length > 1) {
                String name = partLines[0];
                List<Range> ranges = parseRanges(partLines);
                categoryMappers.add(new CategoryMapper(name, ranges));
            }
        }
        return categoryMappers;
    }

    private static List<Range> parseRanges(String[] partLines) {
        List<Range> ranges = new ArrayList<>();
        for (int i = 1; i < partLines.length; i++) {
            String partLine = partLines[i];
            Matcher mapperMatcher = MAPPER_PATTERN.matcher(partLine);
            if (mapperMatcher.matches()) {
                ranges.add(new Range(
                    Long.parseLong(mapperMatcher.group(2)),
                    Long.parseLong(mapperMatcher.group(1)),
                    Long.parseLong(mapperMatcher.group(3))
                ));
            }
        }
        return ranges;
    }
}
