package de.n26.stats.domain;

import lombok.Data;

import java.time.Instant;

@Data
public class Transaction {
    private Double amount;
    private Instant timestamp;
}
