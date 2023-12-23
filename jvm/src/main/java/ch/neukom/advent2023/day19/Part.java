package ch.neukom.advent2023.day19;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Part(int coolness, int musicality, int aerodynamics, int shininess) {
    private static final Pattern PART_PATTERN = Pattern.compile("\\{x=([0-9]*),m=([0-9]*),a=([0-9]*),s=([0-9]*)}");

    public static Part parse(String line) {
        Matcher matcher = PART_PATTERN.matcher(line);
        if (matcher.matches()) {
            return new Part(
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4))
            );
        } else {
            throw new IllegalArgumentException("Unexpected part %s".formatted(line));
        }
    }

    public int getRating() {
        return coolness + musicality + aerodynamics + shininess;
    }
}
