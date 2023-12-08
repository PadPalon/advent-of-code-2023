package ch.neukom.advent2023.day8;

import ch.neukom.advent2023.day8.Node.NodeParser;
import ch.neukom.advent2023.util.InputResourceReader;

import java.io.IOException;
import java.util.List;

public class Part1 {
    public static final String START_NODE_ID = "AAA";
    public static final String END_NODE_ID = "ZZZ";

    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        String instructions = reader.getFirstLine();
        List<NodeParser> parsers = reader.readInput().skip(2)
            .map(NodeParser::parse)
            .toList();

        List<Node> nodes = parsers.stream()
            .map(parser -> parser.construct(parsers))
            .toList();
        Node startNode = nodes.stream()
            .filter(node -> node.getId().equals(START_NODE_ID))
            .findAny()
            .orElseThrow();
        Node endNode = nodes.stream()
            .filter(node -> node.getId().equals(END_NODE_ID))
            .findAny()
            .orElseThrow();

        InstructionRepeater instructionRepeater = new InstructionRepeater(instructions);
        int stepCount = 0;
        Node currentNode = startNode;
        while (currentNode != endNode) {
            stepCount++;
            currentNode = switch (instructionRepeater.get()) {
                case 'L' -> currentNode.getLeft();
                case 'R' -> currentNode.getRight();
                default -> throw new IllegalArgumentException("Unexpected instruction");
            };
        }
        System.out.printf("Needed %s steps to reach the end", stepCount);
    }
}
