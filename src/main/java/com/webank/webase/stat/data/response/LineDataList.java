package com.webank.webase.stat.data.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * DATA UNIT of monitor data
 * LineDataList => Data => PerformanceData
 * containing list of result number which comes from monitor result
 */
@Data
public class LineDataList {
    List<Long> timestampList;
    List<BigDecimal> valueList;

    public LineDataList(List<Long> timestampList, List<BigDecimal> valueList) {
        this.timestampList = timestampList;
        this.valueList = valueList;
    }
}
