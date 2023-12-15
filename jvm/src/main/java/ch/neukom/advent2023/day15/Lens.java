package ch.neukom.advent2023.day15;

public class Lens {
    private final String label;
    private final int strength;

    public Lens(String label) {
        this(label, -1);
    }

    public Lens(String label, int strength) {
        this.label = label;
        this.strength = strength;
    }

    public int getStrength() {
        return strength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lens lens = (Lens) o;

        return label.equals(lens.label);
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }

    @Override
    public String toString() {
        return "%s -> %s".formatted(label, strength);
    }
}
