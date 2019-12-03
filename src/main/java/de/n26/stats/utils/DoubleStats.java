package de.n26.stats.utils;

import de.n26.stats.domain.Statistics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Collector;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoubleStats {
    long count = 0L;
    Double min = 0d;
    Double max = 0d;
    Double avg = 0d;
    Double sum = 0d;

    public Statistics toStatistics() {
        return Statistics.builder()
                .avg(avg)
                .count(count)
                .max(max)
                .min(min)
                .sum(sum)
                .build();
    }

    void accept(Double val) {
        min = Math.min(val, min);
        max = Math.max(val, max);
        sum += val;
        count++;
        avg = sum / count;
    }

    DoubleStats combine(DoubleStats that) {
        if (this.count == 0) return that;
        if (that.count == 0) return this;

        this.min = Math.min(this.min, that.min);
        this.max = Math.max(this.max, that.max);
        this.sum += that.sum;
        this.avg = newAvg(that);
        this.count += that.count;
        return this;
    }

    private double newAvg(DoubleStats that) {
        return ((this.avg * this.count) + (that.avg * that.count)) / (this.count + that.count);
    }

    public static Collector<Double, DoubleStats, DoubleStats> collector() {
        return Collector.of(
                DoubleStats::new,
                DoubleStats::accept,
                DoubleStats::combine,
                Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH);
    }
}
