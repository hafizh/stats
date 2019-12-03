package de.n26.stats.utils;

import java.time.Duration;
import java.time.Instant;

public final class DurationUtils {

    private static final long limit = 60; // seconds

    public static boolean tooOld(Instant timestamp) {
        return Duration.between(timestamp, Instant.now()).getSeconds() > limit;
    }
}
