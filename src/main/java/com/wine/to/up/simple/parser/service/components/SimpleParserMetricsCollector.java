package com.wine.to.up.simple.parser.service.components;

import java.util.concurrent.TimeUnit;

import com.wine.to.up.commonlib.metrics.CommonMetricsCollector;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Metrics;
import io.prometheus.client.Summary;
import io.prometheus.client.Counter;
/**
 * This Class expose methods for recording specific metrics It changes metrics
 * of Micrometer and Prometheus simultaneously Micrometer's metrics exposed at
 * /actuator/prometheus Prometheus' metrics exposed at /metrics-prometheus
 */
@Component
public class SimpleParserMetricsCollector extends CommonMetricsCollector {
    private static final String SERVICE_NAME = "simple_parser_service_test";

    private static final String WINE_PAGE_FETCHING_DURATION = "wine_page_fetching_duration";
    private static final String WINE_DETAILS_PARSING_DURATION = "wine_details_parsing_duration";
    private static final String WINE_PAGE_PARSING_DURATION = "wine_page_parsing_duration";
    private static final String WINES_PUBLISHED_TO_KAFKA_COUNT = "wines_published_to_kafka_count";

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

    private static final Summary winePageParsingDurationSummary = Summary.build()
            .name(WINE_PAGE_PARSING_DURATION)
            .help("Wine catalog page parsing duration")
            .register();

    private static final Counter winesPublishedToKafkaCount = Counter.build()
            .name(WINES_PUBLISHED_TO_KAFKA_COUNT)
            .help("Wines published to kafka count")
            .register();

    public static void parseWineFetch(long time) {
        Metrics.timer(WINE_PAGE_FETCHING_DURATION).record(time, TimeUnit.MILLISECONDS);
        parseWineFetchSummary.observe(time);
    }

    public static void parseWineDetailsParsing(long time) {
        Metrics.timer(WINE_DETAILS_PARSING_DURATION).record(time, TimeUnit.MILLISECONDS);
        parseWineDetailsParsingSummary.observe(time);
    }

    public static void winePageParsingDuration(long time) {
        Metrics.timer(WINE_PAGE_PARSING_DURATION).record(time, TimeUnit.MILLISECONDS);
        winePageParsingDurationSummary.observe(time);
    }

    public static void winesPublishedToKafka() {
        Metrics.counter(WINES_PUBLISHED_TO_KAFKA_COUNT).increment();
        winesPublishedToKafkaCount.inc();
    }

}
