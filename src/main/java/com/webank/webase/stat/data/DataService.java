/**
 * Copyright 2014-2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.webank.webase.stat.data;

import com.webank.webase.stat.base.code.ConstantCode;
import com.webank.webase.stat.base.entity.BaseQueryParam;
import com.webank.webase.stat.base.exception.BaseException;
import com.webank.webase.stat.data.entity.TbGroupBasicData;
import com.webank.webase.stat.data.entity.TbNodeMonitor;
import com.webank.webase.stat.data.entity.TbServerPerformance;
import com.webank.webase.stat.data.mapper.GroupBasicDataMapper;
import com.webank.webase.stat.data.mapper.NodeMonitorMapper;
import com.webank.webase.stat.data.mapper.ServerPerformanceMapper;
import com.webank.webase.stat.data.response.Data;
import com.webank.webase.stat.data.response.LineDataList;
import com.webank.webase.stat.data.response.MetricData;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * services for data.
 */
@Log4j2
@Service
public class DataService {
    @Autowired
    private GroupBasicDataMapper groupBasicDataMapper;
    @Autowired
    private NodeMonitorMapper nodeMonitorMapper;
    @Autowired
    private ServerPerformanceMapper serverPerformanceMapper;
    // host upload bps(bit per second)
    private static final String TXBPS = "txbps";
    // host download bps(bit per second)
    private static final String RXBPS = "rxbps";

    /**
     * get node monitor metrics list
     */
    public List<MetricData> findNodeMonitorListByTime(Integer chainId, Integer frontId, Integer groupId,
        LocalDateTime startTime, LocalDateTime endTime,
        LocalDateTime contrastStartTime, LocalDateTime contrastEndTime, int gap) {

        List<TbNodeMonitor> monitorList;
        if (startTime == null || endTime == null) {
            monitorList = new ArrayList<>();
        } else {
            monitorList = this.getNodeMonitorList(chainId, frontId, groupId, startTime, endTime);
        }
        List<TbNodeMonitor> contrastMonitorList = new ArrayList<>();
        if (contrastStartTime != null && contrastEndTime != null) {
            contrastMonitorList = this.getNodeMonitorList(chainId, frontId, groupId,
                contrastStartTime, contrastEndTime);
        }
        return transferToNodeMonitorMetricData(transferMonitorListByGap(monitorList, gap),
            transferMonitorListByGap(contrastMonitorList, gap));
    }

    /**
     * get server performance metrics list
     */
    public List<MetricData> findServerPerformanceByTime(Integer chainId, Integer frontId,
        LocalDateTime startTime, LocalDateTime endTime,
        LocalDateTime contrastStartTime, LocalDateTime contrastEndTime, int gap) {

        List<TbServerPerformance> performanceList;
        if (startTime == null || endTime == null) {
            performanceList = new ArrayList<>();
        } else {
            performanceList = this.getServerPerformanceList(chainId, frontId, startTime, endTime);
        }
        List<TbServerPerformance> contrastPerformanceList = new ArrayList<>();
        if (contrastStartTime != null && contrastEndTime != null) {
            contrastPerformanceList = this.getServerPerformanceList(chainId, frontId,
                contrastStartTime, contrastEndTime);
        }
        return transferToPerformanceData(transferPerformanceListByGap(performanceList, gap),
            transferPerformanceListByGap(contrastPerformanceList, gap));
    }

    /**
     * process node monitor / server performance contrast metric data
     */
    // start of node monitor contrast
    private List<MetricData> transferToNodeMonitorMetricData(List<TbNodeMonitor> monitorList,
        List<TbNodeMonitor> contrastMonitorList) {
        List<Long> timestampList = new ArrayList<>();
        List<BigDecimal> blockHeightValueList = new ArrayList<>();
        List<BigDecimal> pbftViewValueList = new ArrayList<>();
        List<BigDecimal> pendingCountValueList = new ArrayList<>();
        for (TbNodeMonitor monitor : monitorList) {
            blockHeightValueList.add(monitor.getBlockHeight() == null ? null
                : new BigDecimal(monitor.getBlockHeight()));
            pbftViewValueList.add(
                monitor.getPbftView() == null ? null : new BigDecimal(monitor.getPbftView()));
            pendingCountValueList.add(monitor.getPendingTransactionCount() == null ? null
                : new BigDecimal(monitor.getPendingTransactionCount()));
            timestampList.add(monitor.getTimestamp());
        }
        monitorList.clear();

        List<Long> contrastTimestampList = new ArrayList<>();
        List<BigDecimal> contrastBlockHeightValueList = new ArrayList<>();
        List<BigDecimal> contrastPbftViewValueList = new ArrayList<>();
        List<BigDecimal> contrastPendingCountValueList = new ArrayList<>();
        for (TbNodeMonitor monitor : contrastMonitorList) {
            contrastBlockHeightValueList.add(monitor.getBlockHeight() == null ? null
                : new BigDecimal(monitor.getBlockHeight()));
            contrastPbftViewValueList.add(
                monitor.getPbftView() == null ? null : new BigDecimal(monitor.getPbftView()));
            contrastPendingCountValueList.add(monitor.getPendingTransactionCount() == null ? null
                : new BigDecimal(monitor.getPendingTransactionCount()));
            contrastTimestampList.add(monitor.getTimestamp());
        }
        contrastMonitorList.clear();
        List<MetricData> MetricDataList = new ArrayList<>();
        MetricDataList.add(new MetricData("blockHeight",
            new Data(new LineDataList(timestampList, blockHeightValueList),
                new LineDataList(contrastTimestampList, contrastBlockHeightValueList))));
        MetricDataList.add(
            new MetricData("pbftView", new Data(new LineDataList(null, pbftViewValueList),
                new LineDataList(null, contrastPbftViewValueList))));
        MetricDataList.add(new MetricData("pendingCount",
            new Data(new LineDataList(null, pendingCountValueList),
                new LineDataList(null, contrastPendingCountValueList))));
        return MetricDataList;
    }

    public List transferMonitorListByGap(List arrayList, int gap) {
        if (gap == 0) {
            throw new BaseException(ConstantCode.GAP_PARAM_ERROR);
        }
        List newMonitorList = this.fillNodeMonitorList(arrayList);
        List ilist = new ArrayList<>();
        int len = newMonitorList.size();
        for (int i = 0; i < len; i = i + gap) {
            ilist.add(newMonitorList.get(i));
        }
        return ilist;
    }

    private List<TbNodeMonitor> fillNodeMonitorList(List<TbNodeMonitor> monitorList) {
        List<TbNodeMonitor> newMonitorList = new ArrayList<>();
        for (int i = 0; i < monitorList.size() - 1; i++) {
            Long startTime = monitorList.get(i).getTimestamp();
            Long endTime = monitorList.get(i + 1).getTimestamp();
            if (endTime - startTime > 10000) {
                log.debug("****startTime" + startTime);
                log.debug("****endTime" + endTime);
                while (endTime - startTime > 5000) {
                    TbNodeMonitor emptyMonitor = new TbNodeMonitor();
                    emptyMonitor.setTimestamp(startTime + 5000);
                    newMonitorList.add(emptyMonitor);
                    log.debug("****insert" + startTime);
                    startTime = startTime + 5000;
                }
            } else {
                newMonitorList.add(monitorList.get(i));
            }
        }
        return newMonitorList;
    }
    // end of node monitor contrast

    // start of server performance contrast

    /**
     * process server performance contrast metric data
     */
    private List<MetricData> transferToPerformanceData(List<TbServerPerformance> performanceList,
        List<TbServerPerformance> contrastPerformanceList) {
        List<Long> timestampList = new ArrayList<>();
        List<BigDecimal> memoryValueList = new ArrayList<>();
        List<BigDecimal> cpuValueList = new ArrayList<>();
        List<BigDecimal> diskValueList = new ArrayList<>();
        List<BigDecimal> rxbpsValueList = new ArrayList<>();
        List<BigDecimal> txbpsValueList = new ArrayList<>();
        for (TbServerPerformance performance : performanceList) {
            cpuValueList.add(performance.getCpuUseRatio());
            memoryValueList.add(performance.getMemoryUseRatio());
            diskValueList.add(performance.getDiskUseRatio());
            timestampList.add(performance.getTimestamp());
            rxbpsValueList.add(performance.getRxbps());
            txbpsValueList.add(performance.getTxbps());
        }
        performanceList.clear();

        List<Long> contrastTimestampList = new ArrayList<>();
        List<BigDecimal> contrastMemoryValueList = new ArrayList<>();
        List<BigDecimal> contrastCpuValueList = new ArrayList<>();
        List<BigDecimal> contrastDiskValueList = new ArrayList<>();
        List<BigDecimal> contrastRxbpsValueList = new ArrayList<>();
        List<BigDecimal> contrastTxbpsValueList = new ArrayList<>();
        for (TbServerPerformance performance : contrastPerformanceList) {
            contrastCpuValueList.add(performance.getCpuUseRatio());
            contrastMemoryValueList.add(performance.getMemoryUseRatio());
            contrastDiskValueList.add(performance.getDiskUseRatio());
            contrastRxbpsValueList.add(performance.getRxbps());
            contrastTxbpsValueList.add(performance.getTxbps());
            contrastTimestampList.add(performance.getTimestamp());
        }
        contrastPerformanceList.clear();
        List<MetricData> performanceDataList = new ArrayList<>();
        performanceDataList.add(
            new MetricData("cpu", new Data(new LineDataList(timestampList, cpuValueList),
                new LineDataList(contrastTimestampList, contrastCpuValueList))));
        performanceDataList
            .add(new MetricData("memory", new Data(new LineDataList(null, memoryValueList),
                new LineDataList(null, contrastMemoryValueList))));
        performanceDataList
            .add(new MetricData("disk", new Data(new LineDataList(null, diskValueList),
                new LineDataList(null, contrastDiskValueList))));
        performanceDataList
            .add(new MetricData(TXBPS, new Data(new LineDataList(null, txbpsValueList),
                new LineDataList(null, contrastTxbpsValueList))));
        performanceDataList
            .add(new MetricData(RXBPS, new Data(new LineDataList(null, rxbpsValueList),
                new LineDataList(null, contrastRxbpsValueList))));
        return performanceDataList;
    }

    public List transferPerformanceListByGap(List arrayList, int gap) {
        if (gap == 0) {
            throw new BaseException(ConstantCode.GAP_PARAM_ERROR);
        }
        List newMonitorList = this.fillPerformanceList(arrayList);
        List ilist = new ArrayList<>();
        int len = newMonitorList.size();
        for (int i = 0; i < len; i = i + gap) {
            ilist.add(newMonitorList.get(i));
        }
        return ilist;
    }

    private List<TbServerPerformance> fillPerformanceList(List<TbServerPerformance> performanceList) {
        List<TbServerPerformance> newPerformanceList = new ArrayList<>();
        for (int i = 0; i < performanceList.size() - 1; i++) {
            Long startTime = performanceList.get(i).getTimestamp();
            Long endTime = performanceList.get(i + 1).getTimestamp();
            if (endTime - startTime > 10000) {
                log.debug("****startTime" + startTime);
                log.debug("****endTime" + endTime);
                while (endTime - startTime > 5000) {
                    TbServerPerformance emptyPerformance = new TbServerPerformance();
                    emptyPerformance.setTimestamp(startTime + 5000);
                    newPerformanceList.add(emptyPerformance);
                    log.debug("****insert" + startTime);
                    startTime = startTime + 5000;
                }
            } else {
                newPerformanceList.add(performanceList.get(i));
            }
        }
        return newPerformanceList;
    }
    // end of server performance contrast

    /**
     * query count.
     */
    public Integer countOfGroupBasicData(BaseQueryParam queryParam) {
        return groupBasicDataMapper.getCount(queryParam);
    }

    /**
     * query list.
     */
    public List<TbGroupBasicData> getGroupBasicDataList(BaseQueryParam queryParam) {
        return groupBasicDataMapper.getList(queryParam);
    }

    /**
     * query count.
     */
    public Integer countOfNodeMonitor(BaseQueryParam queryParam) {
        return nodeMonitorMapper.getCount(queryParam);
    }

    /**
     * query list.
     */
    public List<TbNodeMonitor> getNodeMonitorList(BaseQueryParam queryParam) {
        return nodeMonitorMapper.getList(queryParam);
    }

    public List<TbNodeMonitor> getNodeMonitorList(Integer chainId, Integer frontId,
        Integer groupId, LocalDateTime startTime, LocalDateTime endTime) {
        BaseQueryParam param = this.initQueryParam(chainId, frontId, groupId, startTime, endTime);
        return nodeMonitorMapper.getList(param);
    }

    /**
     * query count.
     */
    public Integer countOfServerPerformance(BaseQueryParam queryParam) {
        return serverPerformanceMapper.getCount(queryParam);
    }

    /**
     * query list.
     */
    public List<TbServerPerformance> getServerPerformanceList(BaseQueryParam queryParam) {
        return serverPerformanceMapper.getList(queryParam);
    }

    public List<TbServerPerformance> getServerPerformanceList(Integer chainId, Integer frontId,
        LocalDateTime startTime, LocalDateTime endTime) {
        BaseQueryParam param = this.initQueryParam(chainId, frontId, null, startTime, endTime);
        return serverPerformanceMapper.getList(param);
    }


    private BaseQueryParam initQueryParam(Integer chainId, Integer frontId,
        Integer groupId, LocalDateTime startTime, LocalDateTime endTime) {
        BaseQueryParam param = new BaseQueryParam();
        param.setChainId(chainId);
        param.setFrontId(frontId);
        param.setGroupId(groupId);
        param.setBeginDate(startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        param.setEndDate(endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return param;
    }
}
