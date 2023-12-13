package ch.neukom.advent2023.day12;

import ch.neukom.advent2023.util.InputResourceReader;
import com.google.common.base.Splitter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

public class Part2 {
    private static final Cache<String, Long> CACHE = CacheBuilder.newBuilder().build();

    private static final Pattern SPRING_PATTERN = Pattern.compile("([?.#]*) ([0-9,]*)");
    private static final Splitter COMMA_SPLITTER = Splitter.on(',');

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        long result = reader.readInput()
            .map(SPRING_PATTERN::matcher)
            .filter(Matcher::matches)
            .map(Part2::getData)
            .mapToLong(Part2::getFromCacheOrCalculate)
            .sum();
        System.out.printf("The count of all possible arrangements is %s", result);
    }

    private static Data getData(Matcher matcher) {
        String statusSource = matcher.group(1);
        String status = String.join("?", statusSource, statusSource, statusSource, statusSource, statusSource);

        String groupSource = matcher.group(2);
        String group = String.join(",", groupSource, groupSource, groupSource, groupSource, groupSource);
        List<Integer> groups = COMMA_SPLITTER.splitToStream(group).map(Integer::valueOf).toList();
        return new Data(status, groups);
    }

    private static long getFromCacheOrCalculate(Data data) {
        try {
            String status = data.status();
            List<Integer> groups = data.groups();
            String cacheKey = "%s/%s".formatted(status, groups.stream().map(String::valueOf).collect(joining("-")));
            return CACHE.get(cacheKey, () -> countValidArrangement(status, groups));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private static long countValidArrangement(String status, List<Integer> groups) {
        if (groups.isEmpty()) {
            if (status.contains("#")) {
                // no groups left but operational springs still available
                return 0L;
            } else {
                // arrangement valid, only broken or unknown springs left
                return 1L;
            }
        }

        if (status.isEmpty()) {
            // more groups still available but no springs left
            return 0L;
        }

        char currentCharacter = status.charAt(0);
        if (currentCharacter == '.') {
            // ignore broken spring
            return countValidArrangement(status.substring(1), groups);
        } else if (currentCharacter == '?') {
            return checkVariations(status, groups);
        } else if (currentCharacter == '#') {
            return checkGroup(status, groups);
        } else {
            throw new IllegalArgumentException("Unknown character %s".formatted(currentCharacter));
        }
    }

    private static long checkVariations(String status, List<Integer> groups) {
        return countValidArrangement(status.replaceFirst("\\?", "#"), groups)
            + countValidArrangement(status.replaceFirst("\\?", "."), groups);
    }

    private static long checkGroup(String status, List<Integer> groups) {
        int groupSize = groups.get(0);
        int statusSize = status.length();
        if (statusSize < groupSize) {
            // not enough springs left
            return 0L;
        } else if (statusSize == groupSize) {
            return checkEnd(status, groups, groupSize);
        } else {
            return checkRange(status, groups, groupSize);
        }
    }

    private static long checkEnd(String status, List<Integer> groups, int groupSize) {
        // available springs are exactly same length as group

        if (groups.size() > 1) {
            // more groups still available
            return 0L;
        }

        String potentialGroup = status.substring(0, groupSize);
        if (potentialGroup.contains(".")) {
            // a spring in contiguous group is broken
            return 0L;
        } else {
            return 1L;
        }
    }

    private static long checkRange(String status, List<Integer> groups, int groupSize) {
        String potentialGroup = status.substring(0, groupSize);
        char delimiter = status.charAt(groupSize);
        if (potentialGroup.contains(".") || delimiter == '#') {
            // a spring in contiguous group is broken or group is not delimited
            return 0L;
        } else {
            List<Integer> nextGroups = new ArrayList<>(groups);
            nextGroups.removeFirst();
            // skip group springs and check rest
            return countValidArrangement(status.substring(groupSize + 1), nextGroups);
        }
    }

    record Data(String status, List<Integer> groups) {
    }
}
