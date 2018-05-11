package de.n26.stats.controllers;

import de.n26.stats.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
public class TransactionsController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionsController.class);
    private static final long limit = TimeUnit.MINUTES.toSeconds(1);

    @RequestMapping(method = RequestMethod.POST, path = "/transactions")
    public ResponseEntity<String> addTransaction(@RequestBody Transaction newTransaction) {
        LOG.info("Transaction received: {}", newTransaction);

        final long elapsed = Duration.between(newTransaction.getTimestamp(), Instant.now()).getSeconds();
        if(elapsed > limit) {
            return new ResponseEntity<>(NO_CONTENT);
        }

        return new ResponseEntity<>(CREATED);
    }
}
