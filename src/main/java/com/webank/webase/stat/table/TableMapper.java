/**
 * Copyright 2014-2020  the original author or authors.
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

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TableMapper {

    void createTbFront();
    
    void createTbGroup();
    
    void createTbGroupBasicData(@Param("tableName") String tableName);
    
    void createTbNetworkData(@Param("tableName") String tableName);
    
    void createTbGasData(@Param("tableName") String tableName);
    
    void createTbNodeMonitor(@Param("tableName") String tableName);
    
    void createTbServerPerformance(@Param("tableName") String tableName);
    
    void dropTable(@Param("tableName") String tableName);

}