package ch.neukom.advent2023.day12;

import ch.neukom.advent2023.util.InputResourceReader;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Part1 {
    private static final Pattern SPRING_PATTERN = Pattern.compile("([?.#]*) ([0-9,]*)");
    private static final Splitter COMMA_SPLITTER = Splitter.on(',');

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        long result = reader.readInput()
            .map(SPRING_PATTERN::matcher)
            .filter(Matcher::matches)
            .parallel()
            .mapToLong(matcher -> {
                String status = matcher.group(1);

                String group = matcher.group(2);
                List<Integer> groups = COMMA_SPLITTER.splitToStream(group).map(Integer::valueOf).toList();

                Set<Set<Integer>> combinations = getPossibleCombinations(status, groups);
                return combinations.stream()
                    .parallel()
                    .filter(combination -> isArrangementCorrect(combination, status, groups))
                    .count();
            })
            .sum();
        System.out.printf("The count of all possible arrangements is %s", result);
    }

    private static Set<Set<Integer>> getPossibleCombinations(String status, List<Integer> groups) {
        int operational = 0;
        Set<Integer> unknownIndexes = new HashSet<>();
        for (int i = 0; i < status.toCharArray().length; i++) {
            char c = status.toCharArray()[i];
            if (c == '#') {
                operational++;
            }
            if (c == '?') {
                unknownIndexes.add(i);
            }
        }
        int totalOperationalSprings = groups.stream().mapToInt(i -> i).sum();
        int operationalToFind = totalOperationalSprings - operational;
        return Sets.combinations(unknownIndexes, operationalToFind);
    }

    private static boolean isArrangementCorrect(Set<Integer> combination, String status, List<Integer> groups) {
        StringBuilder replacedStatusBuilder = new StringBuilder(status);
        for (Integer index : combination) {
            replacedStatusBuilder.replace(index, index + 1, "#");
        }

        boolean inGroup = false;
        int groupCount = 0;
        List<Integer> operationalGroupCounts = new ArrayList<>();
        String replacedStatus = replacedStatusBuilder.toString().replace('?', '.');
        for (int i = 0; i < replacedStatus.toCharArray().length; i++) {
            char c = replacedStatus.toCharArray()[i];
            if (!inGroup && c == '#') {
                inGroup = true;
            }
            if (inGroup && c == '#') {
                groupCount++;
            }
            if (inGroup && c == '.') {
                inGroup = false;
                operationalGroupCounts.add(groupCount);
                groupCount = 0;
            }
        }
        if (inGroup) {
            operationalGroupCounts.add(groupCount);
        }
        return operationalGroupCounts.equals(groups);
    }
}
