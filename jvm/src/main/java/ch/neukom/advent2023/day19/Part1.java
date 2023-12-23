package ch.neukom.advent2023.day19;

import ch.neukom.advent2023.util.InputResourceReader;
import com.google.common.base.Functions;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Part1 {
    public static void main(String[] args) throws IOException {
        try (InputResourceReader reader = new InputResourceReader(Part1.class)) {
            run(reader);
        }
    }

    private static void run(InputResourceReader reader) {
        Map<String, Workflow> workflows = reader.readInput()
            .takeWhile(line -> !line.isEmpty())
            .map(Workflow::parse)
            .collect(Collectors.toMap(Workflow::getId, Functions.identity()));

        List<Part> parts = reader.readInput()
            .skip(workflows.size())
            .filter(line -> !line.isEmpty())
            .map(Part::parse)
            .toList();

        int acceptedRatings = 0;
        for (Part part : parts) {
            String currentWorkflow = "in";
            while (!(currentWorkflow.equals("A") || currentWorkflow.equals("R"))) {
                Workflow workflow = workflows.get(currentWorkflow);
                currentWorkflow = workflow.getResult(part);
            }
            if (currentWorkflow.equals("A")) {
                acceptedRatings += part.getRating();
            }
        }
        System.out.printf("The sum of the ratings of all accepted parts was %s%n", acceptedRatings);
    }
}
