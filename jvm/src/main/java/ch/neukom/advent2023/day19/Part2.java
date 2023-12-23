package ch.neukom.advent2023.day19;

import ch.neukom.advent2023.util.InputResourceReader;
import com.google.common.base.Functions;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.stream.Collectors;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        Map<String, Workflow> workflows = reader.readInput()
            .takeWhile(line -> !line.isEmpty())
            .map(Workflow::parse)
            .collect(Collectors.toMap(Workflow::getId, Functions.identity()));

        long possibleCombinations = 0;
        Deque<PartRange> ranges = new ArrayDeque<>();
        ranges.add(new PartRange());
        while (!ranges.isEmpty()) {
            PartRange range = ranges.pop();
            if (range.getTarget().equals("A")) {
                possibleCombinations += range.countCombinations();
            } else if (!range.getTarget().equals("R")) {
                Workflow workflow = workflows.get(range.getTarget());
                ranges.addAll(workflow.analyze(range));
            }
        }
        System.out.printf("%s possible cominbations found%n", possibleCombinations);
    }
}
