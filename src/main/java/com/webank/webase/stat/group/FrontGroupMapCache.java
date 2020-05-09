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


import com.webank.webase.stat.group.entity.TbGroup;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FrontGroupMapCache {

    @Autowired
    private GroupService groupService;

    private static Map<Integer, List<TbGroup>> mapList = new ConcurrentHashMap<>();

    /**
     * clear mapList.
     */
    public void clearMapList(Integer frontId) {
        mapList.remove(frontId);
    }

    /**
     * put mapList.
     */
    public Map<Integer, List<TbGroup>> putMapList(Integer frontId) {
        mapList.put(frontId, groupService.getGroupList(frontId));
        return mapList;
    }

    /**
     * getMapByFrontId.
     */
    public List<TbGroup> getMapByFrontId(Integer frontId) {
        if (mapList == null || mapList.get(frontId) == null) {
            mapList = putMapList(frontId);
        }
        return mapList.get(frontId);
    }
}
