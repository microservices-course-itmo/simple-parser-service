package com.wine.to.up.simple.parser.service.components;

import java.util.concurrent.TimeUnit;

import com.wine.to.up.commonlib.metrics.CommonMetricsCollector;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Metrics;
import io.prometheus.client.Summary;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

/**
 * This Class expose methods for recording specific metrics It changes metrics
 * of Micrometer and Prometheus simultaneously Micrometer's metrics exposed at
 * /actuator/prometheus Prometheus' metrics exposed at /metrics-prometheus
 */
@Component
public class SimpleParserMetricsCollector extends CommonMetricsCollector {
    private static final String SERVICE_NAME = "simple_parser_service_test";

    private static final String wine_page_fetching_duration = "wine_page_fetching_duration";
    private static final String PARSING_STARTED_TOTAL = "parsing_started_total";
    private static final String PARSING_COMPLETE_TOTAL = "parsing_complete_total";
    private static final String PARSING_IN_PROGRESS = "parsing_in_progress";
    private static int activeParsings = 0;
    private static final String WINE_PAGE_FETCHING_DURATION = "wine_page_fetching_duration";
    private static final String WINE_DETAILS_PARSING_DURATION = "wine_details_parsing_duration";

    public SimpleParserMetricsCollector() {
        this(SERVICE_NAME);
    }

    public SimpleParserMetricsCollector(String serviceName) {
        super(serviceName);
    }

    private static final Summary parseWineFetchSummary = Summary.build()
            .name(WINE_PAGE_FETCHING_DURATION)
            .help("Wine fetching time")
            .register();

    private static final Summary parseWineDetailsParsingSummary = Summary.build()
            .name(WINE_DETAILS_PARSING_DURATION)
            .help("Parsing wine details page time")
            .register();

    private static final Counter parsingStartedTotal = Counter.build()
    .name(PARSING_STARTED_TOTAL)
    .help("Number of started parsings")
    .register();

    private static final Counter parsingCompletedTotal = Counter.build()
    .name(PARSING_COMPLETE_TOTAL)
    .help("Number of completed parsings")
    .labelNames("status")
    .register();

    private static Gauge parsingInProgress = Gauge.build()
    .name(PARSING_IN_PROGRESS)
    .help("Number of parsers in progress")
    .register();

    public static void parseWineFetch(long time) {
        Metrics.timer(WINE_PAGE_FETCHING_DURATION).record(time, TimeUnit.MILLISECONDS);
        parseWineFetchSummary.observe(time);
    }

    public static void recordParsingStarted() {
        Metrics.counter(PARSING_STARTED_TOTAL).increment();
        activeParsings++;
        Metrics.gauge(PARSING_IN_PROGRESS, activeParsings);
        parsingInProgress.inc();
        parsingStartedTotal.inc();
    }

    public static void recordParsingCompleted(Boolean status) {
        Metrics.counter(PARSING_COMPLETE_TOTAL, "status", status ? "SUCCESS" : "FAILED").increment();
        activeParsings--;
        Metrics.gauge(PARSING_IN_PROGRESS, activeParsings);
        parsingInProgress.dec();
        parsingCompletedTotal.labels(status ? "SUCCESS" : "FAILED").inc();
    }
    public static void parseWineDetailsParsing(long time) {
        Metrics.timer(WINE_DETAILS_PARSING_DURATION).record(time, TimeUnit.MILLISECONDS);
        parseWineDetailsParsingSummary.observe(time);
    }

}
