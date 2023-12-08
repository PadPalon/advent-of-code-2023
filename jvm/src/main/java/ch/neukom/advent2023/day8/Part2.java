package ch.neukom.advent2023.day8;

import ch.neukom.advent2023.util.InputResourceReader;
import ch.neukom.advent2023.util.math.MathHelper;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Part2 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part2.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        String instructions = reader.getFirstLine();
        List<Node.NodeParser> parsers = reader.readInput().skip(2)
            .map(Node.NodeParser::parse)
            .toList();

        List<Node> nodes = parsers.stream()
            .map(parser -> parser.construct(parsers))
            .toList();
        Set<Node> startNodes = nodes.stream()
            .filter(node -> node.getId().endsWith("A"))
            .collect(Collectors.toSet());
        Set<Node> endNodes = nodes.stream()
            .filter(node -> node.getId().endsWith("Z"))
            .collect(Collectors.toSet());

        /*
        lame, you just got to realize that each start node ends at exactly one end node, with no steps needed to
        enter the loop and no start nodes sharing end nodes, then it's just a search for the cycle of all loops
         */
        InstructionRepeater instructionRepeater = new InstructionRepeater(instructions);
        BigInteger stepCount = startNodes.stream()
            .map(node -> calculateStepCount(node, endNodes, instructionRepeater))
            .map(BigInteger::valueOf)
            .reduce(MathHelper::lcm)
            .orElseThrow();
        System.out.printf("Needed %s steps to reach the end", stepCount);
    }

    private static long calculateStepCount(Node startNode, Set<Node> endNodes, InstructionRepeater instructionRepeater) {
        instructionRepeater.restart();
        long stepCount = 0;
        Node currentNode = startNode;
        while (!endNodes.contains(currentNode)) {
            stepCount++;
            currentNode = switch (instructionRepeater.get()) {
                case 'L' -> currentNode.getLeft();
                case 'R' -> currentNode.getRight();
                default -> throw new IllegalArgumentException("Unexpected instruction");
            };
        }
        return stepCount;
    }
}
