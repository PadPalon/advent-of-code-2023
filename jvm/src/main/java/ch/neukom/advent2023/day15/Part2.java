package ch.neukom.advent2023.day15;

import ch.neukom.advent2023.util.InputResourceReader;
import com.google.common.base.Splitter;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Streams;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;

public class Part2 {
    private static final Splitter COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();
    private static final Pattern STEP_PATTERN = Pattern.compile("([a-z]*)(=[1-9]|-)");

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        ListMultimap<Integer, Lens> boxes = MultimapBuilder.hashKeys(255).arrayListValues().build();
        reader.readInput().collect(collectingAndThen(joining(), COMMA_SPLITTER::splitToStream))
            .map(STEP_PATTERN::matcher)
            .filter(Matcher::matches)
            .forEach(matcher -> executeStep(matcher, boxes));
        long result = boxes.asMap().entrySet()
            .stream()
            .mapToLong(Part2::calculateBoxStrength)
            .sum();
        System.out.printf("The focusing power is %s", result);
    }

    private static void executeStep(Matcher matcher, ListMultimap<Integer, Lens> boxes) {
        String label = matcher.group(1);
        int hash = Hash.calculateHash(label);
        String action = matcher.group(2);
        if (action.contains("=")) {
            addLens(label, action, boxes, hash);
        } else if (action.contains("-")) {
            boxes.remove(hash, new Lens(label));
        }
    }

    private static void addLens(String label, String action, ListMultimap<Integer, Lens> boxes, int hash) {
        Lens newLens = new Lens(label, action.charAt(action.length() - 1) - '0');
        if (boxes.containsEntry(hash, newLens)) {
            List<Lens> existingLenses = boxes.get(hash);
            existingLenses.replaceAll(existingLens -> {
                if (existingLens.equals(newLens)) {
                    return newLens;
                } else {
                    return existingLens;
                }
            });
        } else {
            boxes.put(hash, newLens);
        }
    }

    private static long calculateBoxStrength(Map.Entry<Integer, Collection<Lens>> entry) {
        Integer boxNumber = entry.getKey();
        return Streams.mapWithIndex(
                entry.getValue().stream(),
                (lens, index) -> calculateLensStrength(lens, index, boxNumber)
            )
            .mapToLong(i -> i)
            .sum();
    }

    private static long calculateLensStrength(Lens lens, long index, Integer boxNumber) {
        return (1 + boxNumber) * (index + 1) * lens.getStrength();
    }
}
