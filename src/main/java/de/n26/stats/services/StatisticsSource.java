package de.n26.stats.services;

import de.n26.stats.domain.Statistics;
import de.n26.stats.domain.Transaction;
import de.n26.stats.utils.DoubleStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import static de.n26.stats.utils.DurationUtils.tooOld;

@Service
public class StatisticsSource {

    private Logger logger = LoggerFactory.getLogger(StatisticsSource.class);

    // @VisibleForTesting
    final Queue<Transaction> transactionsQueue =
      new PriorityBlockingQueue<>(1000, Comparator.comparing(Transaction::getTimestamp));

    @Async
    public void insertTransaction(@NonNull final Transaction transaction) {
        if(!tooOld(transaction.getTimestamp())) {
            transactionsQueue.add(transaction);
        } else {
            logger.debug("Not inserted, too old: {}", transaction);
        }
    }

    @NonNull
    public Statistics getStats() {
        return transactionsQueue.stream()
                .map(Transaction::getAmount)
                .collect(DoubleStats.collector())
                .toStatistics();
    }

    @Scheduled(fixedDelay = 1000)
    public void cleanOldTransactions() {
        logger.debug("Starting clean up of old transactions");

        while(transactionsQueue.peek() != null && tooOld(transactionsQueue.peek().getTimestamp())) {
            logger.debug("Removing too old: {}", transactionsQueue.poll());
        }
    }
}
