package com.webank.webase.stat.data.response;

/**
 * DATA ENTITY of node monitor and host monitor (performance) module
 * related with LineDataList and PerformanceData
 * LineDataList => Data => PerformanceData
 */
@lombok.Data
public class Data {
    private LineDataList lineDataList;
    private LineDataList contrastDataList;

    public Data(LineDataList lineDataList, LineDataList contrastDataList) {
        this.lineDataList = lineDataList;
        this.contrastDataList = contrastDataList;
    }
}