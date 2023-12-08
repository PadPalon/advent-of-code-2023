package ch.neukom.advent2023.day8;

import java.util.function.Supplier;

public class InstructionRepeater implements Supplier<Character> {
    private final String instructions;

    private int position = 0;

    public InstructionRepeater(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public Character get() {
        char c = instructions.charAt(position);
        position = (position + 1) % instructions.length();
        return c;
    }

    public void restart() {
        this.position = 0;
    }
}
