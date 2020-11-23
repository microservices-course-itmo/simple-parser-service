package com.wine.to.up.simple.parser.service.components;

import java.util.concurrent.TimeUnit;

import com.wine.to.up.commonlib.metrics.CommonMetricsCollector;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Metrics;
import io.prometheus.client.Summary;

/**
 * This Class expose methods for recording specific metrics It changes metrics
 * of Micrometer and Prometheus simultaneously Micrometer's metrics exposed at
 * /actuator/prometheus Prometheus' metrics exposed at /metrics-prometheus
 *
 */
@Component
public class SimpleParserMetricsCollector extends CommonMetricsCollector {
    private static final String SERVICE_NAME = "simple_parser_service_test";

    private static final String WINE_PAGE_FETCHING_DURATION = "wine_page_fetching_duration";

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

    public static void parseWineFetch(long time) {
        Metrics.timer(WINE_PAGE_FETCHING_DURATION).record(time, TimeUnit.MILLISECONDS);
        parseWineFetchSummary.observe(time);
    }
}
