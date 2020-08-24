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

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class of table tb_node_monitor.
 * using sigar record of cpu, disk, memory, rxbps, txbps
 */
@Data
@NoArgsConstructor
public class TbServerPerformance {

    private Long id;
    private Integer frontId;
    private BigDecimal cpuUseRatio;
    private BigDecimal diskUseRatio;
    private BigDecimal memoryUseRatio;
    private BigDecimal rxbps;
    private BigDecimal txbps;
    private Long timestamp;
    private Integer recordMonth;

    public TbServerPerformance(Long id, Integer frontId, BigDecimal cpuUseRatio,
            BigDecimal diskUseRatio, BigDecimal memoryUseRatio, BigDecimal rxbps, BigDecimal txbps,
            Long timestamp, Integer recordMonth) {
        this.id = id;
        this.frontId = frontId;
        this.cpuUseRatio = cpuUseRatio;
        this.diskUseRatio = diskUseRatio;
        this.memoryUseRatio = memoryUseRatio;
        this.rxbps = rxbps;
        this.txbps = txbps;
        this.timestamp = timestamp;
        this.recordMonth = recordMonth;
    }
}
