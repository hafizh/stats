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
    Double min = Double.MAX_VALUE;
    Double max = 0d;
    Double avg = 0d;
    Double sum = 0d;

    public DoubleStats(double val) {
        min = Math.min(val, min);
        max = Math.max(val, max);
        sum += val;
        count++;
        avg = sum / count;
    }

    public Statistics toStatistics() {
        return Statistics.builder()
                .avg(avg)
                .count(count)
                .max(max)
                .min(min)
                .sum(sum)
                .build();
    }

    void accept(DoubleStats that) {
        min = Math.min(this.min, that.min);
        max = Math.max(this.max, that.max);
        sum += that.sum;
        avg = newAvg(avg, count, that.avg, that.count);
        count += that.count;
    }

    public DoubleStats combine(DoubleStats that) {
        if (this.count == 0) return that;
        if (that.count == 0) return this;

        return new DoubleStats(
          this.count + that.count,
          Math.min(this.min, that.min),
          Math.max(this.max, that.max),
          newAvg(this.avg, this.count, that.avg, that.count),
          this.sum + that.sum);
    }

    private double newAvg(double avg1, long count1, double avg2, long count2) {
        return ((avg1 * count1) + (avg2 * count2)) / (count1 + count2);
    }

    public static Collector<DoubleStats, DoubleStats, DoubleStats> collector() {
        return Collector.of(
                DoubleStats::new,
                DoubleStats::accept,
                DoubleStats::combine,
                Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH);
    }
}
