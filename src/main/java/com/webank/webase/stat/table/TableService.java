/*
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

package com.webank.webase.stat.table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * table service.
 *
 */
@Service
public class TableService {
    @Autowired
    private TableMapper tableMapper;

    // include partition
    public static final String PREFIX_TB_GROUP_BASIC_DATA = "tb_group_basic_data";
    public static final String PREFIX_TB_NODE_MONITOR = "tb_node_monitor";
    public static final String PREFIX_TB_SERVER_PERFORMANCE = "tb_server_performance";
    // deprecated
    // public static final String PREFIX_TB_NETWORK_DATA = "tb_network_data_";
    // public static final String PREFIX_TB_GAS_DATA = "tb_gas_data_";

//    public void createStatTable() {
//        tableMapper.createTbGroupBasicData(PREFIX_TB_GROUP_BASIC_DATA);
//        tableMapper.createTbNodeMonitor(PREFIX_TB_NODE_MONITOR);
//        tableMapper.createTbServerPerformance(PREFIX_TB_SERVER_PERFORMANCE);
//    }

    public void dropTable() {
        tableMapper.dropTable(PREFIX_TB_GROUP_BASIC_DATA);
        tableMapper.dropTable(PREFIX_TB_NODE_MONITOR);
        tableMapper.dropTable(PREFIX_TB_SERVER_PERFORMANCE);
    }
}
