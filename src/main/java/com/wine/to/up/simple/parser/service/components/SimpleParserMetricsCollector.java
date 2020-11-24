package com.wine.to.up.simple.parser.service.components;

import java.util.concurrent.TimeUnit;

import com.wine.to.up.commonlib.metrics.CommonMetricsCollector;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Metrics;
import io.prometheus.client.Summary;
import io.prometheus.client.Gauge;

/**
 * This Class expose methods for recording specific metrics It changes metrics
 * of Micrometer and Prometheus simultaneously Micrometer's metrics exposed at
 * /actuator/prometheus Prometheus' metrics exposed at /metrics-prometheus
 *
 */
@Component
public class SimpleParserMetricsCollector extends CommonMetricsCollector {
    private static final String SERVICE_NAME = "simple_parser_service";

    private static final String WINE_DETAILS_FETCHING_DURATION = "wine_details_fetching_duration";
    private static final String PARSING_PROCESS_DURATION = "parsing_process_duration";
    private static final String TIME_SINCE_LAST_SUCCEEDED_PARSING = "time_since_last_succeeded_parsing";

    public SimpleParserMetricsCollector() {
        this(SERVICE_NAME);
    }

    public SimpleParserMetricsCollector(String serviceName) {
        super(serviceName);
    }

    private static final Summary parseWineFetchSummary = Summary.build()
    .name(WINE_DETAILS_FETCHING_DURATION)
    .help("Wine details fetching time")
    .register();

    public static void parseWineFetch(long time) {
        Metrics.timer(WINE_DETAILS_FETCHING_DURATION).record(time, TimeUnit.MILLISECONDS);
        parseWineFetchSummary.observe(time);
    }

    private static final Summary parseProcessSummary = Summary.build()
            .name(PARSING_PROCESS_DURATION)
            .help("Total parsing process time")
            .register();

    public static void parseProcess(long time) {
        Metrics.timer(PARSING_PROCESS_DURATION).record(time, TimeUnit.MILLISECONDS);
        parseProcessSummary.observe(time);
    }

    private static final Gauge lastSucceededParseGauge = Gauge.build()
            .name(TIME_SINCE_LAST_SUCCEEDED_PARSING)
            .help("Time since last succeeded parsing")
            .register();

    public static void timeSinceLastSucceededParse(long time) {
        Metrics.gauge(TIME_SINCE_LAST_SUCCEEDED_PARSING, time);
        lastSucceededParseGauge.inc();
    }
}
