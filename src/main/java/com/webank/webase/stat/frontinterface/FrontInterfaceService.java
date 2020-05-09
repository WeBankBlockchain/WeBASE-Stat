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
import com.alibaba.fastjson.JSONObject;
import com.webank.webase.stat.base.code.ConstantCode;
import com.webank.webase.stat.base.entity.BasePageResponse;
import com.webank.webase.stat.base.exception.BaseException;
import com.webank.webase.stat.base.tools.HttpRequestTools;
import com.webank.webase.stat.front.entity.TransactionCount;
import com.webank.webase.stat.frontinterface.entity.GroupSizeInfo;
import com.webank.webase.stat.frontinterface.entity.NetWorkData;
import com.webank.webase.stat.frontinterface.entity.NodeMonitor;
import com.webank.webase.stat.frontinterface.entity.Performance;
import com.webank.webase.stat.frontinterface.entity.PerformanceConfig;
import com.webank.webase.stat.frontinterface.entity.TxGasData;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.web3j.protocol.core.methods.response.BcosBlock.Block;
import org.fisco.bcos.web3j.protocol.core.methods.response.Transaction;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
public class FrontInterfaceService {

    @Autowired
    private RestTemplate restTemplate;

    @SuppressWarnings("unchecked")
    public List<String> getGroupList(String frontIp, Integer frontPort) {
        Integer groupId = Integer.MAX_VALUE;
        return getFromFront(groupId, frontIp, frontPort, FrontRestTools.URI_GROUP_PLIST,
                List.class);
    }

    public BigInteger getBlockNumber(String frontIp, Integer frontPort, Integer groupId) {
        return getFromFront(groupId, frontIp, frontPort, FrontRestTools.URI_BLOCK_NUMBER,
                BigInteger.class);
    }

    public Block getBlockByNumber(String frontIp, Integer frontPort, Integer groupId,
            BigInteger blockNmber) {
        String uri = String.format(FrontRestTools.URI_BLOCK_BY_NUMBER, blockNmber);
        return getFromFront(groupId, frontIp, frontPort, uri, Block.class);
    }

    public TransactionCount getTotalTransactionCount(String frontIp, Integer frontPort,
            Integer groupId) {
        return getFromFront(groupId, frontIp, frontPort, FrontRestTools.URI_TRANS_TOTAL,
                TransactionCount.class);
    }

    public Transaction getTransactionByHash(String frontIp, Integer frontPort, Integer groupId,
            String transHash) {
        String uri = String.format(FrontRestTools.URI_TRANS_BY_HASH, transHash);
        return getFromFront(groupId, frontIp, frontPort, uri, Transaction.class);
    }

    public TransactionReceipt getTransactionReceipt(String frontIp, Integer frontPort,
            Integer groupId, String transHash) {
        String uri = String.format(FrontRestTools.URI_TRANS_RECEIPT, transHash);
        return getFromFront(groupId, frontIp, frontPort, uri, TransactionReceipt.class);
    }

    public List<NetWorkData> getNetWorkData(String frontIp, Integer frontPort, Integer groupId,
            Integer pageSize, Integer pageNumber, LocalDateTime beginDate, LocalDateTime endDate) {
        Map<String, String> map = new HashMap<>();
        map.put("groupId", String.valueOf(groupId));
        map.put("pageSize", String.valueOf(pageSize));
        map.put("pageNumber", String.valueOf(pageNumber));
        if (beginDate != null) {
            map.put("beginDate", String.valueOf(beginDate));
        }
        if (endDate != null) {
            map.put("endDate", String.valueOf(endDate));
        }

        String uri =
                HttpRequestTools.getQueryUri(FrontRestTools.URI_CHARGING_GET_NETWORK_DATA, map);
        BasePageResponse frontRsp =
                getFromFront(groupId, frontIp, frontPort, uri, BasePageResponse.class);
        List<NetWorkData> list =
                JSON.parseArray(JSON.toJSONString(frontRsp.getData()), NetWorkData.class);
        return list;
    }

    public List<TxGasData> getTxGasData(String frontIp, Integer frontPort, Integer groupId,
            Integer pageSize, Integer pageNumber, LocalDateTime beginDate, LocalDateTime endDate,
            String transHash) {
        Map<String, String> map = new HashMap<>();
        map.put("groupId", String.valueOf(groupId));
        map.put("pageSize", String.valueOf(pageSize));
        map.put("pageNumber", String.valueOf(pageNumber));
        if (beginDate != null) {
            map.put("beginDate", String.valueOf(beginDate));
        }
        if (endDate != null) {
            map.put("endDate", String.valueOf(endDate));
        }
        if (transHash != null) {
            map.put("transHash", transHash);
        }

        String uri = HttpRequestTools.getQueryUri(FrontRestTools.URI_CHARGING_GET_TXGASDATA, map);
        BasePageResponse frontRsp =
                getFromFront(groupId, frontIp, frontPort, uri, BasePageResponse.class);
        List<TxGasData> list =
                JSON.parseArray(JSON.toJSONString(frontRsp.getData()), TxGasData.class);
        return list;
    }

    public Object deleteLogData(String frontIp, Integer frontPort, Integer groupId, Integer type,
            LocalDateTime keepEndDate) {
        log.debug("start deleteLogData. groupId:{}", groupId);
        Map<String, String> map = new HashMap<>();
        map.put("groupId", String.valueOf(groupId));
        map.put("type", String.valueOf(type));
        map.put("keepEndDate", String.valueOf(keepEndDate));

        String uri = HttpRequestTools.getQueryUri(FrontRestTools.URI_CHARGING_DELETE_DATA, map);

        Object frontRsp = deleteToFront(groupId, frontIp, frontPort, uri, null, Object.class);
        log.debug("end deleteLogData. frontRsp:{}", JSON.toJSONString(frontRsp));
        return frontRsp;
    }

    public List<NodeMonitor> getNodeMonitor(String frontIp, Integer frontPort, Integer groupId,
            Integer pageSize, Integer pageNumber, LocalDateTime beginDate, LocalDateTime endDate) {
        Map<String, String> map = new HashMap<>();
        map.put("groupId", String.valueOf(groupId));
        map.put("pageSize", String.valueOf(pageSize));
        map.put("pageNumber", String.valueOf(pageNumber));
        if (beginDate != null) {
            map.put("beginDate", String.valueOf(beginDate));
        }
        if (endDate != null) {
            map.put("endDate", String.valueOf(endDate));
        }

        String uri = HttpRequestTools.getQueryUri(FrontRestTools.URI_NODE_MONITOR, map);
        BasePageResponse frontRsp =
                getFromFront(groupId, frontIp, frontPort, uri, BasePageResponse.class);
        List<NodeMonitor> list =
                JSON.parseArray(JSON.toJSONString(frontRsp.getData()), NodeMonitor.class);
        return list;
    }

    public List<Performance> getServerPerformance(String frontIp, Integer frontPort,
            Integer pageSize, Integer pageNumber, LocalDateTime beginDate, LocalDateTime endDate) {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", String.valueOf(pageSize));
        map.put("pageNumber", String.valueOf(pageNumber));
        if (beginDate != null) {
            map.put("beginDate", String.valueOf(beginDate));
        }
        if (endDate != null) {
            map.put("endDate", String.valueOf(endDate));
        }

        String uri = HttpRequestTools.getQueryUri(FrontRestTools.FRONT_PERFORMANCE_PAGING, map);
        BasePageResponse frontRsp =
                getFromFront(Integer.MAX_VALUE, frontIp, frontPort, uri, BasePageResponse.class);
        List<Performance> list =
                JSON.parseArray(JSON.toJSONString(frontRsp.getData()), Performance.class);
        return list;
    }

    public Object getNodeMonitorInfo(String frontIp, Integer frontPort, Integer groupId,
            Map<String, String> map) {
        String uri = HttpRequestTools.getQueryUri(FrontRestTools.URI_CHAIN, map);
        Object frontRsp = getFromFront(groupId, frontIp, frontPort, uri, Object.class);
        return frontRsp;
    }

    public Object getPerformanceRatio(String frontIp, Integer frontPort, Map<String, String> map) {
        String uri = HttpRequestTools.getQueryUri(FrontRestTools.FRONT_PERFORMANCE_RATIO, map);
        Object frontRsp = getFromFront(Integer.MAX_VALUE, frontIp, frontPort, uri, Object.class);
        return frontRsp;
    }

    public PerformanceConfig getPerformanceConfig(String frontIp, Integer frontPort) {
        Integer groupId = Integer.MAX_VALUE;
        return getFromFront(groupId, frontIp, frontPort, FrontRestTools.FRONT_PERFORMANCE_CONFIG,
                PerformanceConfig.class);
    }

    public List<GroupSizeInfo> getGroupSizeInfos(String frontIp, Integer frontPort) {
        Integer groupId = Integer.MAX_VALUE;
        String result = getFromFront(groupId, frontIp, frontPort,
                FrontRestTools.URI_GET_GROUP_SIZE_INFOS, String.class);
        List<GroupSizeInfo> list = JSON.parseArray(result, GroupSizeInfo.class);
        return list;
    }

    /**
     * get from front.
     */
    private <T> T getFromFront(Integer groupId, String frontIp, Integer frontPort, String uri,
            Class<T> clazz) {
        log.debug("start getFromFront. groupId:{} frontIp:{} frontPort:{}  uri:{}", groupId,
                frontIp, frontPort.toString(), uri);
        return requestFront(groupId, frontIp, frontPort, HttpMethod.GET, uri, null, clazz);
    }

    /**
     * delete to front.
     */
    private <T> T deleteToFront(Integer groupId, String frontIp, Integer frontPort, String uri,
            Object param, Class<T> clazz) {
        log.debug("start deleteToFront. groupId:{} frontIp:{} frontPort:{}  uri:{}", groupId,
                frontIp, frontPort.toString(), uri);
        return requestFront(groupId, frontIp, frontPort, HttpMethod.DELETE, uri, param, clazz);
    }

    /**
     * request front.
     */
    @SuppressWarnings("rawtypes")
    private <T> T requestFront(Integer groupId, String frontIp, Integer frontPort,
            HttpMethod method, String uri, Object param, Class<T> clazz) {
        log.debug(
                "start requestFront. groupId:{} frontIp:{} frontPort:{} " + "httpMethod:{} uri:{}",
                groupId, frontIp, frontPort, method.toString(), uri);

        uri = FrontRestTools.uriAddGroupId(groupId, uri);
        String url = String.format(FrontRestTools.FRONT_URL, frontIp, frontPort, uri);
        log.debug("requestFront. url:{}", url);

        try {
            HttpEntity entity = FrontRestTools.buildHttpEntity(param);// build entity
            ResponseEntity<T> response = restTemplate.exchange(url, method, entity, clazz);
            return response.getBody();
        } catch (ResourceAccessException e) {
            log.error("requestFront ResourceAccessException.", e);
            throw new BaseException(ConstantCode.REQUEST_FRONT_FAIL);
        } catch (HttpStatusCodeException e) {
            JSONObject error = JSONObject.parseObject(e.getResponseBodyAsString());
            log.error("requestFront fail. error:{}", JSON.toJSONString(error));
            String errorMessage = error.getString("errorMessage");
            if (StringUtils.isBlank(errorMessage)) {
                throw new BaseException(ConstantCode.REQUEST_NODE_EXCEPTION);
            }
            if (errorMessage.contains("code")) {
                JSONObject errorInside = JSONObject.parseObject(
                        JSONObject.parseObject(error.getString("errorMessage")).getString("error"));
                throw new BaseException(errorInside.getInteger("code"),
                        errorInside.getString("message"));
            }
            throw new BaseException(error.getInteger("code"), errorMessage);
        }
    }
}
