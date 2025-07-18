package com.texoit.airton.movieapi.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "movieapi")
public class MovieApiProperties {

    private final Csv csv = new Csv();
    private final Calculation calculation = new Calculation();
    private final Performance performance = new Performance();

    public Csv getCsv() {
        return csv;
    }

    public Calculation getCalculation() {
        return calculation;
    }

    public Performance getPerformance() {
        return performance;
    }

    public static class Csv {
        private String delimiter = ";";
        private String encoding = "UTF-8";
        private boolean skipFirstLine = true;
        private int batchSize = 1000;

        public String getDelimiter() {
            return delimiter;
        }

        public void setDelimiter(String delimiter) {
            this.delimiter = delimiter;
        }

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public boolean isSkipFirstLine() {
            return skipFirstLine;
        }

        public void setSkipFirstLine(boolean skipFirstLine) {
            this.skipFirstLine = skipFirstLine;
        }

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }
    }

    public static class Calculation {
        private boolean includeNonConsecutive = false;
        private int maxResults = 100;
        private boolean enableCaching = true;
        private long cacheExpirationMinutes = 60;

        public boolean isIncludeNonConsecutive() {
            return includeNonConsecutive;
        }

        public void setIncludeNonConsecutive(boolean includeNonConsecutive) {
            this.includeNonConsecutive = includeNonConsecutive;
        }

        public int getMaxResults() {
            return maxResults;
        }

        public void setMaxResults(int maxResults) {
            this.maxResults = maxResults;
        }

        public boolean isEnableCaching() {
            return enableCaching;
        }

        public void setEnableCaching(boolean enableCaching) {
            this.enableCaching = enableCaching;
        }

        public long getCacheExpirationMinutes() {
            return cacheExpirationMinutes;
        }

        public void setCacheExpirationMinutes(long cacheExpirationMinutes) {
            this.cacheExpirationMinutes = cacheExpirationMinutes;
        }
    }

    /**
     * Configurações de performance e monitoramento
     */
    public static class Performance {
        private boolean enableMetrics = true;
        private boolean enableSlowQueryLogging = true;
        private long slowQueryThresholdMs = 1000;
        private int threadPoolSize = 10;

        public boolean isEnableMetrics() {
            return enableMetrics;
        }

        public void setEnableMetrics(boolean enableMetrics) {
            this.enableMetrics = enableMetrics;
        }

        public boolean isEnableSlowQueryLogging() {
            return enableSlowQueryLogging;
        }

        public void setEnableSlowQueryLogging(boolean enableSlowQueryLogging) {
            this.enableSlowQueryLogging = enableSlowQueryLogging;
        }

        public long getSlowQueryThresholdMs() {
            return slowQueryThresholdMs;
        }

        public void setSlowQueryThresholdMs(long slowQueryThresholdMs) {
            this.slowQueryThresholdMs = slowQueryThresholdMs;
        }

        public int getThreadPoolSize() {
            return threadPoolSize;
        }

        public void setThreadPoolSize(int threadPoolSize) {
            this.threadPoolSize = threadPoolSize;
        }
    }
}