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
package com.webank.webase.stat.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class of table tb_network_data.
 * flow size of node consensus and node channel with sdk
 */
@Data
@NoArgsConstructor
public class TbNetWorkData {

    private Long id;
    private Integer frontId;
    private Integer groupId;
    private Long totalIn;
    private Long totalOut;
    private Long timestamp;

    public TbNetWorkData(Long id, Integer frontId, Integer groupId, Long totalIn, Long totalOut,
            Long timestamp) {
        this.id = id;
        this.frontId = frontId;
        this.groupId = groupId;
        this.totalIn = totalIn;
        this.totalOut = totalOut;
        this.timestamp = timestamp;
    }
}
