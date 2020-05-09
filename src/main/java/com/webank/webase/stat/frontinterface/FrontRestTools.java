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
package com.webank.webase.stat.frontinterface;

import com.alibaba.fastjson.JSON;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * about http request for WeBASE-Front.
 */
public class FrontRestTools {

    public static final String FRONT_URL = "http://%1s:%2d/WeBASE-Front/%3s";
    public static final String URI_BLOCK_NUMBER = "web3/blockNumber";
    public static final String URI_BLOCK_BY_NUMBER = "web3/blockByNumber/%1d";
    public static final String URI_BLOCK_BY_HASH = "web3/blockByHash/%1s";
    public static final String URI_TRANS_TOTAL = "web3/transaction-total";
    public static final String URI_TRANS_BY_HASH = "web3/transaction/%1s";
    public static final String URI_TRANS_RECEIPT = "web3/transactionReceipt/%1s";
    public static final String URI_GROUP_PLIST = "web3/groupList";
    public static final String FRONT_PERFORMANCE_RATIO = "performance";
    public static final String FRONT_PERFORMANCE_PAGING = "performance/pagingQuery";
    public static final String FRONT_PERFORMANCE_CONFIG = "performance/config";
    public static final String URI_CHAIN = "chain";
    public static final String URI_NODE_MONITOR = "chain/pagingQuery";
    public static final String URI_GET_GROUP_SIZE_INFOS = "chain/getGroupSizeInfos";
    public static final String URI_CHARGING_GET_NETWORK_DATA = "charging/getNetWorkData";
    public static final String URI_CHARGING_GET_TXGASDATA = "charging/getTxGasData";
    public static final String URI_CHARGING_DELETE_DATA = "charging/deleteData";

    // 不需要在url中包含groupId的
    private static final List<String> URI_NOT_CONTAIN_GROUP_ID =
            Arrays.asList(URI_CHARGING_GET_NETWORK_DATA, URI_CHARGING_GET_TXGASDATA,
                    URI_CHARGING_DELETE_DATA, URI_CHAIN, URI_NODE_MONITOR, FRONT_PERFORMANCE_RATIO,
                    FRONT_PERFORMANCE_PAGING, FRONT_PERFORMANCE_CONFIG, URI_GET_GROUP_SIZE_INFOS);

    /**
     * append groupId to uri.
     */
    public static String uriAddGroupId(Integer groupId, String uri) {
        if (groupId == null || StringUtils.isBlank(uri)) {
            return null;
        }

        final String tempUri = uri.contains("?") ? uri.substring(0, uri.indexOf("?")) : uri;

        long count = URI_NOT_CONTAIN_GROUP_ID.stream().filter(u -> u.contains(tempUri)).count();
        if (count > 0) {
            return uri;
        }
        return groupId + "/" + uri;
    }

    /**
     * build httpEntity
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static HttpEntity buildHttpEntity(Object param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String paramStr = null;
        if (Objects.nonNull(param)) {
            paramStr = JSON.toJSONString(param);
        }
        HttpEntity requestEntity = new HttpEntity(paramStr, headers);
        return requestEntity;
    }
}
