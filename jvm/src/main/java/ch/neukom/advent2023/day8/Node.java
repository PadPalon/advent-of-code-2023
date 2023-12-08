package ch.neukom.advent2023.day8;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Node {
    private final String id;

    private Node left;
    private Node right;

    private Node(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public void connectLeft(Node node) {
        this.left = node;
    }

    public void connectRight(Node node) {
        this.right = node;
    }

    @Override
    public String toString() {
        return "%s - (%s, %s)".formatted(id, left.id, right.id);
    }

    public static class NodeParser {
        private static final Pattern NODE_PATTERN = Pattern.compile("(.{3}) = \\((.{3}), (.{3})\\)");

        private final Node node;
        private final String left;
        private final String right;

        public NodeParser(String id, String left, String right) {
            this.node = new Node(id);
            this.left = left;
            this.right = right;
        }

        public static NodeParser parse(String line) {
            Matcher matcher = NODE_PATTERN.matcher(line);
            if (matcher.matches()) {
                String id = matcher.group(1);
                String left = matcher.group(2);
                String right = matcher.group(3);
                return new NodeParser(id, left, right);
            } else {
                throw new IllegalArgumentException("Malformed node encountered");
            }
        }

        public Node construct(List<NodeParser> nodeParsers) {
            node.connectLeft(findNode(nodeParsers, left));
            node.connectRight(findNode(nodeParsers, right));
            return node;
        }

        private Node findNode(List<NodeParser> nodeParsers, String target) {
            return nodeParsers.stream()
                .map(parser -> parser.node)
                .filter(node -> node.getId().equals(target))
                .findAny()
                .orElseThrow();
        }
    }
}
