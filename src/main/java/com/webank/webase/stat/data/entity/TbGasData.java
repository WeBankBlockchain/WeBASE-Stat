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
 * Entity class of table tb_gas_data.
 */
@Data
@NoArgsConstructor
public class TbGasData {

    private Long id;
    private Integer frontId;
    private Integer groupId;
    private String transHash;
    private Long gasUsed;
    private Long timestamp;

    public TbGasData(Long id, Integer frontId, Integer groupId, String transHash, Long gasUsed,
            Long timestamp) {
        this.id = id;
        this.frontId = frontId;
        this.groupId = groupId;
        this.transHash = transHash;
        this.gasUsed = gasUsed;
        this.timestamp = timestamp;
    }
}
