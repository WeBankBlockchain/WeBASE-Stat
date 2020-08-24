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

import com.webank.webase.stat.base.entity.BaseQueryParam;
import com.webank.webase.stat.data.entity.TbGasData;
import com.webank.webase.stat.data.entity.TbGroupBasicData;
import com.webank.webase.stat.data.entity.TbNetWorkData;
import com.webank.webase.stat.data.entity.TbNodeMonitor;
import com.webank.webase.stat.data.entity.TbServerPerformance;
import com.webank.webase.stat.data.mapper.GasDataMapper;
import com.webank.webase.stat.data.mapper.GroupBasicDataMapper;
import com.webank.webase.stat.data.mapper.NetWorkDataMapper;
import com.webank.webase.stat.data.mapper.NodeMonitorMapper;
import com.webank.webase.stat.data.mapper.ServerPerformanceMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * services for data.
 */
@Service
public class DataService {
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
}
