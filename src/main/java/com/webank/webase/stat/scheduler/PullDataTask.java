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
import com.webank.webase.stat.data.entity.TbGroupBasicData;
import com.webank.webase.stat.data.entity.TbNodeMonitor;
import com.webank.webase.stat.data.entity.TbServerPerformance;
import com.webank.webase.stat.data.mapper.GroupBasicDataMapper;
import com.webank.webase.stat.data.mapper.NodeMonitorMapper;
import com.webank.webase.stat.data.mapper.ServerPerformanceMapper;
import com.webank.webase.stat.front.FrontService;
import com.webank.webase.stat.front.entity.TbFront;
import com.webank.webase.stat.front.entity.TransactionCount;
import com.webank.webase.stat.restinterface.FrontInterfaceService;
import com.webank.webase.stat.restinterface.entity.GroupSizeInfo;
import com.webank.webase.stat.restinterface.entity.NodeMonitor;
import com.webank.webase.stat.restinterface.entity.Performance;
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
    private NodeMonitorMapper nodeMonitorMapper;
    @Autowired
    private ServerPerformanceMapper serverPerformanceMapper;
    @Autowired
    private FrontGroupMapCache frontGroupMapCache;
    @Autowired
    private ConstantProperties constantProperties;

    /**
     * monitor group data size and trans count
     */
    @Scheduled(fixedDelayString = "${constant.pullGroupBasicDataInterval}", initialDelay = 1000)
    public void pullGroupBasicDataStart() {
        pullGroupBasicData();
    }

    /**
     * monitor block height, pbft view etc.
     */
    @Scheduled(fixedDelayString = "${constant.pullNodeMonitorInterval}", initialDelay = 1000)
    public void pullNodeMonitorStart() {
        pullNodeMonitor();
    }

    /**
     * monitor front's host cpu, memory tx_up tx_down etc.
     */
    @Scheduled(fixedDelayString = "${constant.pullServerPerformanceInterval}", initialDelay = 1000)
    public void pullServerPerformanceStart() {
        pullServerPerformance();
    }

    /**
     * pullGroupBasicData.
     */
    public void pullGroupBasicData() {
        Instant startTime = Instant.now();
        log.info("start pullGroupBasicData time:{}", startTime.toEpochMilli());

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
            Integer chainId = front.getChainId();
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
                groupBasicDataMapper.add(new TbGroupBasicData(chainId, frontId, groupId, size,
                    transactionCount.getTxSum().longValue(), null,
                    CommonUtils.getYearMonth(LocalDateTime.now())));
            }
        } catch (Exception ex) {
            log.error("fail pullGroupBasicDataByFront. frontId:{} ", front.getFrontId(), ex);
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
            Integer chainId = front.getChainId();
            Integer frontId = front.getFrontId();
            String frontIp = front.getFrontIp();
            Integer frontPort = front.getFrontPort();
            // query group list
            List<TbGroup> tbGroups = frontGroupMapCache.getMapByFrontId(frontId);
            // save data
            for (TbGroup tbGroup : tbGroups) {
                Integer groupId = tbGroup.getGroupId();
                TbNodeMonitor tbNodeMonitor =
                    nodeMonitorMapper.getMaxData(front.getChainId(), front.getFrontId(), groupId);
                LocalDateTime beginDate = null;
                if (!ObjectUtils.isEmpty(tbNodeMonitor)) {
                    beginDate =
                        CommonUtils.timestamp2LocalDateTime(tbNodeMonitor.getTimestamp() + 1);
                }
                List<NodeMonitor> list = frontInterfaceService.getNodeMonitor(frontIp, frontPort,
                    groupId, constantProperties.getPageSize(), 1, beginDate, null);
                for (NodeMonitor data : list) {
                    nodeMonitorMapper.add(new TbNodeMonitor(data.getId(), chainId, front.getFrontId(),
                        groupId, data.getBlockHeight(), data.getPbftView(),
                        data.getPendingTransactionCount(), data.getTimestamp(),
                        CommonUtils.getYearMonth(data.getTimestamp())));
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
            Integer chainId = front.getChainId();
            String frontIp = front.getFrontIp();
            Integer frontPort = front.getFrontPort();
            // save data
            TbServerPerformance tbServerPerformance =
                serverPerformanceMapper.getMaxData(front.getChainId(), front.getFrontId());
            LocalDateTime beginDate = null;
            if (!ObjectUtils.isEmpty(tbServerPerformance)) {
                beginDate =
                    CommonUtils.timestamp2LocalDateTime(tbServerPerformance.getTimestamp() + 1);
            }
            List<Performance> list = frontInterfaceService.getServerPerformance(frontIp, frontPort,
                constantProperties.getPageSize(), 1, beginDate, null);
            for (Performance data : list) {
                serverPerformanceMapper.add(new TbServerPerformance(data.getId(), chainId,
                    front.getFrontId(), data.getCpuUseRatio(), data.getDiskUseRatio(),
                    data.getMemoryUseRatio(), data.getRxbps(), data.getTxbps(),
                    data.getTimestamp(), CommonUtils.getYearMonth(data.getTimestamp())));
            }
        } catch (Exception ex) {
            log.error("fail pullServerPerformanceByFront. frontId:{} ", front.getFrontId(), ex);
        } finally {
            latch.countDown();
        }
    }
}
