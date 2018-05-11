package de.n26.stats.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Statistics {
    private Double sum; // is a double specifying the total sum of transaction value in the last 60 seconds
    private Double avg; // is a double specifying the average amount of transaction value in the last 60 seconds
    private Double max; // is a double specifying single highest transaction value in the last 60 seconds
    private Double min; // is a double specifying single lowest transaction value in the last 60 seconds
    private Double count; // is a long specifying the total number of transactions happened in the last 60 seconds
}
