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

import com.webank.webase.stat.base.code.ConstantCode;
import com.webank.webase.stat.base.controller.BaseController;
import com.webank.webase.stat.base.entity.BasePageResponse;
import com.webank.webase.stat.base.entity.BaseQueryParam;
import com.webank.webase.stat.base.exception.BaseException;
import com.webank.webase.stat.data.entity.TbGasData;
import com.webank.webase.stat.data.entity.TbGroupBasicData;
import com.webank.webase.stat.data.entity.TbNetWorkData;
import com.webank.webase.stat.data.entity.TbNodeMonitor;
import com.webank.webase.stat.data.entity.TbServerPerformance;
import com.webank.webase.stat.front.FrontService;
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

    @GetMapping(value = "/groupBasicData")
    public BasePageResponse getGroupBasicDataList(@RequestParam(required = true) Integer frontId,
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

    @GetMapping(value = "/netWorkData")
    public BasePageResponse getNetWorkDataList(@RequestParam(required = true) Integer frontId,
            @RequestParam(required = false) Integer groupId,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false) @DateTimeFormat(
                    iso = ISO.DATE_TIME) LocalDateTime beginDate,
            @RequestParam(required = false) @DateTimeFormat(
                    iso = ISO.DATE_TIME) LocalDateTime endDate)
            throws BaseException {
        Instant startTime = Instant.now();
        log.info("start getNetWorkDataList. frontId:{} groupId:{}", frontId, groupId);

        // check frontId
        if (!frontService.checkFrontIdExists(frontId)) {
            log.error("front not exists.");
            throw new BaseException(ConstantCode.INVALID_FRONT_ID);
        }

        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        // param
        BaseQueryParam queryParam = new BaseQueryParam();
        queryParam.setFrontId(frontId);
        queryParam.setGroupId(groupId);
        queryParam.setBeginDate(beginDate);
        queryParam.setEndDate(endDate);
        queryParam.setPageSize(pageSize);
        Integer count = dataService.countOfNetWorkData(queryParam);
        if (count != null && count > 0) {
            Integer start =
                    Optional.ofNullable(pageNumber).map(page -> (page - 1) * pageSize).orElse(null);
            queryParam.setStart(start);

            List<TbNetWorkData> listOfnode = dataService.getNetWorkDataList(queryParam);
            pagesponse.setData(listOfnode);
            pagesponse.setTotalCount(count);
        }
        log.info("end getNetWorkDataList. usedTime:{}",
                Duration.between(startTime, Instant.now()).toMillis());
        return pagesponse;
    }

    @GetMapping(value = "/gasData")
    public BasePageResponse getGasDataList(@RequestParam(required = true) Integer frontId,
            @RequestParam(required = false) Integer groupId,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false) @DateTimeFormat(
                    iso = ISO.DATE_TIME) LocalDateTime beginDate,
            @RequestParam(required = false) @DateTimeFormat(
                    iso = ISO.DATE_TIME) LocalDateTime endDate)
            throws BaseException {
        Instant startTime = Instant.now();
        log.info("start getGasDataList. frontId:{} groupId:{}", frontId, groupId);

        // check frontId
        if (!frontService.checkFrontIdExists(frontId)) {
            log.error("front not exists.");
            throw new BaseException(ConstantCode.INVALID_FRONT_ID);
        }

        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        // param
        BaseQueryParam queryParam = new BaseQueryParam();
        queryParam.setFrontId(frontId);
        queryParam.setGroupId(groupId);
        queryParam.setBeginDate(beginDate);
        queryParam.setEndDate(endDate);
        queryParam.setPageSize(pageSize);
        Integer count = dataService.countOfGasData(queryParam);
        if (count != null && count > 0) {
            Integer start =
                    Optional.ofNullable(pageNumber).map(page -> (page - 1) * pageSize).orElse(null);
            queryParam.setStart(start);

            List<TbGasData> listOfnode = dataService.getGasDataList(queryParam);
            pagesponse.setData(listOfnode);
            pagesponse.setTotalCount(count);
        }
        log.info("end getGasDataList. usedTime:{}",
                Duration.between(startTime, Instant.now()).toMillis());
        return pagesponse;
    }

    @GetMapping(value = "/nodeMonitor")
    public BasePageResponse getNodeMonitorList(@RequestParam(required = true) Integer frontId,
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
