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
package com.webank.webase.stat.data.mapper;

import com.webank.webase.stat.base.entity.BaseQueryParam;
import com.webank.webase.stat.data.entity.TbServerPerformance;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * mapper for table tb_server_performance.
 */
@Repository
public interface ServerPerformanceMapper {

    /**
     * add info
     */
    int add(TbServerPerformance data);

    /**
     * getList.
     */
    TbServerPerformance getMaxData(@Param("chainId") Integer chainId, @Param("frontId") Integer frontId);

    /**
     * getCount.
     */
    int getCount(BaseQueryParam queryParam);

    /**
     * getList.
     */
    List<TbServerPerformance> getList(BaseQueryParam queryParam);
}
