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
package com.webank.webase.stat.group;

import com.webank.webase.stat.base.code.ConstantCode;
import com.webank.webase.stat.base.controller.BaseController;
import com.webank.webase.stat.base.entity.BasePageResponse;
import com.webank.webase.stat.base.exception.BaseException;
import com.webank.webase.stat.group.entity.TbGroup;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for processing group information.
 */
@Log4j2
@RestController
@RequestMapping("group")
public class GroupController extends BaseController {

    @Autowired
    private GroupService groupService;

    /**
     * query group list.
     */
    @GetMapping("/list/{frontId}")
    public BasePageResponse getAllGroup(@PathVariable("frontId") Integer frontId)
            throws BaseException {
        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start getAllGroup startTime:{}", startTime.toEpochMilli());

        // get group list
        Integer count = groupService.getGroupCount(frontId);
        if (count != null && count > 0) {
            List<TbGroup> groupList = groupService.getGroupList(frontId);
            pagesponse.setTotalCount(count);
            pagesponse.setData(groupList);
        }

        log.info("end getAllGroup useTime:{}",
                Duration.between(startTime, Instant.now()).toMillis());
        return pagesponse;
    }
}
