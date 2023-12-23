package ch.neukom.advent2023.day19;

import com.google.common.base.Splitter;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Workflow {
    private static final Pattern WORKFLOW_PATTERN = Pattern.compile("([a-z]*)\\{(.*)}");
    private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("([a-zAR]*)(?:([<>])([0-9]*):([a-zAR]*))?");

    private static final Splitter INSTRUCTION_SPLITTER = Splitter.on(',');

    private final String id;
    private final List<Instruction> instructions;

    public Workflow(String id, List<Instruction> instructions) {
        this.id = id;
        this.instructions = instructions;
    }

    public static Workflow parse(String line) {
        Matcher matcher = WORKFLOW_PATTERN.matcher(line);
        if (matcher.matches()) {
            String id = matcher.group(1);
            List<Instruction> instructions = INSTRUCTION_SPLITTER.splitToStream(matcher.group(2))
                .map(INSTRUCTION_PATTERN::matcher)
                .filter(Matcher::matches)
                .map(instruction -> {
                    if (instruction.group(0).contains(":")) {
                        String attribute = instruction.group(1);
                        String comparison = instruction.group(2);
                        Integer value = Integer.parseInt(instruction.group(3));
                        String target = instruction.group(4);
                        return new Instruction(attribute, comparison, value, target);
                    } else {
                        String target = instruction.group(1);
                        return new Instruction(target);
                    }
                })
                .toList();
            return new Workflow(id, instructions);
        } else {
            throw new IllegalArgumentException("Unexpected workflow %s".formatted(line));
        }
    }

    public String getId() {
        return id;
    }

    public String getResult(Part part) {
        return instructions.stream()
            .map(instruction -> instruction.execute(part))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .orElseThrow();
    }

    public Set<PartRange> analyze(PartRange range) {
        PartRange currentRange = range;
        Set<PartRange> ranges = new HashSet<>();
        for (Instruction instruction : instructions) {
            PartRange matchingRange = currentRange.getMatchingRange(
                instruction.getAttribute(),
                instruction.getValue(),
                instruction.getComparison(),
                instruction.getTarget()
            );
            ranges.add(matchingRange);
            if (currentRange.isSameRange(matchingRange)) {
                break;
            } else {
                currentRange = currentRange.getMismatchingRange(
                    instruction.getAttribute(),
                    instruction.getValue(),
                    instruction.getComparison(),
                    instruction.getTarget()
                );
            }
        }
        return ranges;
    }

    public static class Instruction {
        private final Function<Part, Integer> attributeLoader;
        private final BiPredicate<Integer, Integer> comparator;
        private final int value;
        private final String target;

        private final String attribute;
        private final String comparison;

        public Instruction(String target) {
            this.target = target;

            this.attribute = "";
            this.comparison = "";

            this.attributeLoader = part -> 0;
            this.comparator = (l, r) -> true;
            this.value = 0;
        }

        public Instruction(String attribute, String comparison, Integer value, String target) {
            this.value = value;
            this.target = target;

            this.attribute = attribute;
            this.comparison = comparison;

            this.attributeLoader = switch (attribute) {
                case "x" -> Part::coolness;
                case "m" -> Part::musicality;
                case "a" -> Part::aerodynamics;
                case "s" -> Part::shininess;
                default -> throw new IllegalArgumentException("Unexpected attribute %s".formatted(attribute));
            };
            this.comparator = switch (comparison) {
                case ">" -> (l, r) -> l > r;
                case "<" -> (l, r) -> l < r;
                default -> throw new IllegalArgumentException("Unexpected comparison %s".formatted(comparison));
            };
        }

        public Optional<String> execute(Part part) {
            if (matches(part)) {
                return Optional.of(target);
            } else {
                return Optional.empty();
            }
        }

        private boolean matches(Part part) {
            return comparator.test(attributeLoader.apply(part), value);
        }

        public int getValue() {
            return value;
        }

        public String getTarget() {
            return target;
        }

        public String getAttribute() {
            return attribute;
        }

        public String getComparison() {
            return comparison;
        }
    }
}
