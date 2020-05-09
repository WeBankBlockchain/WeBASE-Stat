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
package com.webank.webase.stat.group;

import com.webank.webase.stat.front.FrontService;
import com.webank.webase.stat.front.entity.TbFront;
import com.webank.webase.stat.frontinterface.FrontInterfaceService;
import com.webank.webase.stat.group.entity.TbGroup;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * services for group data.
 */
@Log4j2
@Service
public class GroupService {

    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private FrontService frontService;
    @Autowired
    private FrontInterfaceService frontInterfaceService;
    @Lazy
    @Autowired
    private FrontGroupMapCache frontGroupMapCache;

    /**
     * save group
     */
    public void saveGroup(TbGroup tbGroup) {
        groupMapper.add(tbGroup);
    }

    /**
     * get group count
     */
    public int getGroupCount(Integer frontId) {
        Integer count = groupMapper.getCount(frontId);
        return count == null ? 0 : count;
    }

    /**
     * get group list
     */
    public List<TbGroup> getGroupList(Integer frontId) {
        return groupMapper.getList(frontId);
    }

    /**
     * reset groupList.
     */
    @Async(value = "asyncExecutor")
    @Transactional
    public synchronized void resetGroupList() {
        Instant startTime = Instant.now();
        log.info("start resetGroupList.", startTime.toEpochMilli());

        // get all front
        List<TbFront> frontList = frontService.getFrontList(null);
        if (CollectionUtils.isEmpty(frontList)) {
            log.info("not fount any front.");
            return;
        }
        // get group from chain
        for (TbFront front : frontList) {
            String frontIp = front.getFrontIp();
            int frontPort = front.getFrontPort();
            // query group list
            List<String> groupIdList;
            try {
                groupIdList = frontInterfaceService.getGroupList(frontIp, frontPort);
            } catch (Exception ex) {
                log.warn("fail getGroupList frontId:{}.", front.getFrontId(), ex);
                continue;
            }
            // save group
            for (String groupId : groupIdList) {
                saveGroup(new TbGroup(front.getFrontId(), Integer.valueOf(groupId), null));
            }
            // check group
            checkAndRemoveInvalidGroup(front.getFrontId(), groupIdList);
            // clear Cache
            frontGroupMapCache.clearMapList(front.getFrontId());
        }

        log.info("end resetGroupList. useTime:{} ",
                Duration.between(startTime, Instant.now()).toMillis());
    }

    /**
     * remove by groupId.
     */
    public void removeByGroupId(int frontId, int groupId) {
        if (frontId == 0 || groupId == 0) {
            return;
        }
        groupMapper.remove(frontId, groupId);
    }

    /**
     * remove by frontId.
     */
    public void removeByFrontId(int frontId) {
        if (frontId == 0) {
            return;
        }
        groupMapper.remove(frontId, null);
    }

    /**
     * check group.
     */
    private void checkAndRemoveInvalidGroup(Integer frontId, List<String> allGroupOnChain) {
        if (CollectionUtils.isEmpty(allGroupOnChain)) {
            return;
        }
        List<TbGroup> allLocalGroup = getGroupList(frontId);
        for (TbGroup localGroup : allLocalGroup) {
            int localGroupId = localGroup.getGroupId();
            long count = allGroupOnChain.stream().filter(id -> Integer.valueOf(id) == localGroupId)
                    .count();
            if (count == 0) {
                // remove group
                removeByGroupId(frontId, localGroupId);
            }
        }
    }

}
