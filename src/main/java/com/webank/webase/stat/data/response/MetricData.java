package com.webank.webase.stat.data.response;

/**
 * Performance data handle(DTO) of monitor and performance module
 * performance response to web
 */
@lombok.Data
public class MetricData {
    private String metricType;
    private Data data;

    public MetricData(String metricType, Data data) {
        this.metricType = metricType;
        this.data = data;
    }
}