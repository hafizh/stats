package de.n26.stats.controllers;

import de.n26.stats.domain.Transaction;
import de.n26.stats.services.StatisticsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static de.n26.stats.utils.DurationUtils.tooOld;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
public class TransactionsController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionsController.class);

    @Autowired
    private StatisticsSource statsSource;

    @RequestMapping(method = RequestMethod.POST, path = "/transactions")
    public ResponseEntity<String> addTransaction(@RequestBody @Valid Transaction newTransaction) {
        LOG.info("Transaction received: {}", newTransaction);

        if(tooOld(newTransaction.getTimestamp())) {
            return new ResponseEntity<>(NO_CONTENT);
        }

        statsSource.insertTransaction(newTransaction);

        return new ResponseEntity<>(CREATED);
    }
}
