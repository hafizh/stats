package de.n26.stats.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(BlockJUnit4ClassRunner.class)
public class DoubleStatsTest {

    @Test
    public void accept() {
        DoubleStats input = new DoubleStats();
        input.accept(12.5);
        input.accept(13d);
        input.accept(15d);

        assertThat(input, is(new DoubleStats(3, 12.5, 15d, 13.5, 40.5)));
    }

    @Test
    public void combineEmptyAndOne() {
        DoubleStats result = new DoubleStats().combine(new DoubleStats(1, 12.5, 12.5, 12.5, 12.5));
        assertThat(result, is(new DoubleStats(1, 12.5, 12.5, 12.5, 12.5)));
    }

    @Test
    public void combine() {
        DoubleStats result = new DoubleStats(2, 13d, 15d, 14d, 28d).combine(new DoubleStats(1, 12.5, 12.5, 12.5, 12.5));
        assertThat(result, is(new DoubleStats(3, 12.5, 15d, 13.5, 40.5)));
    }
}