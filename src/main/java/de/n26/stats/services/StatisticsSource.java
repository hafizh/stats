package de.n26.stats.services;

import com.codepoetics.protonpack.Indexed;
import de.n26.stats.domain.Statistics;
import de.n26.stats.domain.Transaction;
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

    final List<Transaction> sortedTransactions = Collections.synchronizedList(new LinkedList<>());

    @Async
    synchronized public void insertTransaction(final Transaction transaction) {
        List<Transaction> toBeRemoved = sortedTransactions.stream()
                .filter(t -> tooOld(t.getTimestamp()))
                .collect(toList());

        if(!tooOld(transaction.getTimestamp())) {
            sortedTransactions.add(getIndex(sortedTransactions, transaction.getTimestamp()), transaction);
        }

        sortedTransactions.removeAll(toBeRemoved);
    }


    int getIndex(List<Transaction> transactions, Instant timestamp) {
        return skipWhile(zipWithIndex(transactions.stream()),
                i -> i.getValue().getTimestamp().isBefore(timestamp))
                .map(Indexed::getIndex)
                .mapToInt(Long::intValue)
                .findFirst()
                .orElse(transactions.size());
    }

    public Statistics getStats() {
        List<Double> last60Seconds = sortedTransactions.stream()
                .filter(t -> !tooOld(t.getTimestamp()))
                .map(Transaction::getAmount)
                .collect(toList());

        final long count = last60Seconds.stream().collect(Collectors.counting());
        final double avg = last60Seconds.stream().collect(Collectors.averagingDouble(a -> a));
        final double max = last60Seconds.stream().max(Double::compareTo).orElse(0d);
        final double min = last60Seconds.stream().min(Double::compareTo).orElse(Double.MAX_VALUE);
        final double sum = last60Seconds.stream().collect(Collectors.summingDouble(a -> a));

        return Statistics.builder()
                .avg(avg)
                .count(count)
                .max(max)
                .min(min)
                .sum(sum)
                .build();
    }

}
