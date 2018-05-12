package de.n26.stats.services;

import de.n26.stats.domain.Statistics;
import de.n26.stats.domain.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(BlockJUnit4ClassRunner.class)
public class StatisticsSourceTest {

    private StatisticsSource statisticsSource;

    @Before
    public void init() {
        statisticsSource = new StatisticsSource();

        Instant now = Instant.now();
        statisticsSource.insertTransaction(new Transaction(13d, now));
        statisticsSource.insertTransaction(new Transaction(12d, now.minusSeconds(10)));
        statisticsSource.insertTransaction(new Transaction(11d, now.minusSeconds(15)));
        statisticsSource.insertTransaction(new Transaction(14d, now.plusMillis(20)));
        statisticsSource.insertTransaction(new Transaction(10d, now.minusSeconds(61)));
    }

    @Test
    public void insertTransaction() {
        assertThat(statisticsSource.sortedTransactions.size(), is(4));
        assertThat(statisticsSource.sortedTransactions.iterator().next().getAmount(), is(11d));
    }


    @Test
    public void getStats() {
        assertThat(statisticsSource.getStats(), is(new Statistics(50d, 12.5, 14d, 11d, 4l)));
    }

    @Test
    public void getIndexFromTheMiddle() {
        Instant now = Instant.now();
        List<Transaction> transactions = Arrays.asList(new Transaction(1d, now.minusSeconds(15)), new Transaction(2d, now));
        assertThat(statisticsSource.getIndex(transactions, now.minusSeconds(10)), is(1));
    }

    @Test
    public void getIndexFromHead() {
        Instant now = Instant.now();
        List<Transaction> transactions = Arrays.asList(new Transaction(1d, now.minusSeconds(15)), new Transaction(2d, now));
        assertThat(statisticsSource.getIndex(transactions, now.minusSeconds(20)), is(0));
    }

    @Test
    public void getIndexFromTail() {
        Instant now = Instant.now();
        List<Transaction> transactions = Arrays.asList(new Transaction(1d, now.minusSeconds(15)), new Transaction(2d, now));
        assertThat(statisticsSource.getIndex(transactions, now.plusSeconds(10)), is(2));
    }
}