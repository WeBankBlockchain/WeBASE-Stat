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
package com.webank.webase.stat.scheduler;

import com.webank.webase.stat.base.properties.ConstantProperties;
import com.webank.webase.stat.base.tools.CommonUtils;
import com.webank.webase.stat.data.entity.TbGasData;
import com.webank.webase.stat.data.entity.TbGroupBasicData;
import com.webank.webase.stat.data.entity.TbNetWorkData;
import com.webank.webase.stat.data.entity.TbNodeMonitor;
import com.webank.webase.stat.data.entity.TbServerPerformance;
import com.webank.webase.stat.data.enums.LogTypes;
import com.webank.webase.stat.data.mapper.GasDataMapper;
import com.webank.webase.stat.data.mapper.GroupBasicDataMapper;
import com.webank.webase.stat.data.mapper.NetWorkDataMapper;
import com.webank.webase.stat.data.mapper.NodeMonitorMapper;
import com.webank.webase.stat.data.mapper.ServerPerformanceMapper;
import com.webank.webase.stat.front.FrontService;
import com.webank.webase.stat.front.entity.TbFront;
import com.webank.webase.stat.front.entity.TransactionCount;
import com.webank.webase.stat.frontinterface.FrontInterfaceService;
import com.webank.webase.stat.frontinterface.entity.GroupSizeInfo;
import com.webank.webase.stat.frontinterface.entity.NetWorkData;
import com.webank.webase.stat.frontinterface.entity.NodeMonitor;
import com.webank.webase.stat.frontinterface.entity.Performance;
import com.webank.webase.stat.frontinterface.entity.TxGasData;
import com.webank.webase.stat.group.FrontGroupMapCache;
import com.webank.webase.stat.group.entity.TbGroup;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Log4j2
@Component
public class PullDataTask {

    @Autowired
    private FrontService frontService;
    @Autowired
    private FrontInterfaceService frontInterfaceService;
    @Autowired
    private GroupBasicDataMapper groupBasicDataMapper;
    @Autowired
    private NetWorkDataMapper netWorkDataMapper;
    @Autowired
    private GasDataMapper gasDataMapper;
    @Autowired
    private NodeMonitorMapper nodeMonitorMapper;
    @Autowired
    private ServerPerformanceMapper serverPerformanceMapper;
    @Autowired
    private FrontGroupMapCache frontGroupMapCache;
    @Autowired
    private ConstantProperties constantProperties;

    @Scheduled(fixedDelayString = "${constant.pullGroupBasicDataInterval}", initialDelay = 1000)
    public void pullGroupBasicDataStart() {
        pullGroupBasicData();
    }

    @Scheduled(fixedDelayString = "${constant.pullNetWorkDataInterval}", initialDelay = 1000)
    public void pullNetWorkDataStart() {
        pullNetWorkData();
    }

    @Scheduled(fixedDelayString = "${constant.pullGasDataInterval}", initialDelay = 1000)
    public void pullGasDataStart() {
        pullGasData();
    }

    @Scheduled(fixedDelayString = "${constant.pullNodeMonitorInterval}", initialDelay = 1000)
    public void pullNodeMonitorStart() {
        pullNodeMonitor();
    }

    @Scheduled(fixedDelayString = "${constant.pullServerPerformanceInterval}", initialDelay = 1000)
    public void pullServerPerformanceStart() {
        pullServerPerformance();
    }

    /**
     * pullGroupBasicData.
     */
    public void pullGroupBasicData() {
        Instant startTime = Instant.now();
        log.info("start pullGroupBasicData.", startTime.toEpochMilli());

        // get all front
        List<TbFront> frontList = frontService.getFrontList(null);
        if (CollectionUtils.isEmpty(frontList)) {
            log.debug("not fount any front.");
            return;
        }

        CountDownLatch latch = new CountDownLatch(frontList.size());
        for (TbFront front : frontList) {
            pullGroupBasicDataByFront(latch, front);
        }

        try {
            latch.await();
        } catch (InterruptedException ex) {
            log.error("InterruptedException", ex);
            Thread.currentThread().interrupt();
        }

        log.info("end pullGroupBasicData. useTime:{} ",
                Duration.between(startTime, Instant.now()).toMillis());
    }

    /**
     * pullNetWorkData.
     */
    public void pullNetWorkData() {
        Instant startTime = Instant.now();
        log.info("start pullNetWorkData.", startTime.toEpochMilli());

        // get all front
        List<TbFront> frontList = frontService.getFrontList(null);
        if (CollectionUtils.isEmpty(frontList)) {
            log.debug("not fount any front.");
            return;
        }

        CountDownLatch latch = new CountDownLatch(frontList.size());
        for (TbFront front : frontList) {
            pullNetWorkDataByFront(latch, front);
        }

        try {
            latch.await();
        } catch (InterruptedException ex) {
            log.error("InterruptedException", ex);
            Thread.currentThread().interrupt();
        }

        log.info("end pullNetWorkData. useTime:{} ",
                Duration.between(startTime, Instant.now()).toMillis());
    }

    /**
     * pullGasData.
     */
    public void pullGasData() {
        Instant startTime = Instant.now();
        log.info("start pullGasData.", startTime.toEpochMilli());

        // get all front
        List<TbFront> frontList = frontService.getFrontList(null);
        if (CollectionUtils.isEmpty(frontList)) {
            log.debug("not fount any front.");
            return;
        }

        CountDownLatch latch = new CountDownLatch(frontList.size());
        for (TbFront front : frontList) {
            pullGasDataByFront(latch, front);
        }

        try {
            latch.await();
        } catch (InterruptedException ex) {
            log.error("InterruptedException", ex);
            Thread.currentThread().interrupt();
        }

        log.info("end pullGasData. useTime:{} ",
                Duration.between(startTime, Instant.now()).toMillis());
    }

    /**
     * pullNodeMonitor.
     */
    public void pullNodeMonitor() {
        Instant startTime = Instant.now();
        log.info("start pullNodeMonitor.", startTime.toEpochMilli());

        // get all front
        List<TbFront> frontList = frontService.getFrontList(null);
        if (CollectionUtils.isEmpty(frontList)) {
            log.debug("not fount any front.");
            return;
        }

        CountDownLatch latch = new CountDownLatch(frontList.size());
        for (TbFront front : frontList) {
            pullNodeMonitorByFront(latch, front);
        }

        try {
            latch.await();
        } catch (InterruptedException ex) {
            log.error("InterruptedException", ex);
            Thread.currentThread().interrupt();
        }

        log.info("end pullNodeMonitor. useTime:{} ",
                Duration.between(startTime, Instant.now()).toMillis());
    }

    /**
     * pullServerPerformance.
     */
    public void pullServerPerformance() {
        Instant startTime = Instant.now();
        log.info("start pullServerPerformance.", startTime.toEpochMilli());

        // get all front
        List<TbFront> frontList = frontService.getFrontList(null);
        if (CollectionUtils.isEmpty(frontList)) {
            log.debug("not fount any front.");
            return;
        }

        CountDownLatch latch = new CountDownLatch(frontList.size());
        for (TbFront front : frontList) {
            pullServerPerformanceByFront(latch, front);
        }

        try {
            latch.await();
        } catch (InterruptedException ex) {
            log.error("InterruptedException", ex);
            Thread.currentThread().interrupt();
        }

        log.info("end pullServerPerformance. useTime:{} ",
                Duration.between(startTime, Instant.now()).toMillis());
    }

    /**
     * pullGroupBasicDataByFront.
     */
    @Async(value = "asyncExecutor")
    private void pullGroupBasicDataByFront(CountDownLatch latch, TbFront front) {
        try {
            Integer frontId = front.getFrontId();
            String frontIp = front.getFrontIp();
            Integer frontPort = front.getFrontPort();
            // query group list
            List<TbGroup> tbGroups = frontGroupMapCache.getMapByFrontId(frontId);
            // query groupSizeInfos
            List<GroupSizeInfo> groupSizeInfos =
                    frontInterfaceService.getGroupSizeInfos(frontIp, frontPort);
            for (TbGroup tbGroup : tbGroups) {
                Integer groupId = tbGroup.getGroupId();
                TransactionCount transactionCount;
                try {
                    transactionCount = frontInterfaceService.getTotalTransactionCount(frontIp,
                            frontPort, groupId);
                } catch (Exception e) {
                    continue;
                }
                long size = 0;
                for (GroupSizeInfo groupSizeInfo : groupSizeInfos) {
                    if (groupSizeInfo.getGroupId() == groupId) {
                        size = groupSizeInfo.getSize();
                        break;
                    }
                }
                // save data
                groupBasicDataMapper.add(new TbGroupBasicData(frontId, groupId, size,
                        transactionCount.getTxSum().longValue(), null));
            }
        } catch (Exception ex) {
            log.error("fail pullGroupBasicDataByFront. frontId:{} ", front.getFrontId(), ex);
        } finally {
            latch.countDown();
        }
    }

    /**
     * pullNetWorkDataByFront.
     */
    @Async(value = "asyncExecutor")
    private void pullNetWorkDataByFront(CountDownLatch latch, TbFront front) {
        try {
            Integer frontId = front.getFrontId();
            String frontIp = front.getFrontIp();
            Integer frontPort = front.getFrontPort();
            // query group list
            List<TbGroup> tbGroups = frontGroupMapCache.getMapByFrontId(frontId);
            for (TbGroup tbGroup : tbGroups) {
                Integer groupId = tbGroup.getGroupId();
                TbNetWorkData tbNetWorkData =
                        netWorkDataMapper.getMaxData(front.getFrontId(), groupId);
                LocalDateTime lastDate = null;
                if (!ObjectUtils.isEmpty(tbNetWorkData)) {
                    lastDate =
                            CommonUtils.timestamp2LocalDateTime(tbNetWorkData.getTimestamp() + 1);
                }
                List<NetWorkData> list = frontInterfaceService.getNetWorkData(frontIp, frontPort,
                        groupId, constantProperties.getPageSize(), 1, lastDate, null);
                // save data
                for (NetWorkData data : list) {
                    netWorkDataMapper.add(new TbNetWorkData(data.getId(), front.getFrontId(),
                            groupId, data.getTotalIn(), data.getTotalOut(), data.getTimestamp()));
                }
                if (CollectionUtils.isEmpty(list) || lastDate == null) {
                    continue;
                }
                // remove front data
                frontInterfaceService.deleteLogData(frontIp, frontPort, groupId,
                        LogTypes.NETWORK.getValue(), lastDate);
            }
        } catch (Exception ex) {
            log.error("fail pullNetWorkDataByFront. frontId:{} ", front.getFrontId(), ex);
        } finally {
            latch.countDown();
        }
    }

    /**
     * pullGasDataByFront.
     */
    @Async(value = "asyncExecutor")
    private void pullGasDataByFront(CountDownLatch latch, TbFront front) {
        try {
            Integer frontId = front.getFrontId();
            String frontIp = front.getFrontIp();
            Integer frontPort = front.getFrontPort();
            // query group list
            List<TbGroup> tbGroups = frontGroupMapCache.getMapByFrontId(frontId);
            for (TbGroup tbGroup : tbGroups) {
                Integer groupId = tbGroup.getGroupId();
                TbGasData tbGasData = gasDataMapper.getMaxData(front.getFrontId(), groupId);
                LocalDateTime lastDate = null;
                if (!ObjectUtils.isEmpty(tbGasData)) {
                    lastDate = CommonUtils.timestamp2LocalDateTime(tbGasData.getTimestamp() + 1);
                }
                List<TxGasData> list = frontInterfaceService.getTxGasData(frontIp, frontPort,
                        groupId, constantProperties.getPageSize(), 1, lastDate, null, null);
                // save data
                for (TxGasData data : list) {
                    gasDataMapper.add(new TbGasData(data.getId(), front.getFrontId(), groupId,
                            data.getTransHash(), data.getGasUsed(), data.getTimestamp()));
                }
                if (CollectionUtils.isEmpty(list) || lastDate == null) {
                    continue;
                }
                // remove front data
                frontInterfaceService.deleteLogData(frontIp, frontPort, groupId,
                        LogTypes.TxGAS.getValue(), lastDate);
            }
        } catch (Exception ex) {
            log.error("fail pullGasDataByFront. frontId:{} ", front.getFrontId(), ex);
        } finally {
            latch.countDown();
        }
    }

    /**
     * pullNodeMonitorByFront.
     */
    @Async(value = "asyncExecutor")
    private void pullNodeMonitorByFront(CountDownLatch latch, TbFront front) {
        try {
            Integer frontId = front.getFrontId();
            String frontIp = front.getFrontIp();
            Integer frontPort = front.getFrontPort();
            // query group list
            List<TbGroup> tbGroups = frontGroupMapCache.getMapByFrontId(frontId);
            // save data
            for (TbGroup tbGroup : tbGroups) {
                Integer groupId = tbGroup.getGroupId();
                TbNodeMonitor tbNodeMonitor =
                        nodeMonitorMapper.getMaxData(front.getFrontId(), groupId);
                LocalDateTime beginDate = null;
                if (!ObjectUtils.isEmpty(tbNodeMonitor)) {
                    beginDate =
                            CommonUtils.timestamp2LocalDateTime(tbNodeMonitor.getTimestamp() + 1);
                }
                List<NodeMonitor> list = frontInterfaceService.getNodeMonitor(frontIp, frontPort,
                        groupId, constantProperties.getPageSize(), 1, beginDate, null);
                for (NodeMonitor data : list) {
                    nodeMonitorMapper.add(new TbNodeMonitor(data.getId(), front.getFrontId(),
                            groupId, data.getBlockHeight(), data.getPbftView(),
                            data.getPendingTransactionCount(), data.getTimestamp()));
                }
            }
        } catch (Exception ex) {
            log.error("fail pullNodeMonitorByFront. frontId:{} ", front.getFrontId(), ex);
        } finally {
            latch.countDown();
        }
    }

    /**
     * pullServerPerformanceByFront.
     */
    @Async(value = "asyncExecutor")
    private void pullServerPerformanceByFront(CountDownLatch latch, TbFront front) {
        try {
            String frontIp = front.getFrontIp();
            Integer frontPort = front.getFrontPort();
            // save data
            TbServerPerformance tbServerPerformance =
                    serverPerformanceMapper.getMaxData(front.getFrontId());
            LocalDateTime beginDate = null;
            if (!ObjectUtils.isEmpty(tbServerPerformance)) {
                beginDate =
                        CommonUtils.timestamp2LocalDateTime(tbServerPerformance.getTimestamp() + 1);
            }
            List<Performance> list = frontInterfaceService.getServerPerformance(frontIp, frontPort,
                    constantProperties.getPageSize(), 1, beginDate, null);
            for (Performance data : list) {
                serverPerformanceMapper.add(new TbServerPerformance(data.getId(),
                        front.getFrontId(), data.getCpuUseRatio(), data.getDiskUseRatio(),
                        data.getMemoryUseRatio(), data.getRxbps(), data.getTxbps(),
                        data.getTimestamp()));
            }
        } catch (Exception ex) {
            log.error("fail pullServerPerformanceByFront. frontId:{} ", front.getFrontId(), ex);
        } finally {
            latch.countDown();
        }
    }
}
