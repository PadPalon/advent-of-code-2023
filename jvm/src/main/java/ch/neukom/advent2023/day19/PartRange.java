package ch.neukom.advent2023.day19;

public class PartRange {

    private final Part lowEnd;
    private final Part highEnd;
    private final String target;

    public PartRange() {
        lowEnd = new Part(1, 1, 1, 1);
        highEnd = new Part(4000, 4000, 4000, 4000);
        target = "in";
    }

    public PartRange(Part lowEnd, Part highEnd, String target) {
        this.lowEnd = lowEnd;
        this.highEnd = highEnd;
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public PartRange getMatchingRange(String attribute, Integer value, String comparison, String target) {
        if (attribute.isEmpty() && comparison.isEmpty()) {
            return new PartRange(lowEnd, highEnd, target);
        }
        if (comparison.equals("<")) {
            return new PartRange(
                lowEnd,
                limitPart(attribute, value - 1, highEnd),
                target
            );
        } else if (comparison.equals(">")) {
            return new PartRange(
                limitPart(attribute, value + 1, lowEnd),
                highEnd,
                target
            );
        } else {
            throw new IllegalStateException();
        }
    }

    public PartRange getMismatchingRange(String attribute, Integer value, String comparison, String target) {
        if (comparison.equals("<")) {
            return new PartRange(
                limitPart(attribute, value, lowEnd),
                highEnd,
                target
            );
        } else if (comparison.equals(">")) {
            return new PartRange(
                lowEnd,
                limitPart(attribute, value, highEnd),
                target
            );
        } else {
            throw new IllegalStateException();
        }
    }

    public Part limitPart(String attribute, Integer value, Part original) {
        return switch (attribute) {
            case "x" -> new Part(value, original.musicality(), original.aerodynamics(), original.shininess());
            case "m" -> new Part(original.coolness(), value, original.aerodynamics(), original.shininess());
            case "a" -> new Part(original.coolness(), original.musicality(), value, original.shininess());
            case "s" -> new Part(original.coolness(), original.musicality(), original.aerodynamics(), value);
            default -> throw new IllegalStateException();
        };
    }

    public boolean isSameRange(PartRange matchingRange) {
        return lowEnd.equals(matchingRange.lowEnd) && highEnd.equals(matchingRange.highEnd);
    }

    public long countCombinations() {
        int possibleCoolness = highEnd.coolness() - lowEnd.coolness() + 1;
        int possibleMusicality = highEnd.musicality() - lowEnd.musicality() + 1;
        int possibleAerodynamics = highEnd.aerodynamics() - lowEnd.aerodynamics() + 1;
        int possibleShininess = highEnd.shininess() - lowEnd.shininess() + 1;
        return (long) possibleCoolness
            * (long) possibleMusicality
            * (long) possibleAerodynamics
            * (long) possibleShininess;
    }
}
