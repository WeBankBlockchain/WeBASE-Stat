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

import java.math.BigInteger;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class of table tb_node_monitor.
 * node's group height and pbft view data
 */
@Data
@NoArgsConstructor
public class TbNodeMonitor {

    private Long id;
    private Integer frontId;
    private Integer chainId;
    private Integer groupId;
    private BigInteger blockHeight;
    private BigInteger pbftView;
    private BigInteger pendingTransactionCount;
    private Long timestamp;
    private Integer recordMonth;

    public TbNodeMonitor(Long id, Integer chainId, Integer frontId, Integer groupId, BigInteger blockHeight,
            BigInteger pbftView, BigInteger pendingTransactionCount, Long timestamp, Integer recordMonth) {
        this.id = id;
        this.chainId = chainId;
        this.frontId = frontId;
        this.groupId = groupId;
        this.blockHeight = blockHeight;
        this.pbftView = pbftView;
        this.pendingTransactionCount = pendingTransactionCount;
        this.timestamp = timestamp;
        this.recordMonth = recordMonth;
    }
}
