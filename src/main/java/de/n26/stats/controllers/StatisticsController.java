package de.n26.stats.controllers;

import de.n26.stats.domain.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private static final Logger LOG = LoggerFactory.getLogger(StatisticsController.class);

    @RequestMapping(method = RequestMethod.GET, path = "/statistics")
    @ResponseBody
    public Statistics getStatistics() {
        final Statistics stats = Statistics.builder()
                .count(10d)
                .max(150d)
                .min(13d)
                .sum(567d)
                .avg(59.4)
                .build();
        LOG.info("Getting statistics: {}", stats);

        return stats;
    }
}
