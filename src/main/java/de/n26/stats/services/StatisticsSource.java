package de.n26.stats.services;

import com.codepoetics.protonpack.Indexed;
import de.n26.stats.domain.Statistics;
import de.n26.stats.domain.Transaction;
import de.n26.stats.utils.DoubleStats;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codepoetics.protonpack.StreamUtils.skipWhile;
import static com.codepoetics.protonpack.StreamUtils.zipWithIndex;
import static de.n26.stats.utils.DurationUtils.tooOld;
import static java.util.stream.Collectors.toList;

@Service
public class StatisticsSource {

    // @VisibleForTesting
    final List<Transaction> sortedTransactions = Collections.synchronizedList(new LinkedList<>());

    @Async
    synchronized public void insertTransaction(@NonNull final Transaction transaction) {
        List<Transaction> toBeRemoved = sortedTransactions.stream()
                .filter(t -> tooOld(t.getTimestamp()))
                .collect(toList());

        if(!tooOld(transaction.getTimestamp())) {
            sortedTransactions.add(getIndex(sortedTransactions, transaction.getTimestamp()), transaction);
        }

        sortedTransactions.removeAll(toBeRemoved);
    }

    @NonNull
    synchronized public Statistics getStats() {
        return skipWhile(sortedTransactions.stream(), t -> tooOld(t.getTimestamp()))
                .map(Transaction::getAmount)
                .collect(DoubleStats.collector())
                .toStatistics();
    }

    // @VisibleForTesting
    int getIndex(List<Transaction> transactions, Instant timestamp) {
        return skipWhile(zipWithIndex(transactions.stream()),
                i -> i.getValue().getTimestamp().isBefore(timestamp))
                .map(Indexed::getIndex)
                .mapToInt(Long::intValue)
                .findFirst()
                .orElse(transactions.size());
    }

}
