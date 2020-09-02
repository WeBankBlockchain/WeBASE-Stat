/**
 * Copyright 2014-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.webank.webase.stat.data;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import com.webank.webase.stat.base.code.ConstantCode;
import com.webank.webase.stat.base.controller.BaseController;
import com.webank.webase.stat.base.entity.BasePageResponse;
import com.webank.webase.stat.base.entity.BaseQueryParam;
import com.webank.webase.stat.base.entity.BaseResponse;
import com.webank.webase.stat.base.exception.BaseException;
import com.webank.webase.stat.data.entity.TbGroupBasicData;
import com.webank.webase.stat.data.entity.TbNodeMonitor;
import com.webank.webase.stat.data.entity.TbServerPerformance;
import com.webank.webase.stat.data.response.MetricData;
import com.webank.webase.stat.front.FrontService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for processing group information.
 */
@Log4j2
@RestController
@RequestMapping("data")
public class DataController extends BaseController {

    @Autowired
    private DataService dataService;
    @Autowired
    private FrontService frontService;

    /* metric data of processed statistic data to fit in echart */

    /**
     * get node monitor metrics
     */
    @ApiOperation(value = "查询链上数据", notes = "查询链上数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "beginDate", value = "开始时间"),
        @ApiImplicitParam(name = "endDate", value = "结束时间"),
        @ApiImplicitParam(name = "contrastBeginDate", value = "对比开始时间"),
        @ApiImplicitParam(name = "contrastEndDate", value = "对比结束时间"),
        @ApiImplicitParam(name = "gap", value = "时间间隔", dataType = "int")})
    @GetMapping("/metrics/nodeMonitor")
    public BaseResponse getNodeMonitorMetrics(
        @RequestParam(required = false) @DateTimeFormat(
            iso = DATE_TIME) LocalDateTime beginDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DATE_TIME) LocalDateTime endDate,
        @RequestParam(required = false) @DateTimeFormat(
            iso = DATE_TIME) LocalDateTime contrastBeginDate,
        @RequestParam(required = false) @DateTimeFormat(
            iso = DATE_TIME) LocalDateTime contrastEndDate,
        @RequestParam(required = false, defaultValue = "1") int gap,
        @RequestParam(defaultValue = "1") int groupId,
        @RequestParam(required = true) Integer frontId,
        @RequestParam(required = false) Integer chainId) {
        Instant startTime = Instant.now();
        log.info("getChainMonitor start. groupId:[{}]", groupId,
            startTime.toEpochMilli());

        List<MetricData> metricDataList = dataService.findNodeMonitorListByTime(chainId, frontId,
            groupId, beginDate, endDate, contrastBeginDate, contrastEndDate, gap);

        log.info("getChainMonitor end. useTime:{}",
            Duration.between(startTime, Instant.now()).toMillis());
        return new BaseResponse(ConstantCode.SUCCESS, metricDataList);
    }

    /**
     * query performance data.
     */
    @ApiOperation(value = "query performance data", notes = "query performance data")
    @ApiImplicitParams({@ApiImplicitParam(name = "beginDate", value = "start time"),
        @ApiImplicitParam(name = "endDate", value = "end time"),
        @ApiImplicitParam(name = "contrastBeginDate", value = "compare start time"),
        @ApiImplicitParam(name = "contrastEndDate", value = "compare end time"),
        @ApiImplicitParam(name = "gap", value = "time gap", dataType = "int"),
        @ApiImplicitParam(name = "frontId", value = "front id", dataType = "int"),
        @ApiImplicitParam(name = "chainId", value = "chain id", dataType = "int")
    })
    @GetMapping("/metrics/serverPerformance")
    public BaseResponse getPerformanceRatio(
        @RequestParam(required = false) @DateTimeFormat(
            iso = DATE_TIME) LocalDateTime beginDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DATE_TIME) LocalDateTime endDate,
        @RequestParam(required = false) @DateTimeFormat(
            iso = DATE_TIME) LocalDateTime contrastBeginDate,
        @RequestParam(required = false) @DateTimeFormat(
            iso = DATE_TIME) LocalDateTime contrastEndDate,
        @RequestParam(required = false, defaultValue = "1") int gap,
        @RequestParam(required = true) Integer frontId,
        @RequestParam(required = false) Integer chainId) throws Exception {
        Instant startTime = Instant.now();
        log.info("getPerformanceRatio start.", startTime.toEpochMilli());
        List<MetricData> metricDataList = dataService.findServerPerformanceByTime(chainId, frontId,
            beginDate, endDate, contrastBeginDate, contrastEndDate, gap);
        log.info("getPerformanceRatio end. useTime:{}",
            Duration.between(startTime, Instant.now()).toMillis());
        return new BaseResponse(ConstantCode.SUCCESS, metricDataList);
    }

    /* raw statistic data */

    @GetMapping(value = "/groupBasicData")
    public BasePageResponse getGroupBasicDataList(@RequestParam(required = true) Integer frontId,
            @RequestParam(required = false) Integer chainId,
            @RequestParam(required = false) Integer groupId,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false) @DateTimeFormat(
                    iso = ISO.DATE_TIME) LocalDateTime beginDate,
            @RequestParam(required = false) @DateTimeFormat(
                    iso = ISO.DATE_TIME) LocalDateTime endDate)
            throws BaseException {
        Instant startTime = Instant.now();
        log.info("start getGroupBasicDataList. frontId:{} groupId:{}", frontId, groupId);

        // check frontId
        if (!frontService.checkFrontIdExists(frontId)) {
            log.error("front not exists.");
            throw new BaseException(ConstantCode.INVALID_FRONT_ID);
        }

        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        // param
        BaseQueryParam queryParam = new BaseQueryParam();
        queryParam.setFrontId(frontId);
        queryParam.setChainId(chainId);
        queryParam.setGroupId(groupId);
        queryParam.setBeginDate(beginDate);
        queryParam.setEndDate(endDate);
        queryParam.setPageSize(pageSize);
        Integer count = dataService.countOfGroupBasicData(queryParam);
        if (count != null && count > 0) {
            Integer start =
                    Optional.ofNullable(pageNumber).map(page -> (page - 1) * pageSize).orElse(null);
            queryParam.setStart(start);

            List<TbGroupBasicData> listOfnode = dataService.getGroupBasicDataList(queryParam);
            pagesponse.setData(listOfnode);
            pagesponse.setTotalCount(count);
        }
        log.info("end getGroupBasicDataList. usedTime:{}",
                Duration.between(startTime, Instant.now()).toMillis());
        return pagesponse;
    }

    @GetMapping(value = "/nodeMonitor")
    public BasePageResponse getNodeMonitorList(@RequestParam(required = true) Integer frontId,
            @RequestParam(required = false) Integer chainId,
            @RequestParam(required = false) Integer groupId,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false) @DateTimeFormat(
                    iso = ISO.DATE_TIME) LocalDateTime beginDate,
            @RequestParam(required = false) @DateTimeFormat(
                    iso = ISO.DATE_TIME) LocalDateTime endDate)
            throws BaseException {
        Instant startTime = Instant.now();
        log.info("start getNodeMonitorInfo. frontId:{} groupId:{}", frontId, groupId);

        // check frontId
        if (!frontService.checkFrontIdExists(frontId)) {
            log.error("front not exists.");
            throw new BaseException(ConstantCode.INVALID_FRONT_ID);
        }

        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        // param
        BaseQueryParam queryParam = new BaseQueryParam();
        queryParam.setChainId(chainId);
        queryParam.setFrontId(frontId);
        queryParam.setGroupId(groupId);
        queryParam.setBeginDate(beginDate);
        queryParam.setEndDate(endDate);
        queryParam.setPageSize(pageSize);
        Integer count = dataService.countOfNodeMonitor(queryParam);
        if (count != null && count > 0) {
            Integer start =
                    Optional.ofNullable(pageNumber).map(page -> (page - 1) * pageSize).orElse(null);
            queryParam.setStart(start);

            List<TbNodeMonitor> listOfnode = dataService.getNodeMonitorList(queryParam);
            pagesponse.setData(listOfnode);
            pagesponse.setTotalCount(count);
        }
        log.info("end getNodeMonitorInfo. usedTime:{}",
                Duration.between(startTime, Instant.now()).toMillis());
        return pagesponse;
    }

    @GetMapping(value = "/serverPerformance")
    public BasePageResponse getServerPerformanceList(@RequestParam(required = true) Integer frontId,
            @RequestParam(required = false) Integer chainId,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false) @DateTimeFormat(
                    iso = ISO.DATE_TIME) LocalDateTime beginDate,
            @RequestParam(required = false) @DateTimeFormat(
                    iso = ISO.DATE_TIME) LocalDateTime endDate)
            throws BaseException {
        Instant startTime = Instant.now();
        log.info("start getServerPerformanceList. frontId:{}", frontId);

        // check frontId
        if (!frontService.checkFrontIdExists(frontId)) {
            log.error("front not exists.");
            throw new BaseException(ConstantCode.INVALID_FRONT_ID);
        }

        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        // param
        BaseQueryParam queryParam = new BaseQueryParam();
        queryParam.setChainId(chainId);
        queryParam.setFrontId(frontId);
        queryParam.setBeginDate(beginDate);
        queryParam.setEndDate(endDate);
        queryParam.setPageSize(pageSize);
        Integer count = dataService.countOfServerPerformance(queryParam);
        if (count != null && count > 0) {
            Integer start =
                    Optional.ofNullable(pageNumber).map(page -> (page - 1) * pageSize).orElse(null);
            queryParam.setStart(start);

            List<TbServerPerformance> listOfnode = dataService.getServerPerformanceList(queryParam);
            pagesponse.setData(listOfnode);
            pagesponse.setTotalCount(count);
        }
        log.info("end getServerPerformanceList. usedTime:{}",
                Duration.between(startTime, Instant.now()).toMillis());
        return pagesponse;
    }
}
