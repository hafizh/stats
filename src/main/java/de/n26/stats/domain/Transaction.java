package de.n26.stats.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;
import java.time.Instant;

@Data
@AllArgsConstructor
public class Transaction {
    @NonNull
    @Min(0)
    private Double amount;

    @NonNull
    private Instant timestamp;
}
