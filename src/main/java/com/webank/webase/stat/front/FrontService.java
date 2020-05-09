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
package com.webank.webase.stat.front;

import com.webank.webase.stat.base.code.ConstantCode;
import com.webank.webase.stat.base.exception.BaseException;
import com.webank.webase.stat.base.tools.CommonUtils;
import com.webank.webase.stat.front.entity.FrontParam;
import com.webank.webase.stat.front.entity.ReqNewFront;
import com.webank.webase.stat.front.entity.TbFront;
import com.webank.webase.stat.frontinterface.FrontInterfaceService;
import com.webank.webase.stat.frontinterface.entity.PerformanceConfig;
import com.webank.webase.stat.group.GroupService;
import com.webank.webase.stat.group.entity.TbGroup;
import com.webank.webase.stat.table.TableService;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/**
 * service of web3.
 */
@Log4j2
@Service
public class FrontService {

    @Autowired
    private TableService tableService;
    @Autowired
    private FrontMapper frontMapper;
    @Lazy
    @Autowired
    private GroupService groupService;
    @Autowired
    private FrontInterfaceService frontInterfaceService;

    /**
     * add new front
     */
    @Transactional
    public TbFront newFront(ReqNewFront reqNewFront) {
        // check frontId
        Integer frontId = reqNewFront.getFrontId();
        if (checkFrontIdExists(frontId)) {
            throw new BaseException(ConstantCode.FRONT_EXISTS);
        }

        // check front ip and port
        String frontIp = reqNewFront.getFrontIp();
        Integer frontPort = reqNewFront.getFrontPort();
        if (checkIpPortExists(frontIp, frontPort)) {
            throw new BaseException(ConstantCode.FRONT_EXISTS);
        }
        CommonUtils.checkServerConnect(frontIp, frontPort);

        // query front info
        List<String> groupIdList = frontInterfaceService.getGroupList(frontIp, frontPort);
        PerformanceConfig performanceConfig =
                frontInterfaceService.getPerformanceConfig(frontIp, frontPort);
        // copy attribute
        TbFront tbFront = new TbFront();
        BeanUtils.copyProperties(reqNewFront, tbFront);
        BeanUtils.copyProperties(performanceConfig, tbFront);
        // save front info
        frontMapper.add(tbFront);
        // save group
        for (String groupId : groupIdList) {
            groupService
                    .saveGroup(new TbGroup(tbFront.getFrontId(), Integer.valueOf(groupId), null));
        }
        // create tables by frontId
        tableService.createTable(frontId);

        return getById(frontId);
    }

    /**
     * updateFrontInfo.
     */
    @Async(value = "asyncExecutor")
    public void updateFrontInfo() {
        Instant startTime = Instant.now();
        log.info("start updateFrontInfo.", startTime.toEpochMilli());

        // get all front
        List<TbFront> frontList = getFrontList(null);
        if (CollectionUtils.isEmpty(frontList)) {
            log.info("not fount any front.");
            return;
        }
        // get group from chain
        for (TbFront front : frontList) {
            String frontIp = front.getFrontIp();
            int frontPort = front.getFrontPort();
            // query performanceConfig
            PerformanceConfig performanceConfig;
            try {
                performanceConfig = frontInterfaceService.getPerformanceConfig(frontIp, frontPort);
            } catch (Exception ex) {
                log.warn("fail getPerformanceConfig frontId:{}.", front.getFrontId(), ex);
                continue;
            }
            // copy attribute
            BeanUtils.copyProperties(performanceConfig, front);
            // save front info
            frontMapper.add(front);
        }
        log.info("end updateFrontInfo. useTime:{} ",
                Duration.between(startTime, Instant.now()).toMillis());
    }

    /**
     * get front count
     */
    public int getFrontCount(FrontParam param) {
        Integer count = frontMapper.getCount(param);
        return count == null ? 0 : count;
    }

    /**
     * get front list
     */
    public List<TbFront> getFrontList(FrontParam param) {
        return frontMapper.getList(param);
    }

    public boolean checkFrontIdExists(Integer frontId) {
        TbFront tbFront = getById(frontId);
        if (ObjectUtils.isEmpty(tbFront)) {
            return false;
        }
        return true;
    }

    public boolean checkIpPortExists(String frontIp, Integer frontPort) {
        FrontParam param = new FrontParam();
        param.setFrontIp(frontIp);
        param.setFrontPort(frontPort);
        int count = getFrontCount(param);
        if (count > 0) {
            return true;
        }
        return false;
    }

    public TbFront getById(Integer frontId) {
        if (frontId == 0) {
            return null;
        }
        return frontMapper.getById(frontId);
    }

    /**
     * remove front by frontId
     */
    public void removeByFrontId(int frontId) {
        // check frontId
        TbFront tbFront = getById(frontId);
        if (tbFront == null) {
            throw new BaseException(ConstantCode.INVALID_FRONT_ID);
        }
        // remove front
        frontMapper.removeById(frontId);
        // remove group
        groupService.removeByFrontId(frontId);
        // drop tables
        tableService.dropTable(frontId);
    }
}
