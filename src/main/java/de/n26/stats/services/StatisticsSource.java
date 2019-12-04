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

import java.time.Instant;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.PriorityBlockingQueue;

import static de.n26.stats.utils.DurationUtils.tooOld;

@Service
public class StatisticsSource {

    private Logger logger = LoggerFactory.getLogger(StatisticsSource.class);

    // @VisibleForTesting
    final Queue<Transaction> transactionsQueue =
      new PriorityBlockingQueue<>(1000, Comparator.comparing(Transaction::getTimestamp));

    final Map<Long, DoubleStats> statsBySeconds = new ConcurrentSkipListMap<>();

    @Async
    public void insertTransaction(@NonNull final Transaction transaction) {
        if(!tooOld(transaction.getTimestamp())) {
            long key = transaction.getTimestamp().getEpochSecond();
            DoubleStats newStats = new DoubleStats(transaction.getAmount());

            statsBySeconds.merge(key, newStats, DoubleStats::combine);
        } else {
            logger.debug("Not inserted, too old: {}", transaction);
        }
    }

    @NonNull
    public Statistics getStats() {
        return statsBySeconds.values().parallelStream()
                .collect(DoubleStats.collector())
                .toStatistics();
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void cleanOldTransactions() {
        long now = Instant.now().getEpochSecond();
        logger.debug("Starting clean up of old stats");

        Optional<DoubleStats> removed = Optional.ofNullable(statsBySeconds.remove(now - 60));

        if(removed.isPresent()) logger.debug("Cleaning up oldest: {} from {}, {}", now, now - 60, removed.get());
        else logger.debug("Nothing to clean");
    }
}
