package ch.neukom.advent2023.day6;

import java.util.stream.LongStream;

public record Race(long time, long recordDistance) {
    public LongStream calculatePossibleDistances() {
        return LongStream.rangeClosed(0, time).map(chargeTime -> chargeTime * (time - chargeTime));
    }

    public LongStream calculateWinningDistances() {
        return calculatePossibleDistances().filter(distance -> distance > recordDistance);
    }
}
