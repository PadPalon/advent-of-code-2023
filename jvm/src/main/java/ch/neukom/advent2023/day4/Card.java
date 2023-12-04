package ch.neukom.advent2023.day4;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public record Card(int id, List<Integer> winningNumbers, List<Integer> actualNumbers) {
    public double getPoints() {
        long matches = actualNumbers.stream()
            .filter(winningNumbers::contains)
            .count();
        return matches > 0 ? Math.pow(2, matches - 1) : 0;
    }

    public IntStream getCardsWon() {
        int matches = (int) actualNumbers.stream()
            .filter(winningNumbers::contains)
            .count();
        return IntStream.range(0, matches).map(Math::incrementExact).map(l -> l + id);
    }

    public static class CardParser {
        private static final Pattern CARD_PATTERN = Pattern.compile("Card\\s*(\\d*): ([\\d ]*) \\| ([\\d ]*)");
        private static final Splitter SPACE_SPLITTER = Splitter.on(' ').trimResults().omitEmptyStrings();

        private CardParser() {
        }

        public static Card parse(String line) {
            Matcher cardMatcher = CARD_PATTERN.matcher(line);
            if (cardMatcher.matches()) {
                int id = Integer.parseInt(cardMatcher.group(1));
                List<Integer> winningNumbers = parseNumberList(cardMatcher.group(2));
                List<Integer> actualNumbers = parseNumberList(cardMatcher.group(3));
                return new Card(id, winningNumbers, actualNumbers);
            } else {
                throw new IllegalArgumentException("Malformed line encountered:\n%s".formatted(line));
            }
        }

        private static List<Integer> parseNumberList(String numbers) {
            return SPACE_SPLITTER.splitToStream(numbers)
                .map(Integer::valueOf)
                .toList();
        }
    }
}
