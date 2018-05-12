package de.n26.stats.controllers;

import de.n26.stats.domain.Statistics;
import de.n26.stats.services.StatisticsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private static final Logger LOG = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private StatisticsSource statsSource;

    @RequestMapping(method = RequestMethod.GET, path = "/statistics")
    @ResponseBody
    public Statistics getStatistics() {
        final Statistics stats = statsSource.getStats();
        LOG.info("Getting statistics: {}", stats);

        return stats;
    }
}
