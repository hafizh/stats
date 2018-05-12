package de.n26.stats.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class DurationUtilsTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        final Instant now = Instant.now();
        return Arrays.asList(new Object[][]{
                {now, false},
                {now.plusMillis(10), false},
                {now.plusSeconds(4), false},
                {now.minusSeconds(30), false},
                {now.minusSeconds(70), true}
        });
    }

    private Instant input;
    private boolean expected;
    public DurationUtilsTest(Instant in, boolean ex) {
        input = in;
        expected = ex;
    }

    @Test
    public void tooOld() {
        assertThat(DurationUtils.tooOld(input), is(expected));
    }
}