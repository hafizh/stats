package de.n26.stats.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public final class DurationUtils {

    private static final long limit = TimeUnit.MINUTES.toSeconds(1);

    public static boolean tooOld(Instant timestamp) {
        return limit < Duration.between(timestamp, Instant.now()).getSeconds();
    }
}
