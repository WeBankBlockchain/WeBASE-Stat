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

package com.webank.webase.stat.restinterface;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.webank.webase.stat.base.code.ConstantCode;
import com.webank.webase.stat.base.entity.BasePageResponse;
import com.webank.webase.stat.base.entity.BaseResponse;
import com.webank.webase.stat.base.exception.BaseException;
import com.webank.webase.stat.base.tools.JacksonUtils;
import com.webank.webase.stat.restinterface.entity.NetWorkData;
import com.webank.webase.stat.restinterface.entity.RspChain;
import com.webank.webase.stat.restinterface.entity.RspFront;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
public class ChainMgrInterfaceService {


    @Autowired
    private RestTemplate restTemplate;

    public List<RspChain> getChainListFromMgr(String chainMgrIp, Integer chainMgrPort) {
        log.info("getChainListFromMgr chainMgrIp:{},chainMgrPort:{}", chainMgrIp, chainMgrPort);
        String uri = String.format(RestTools.CHAIN_MGR_URI_CHAIN_LIST);
        BaseResponse response = getFromChainMgr(chainMgrIp, chainMgrPort, uri, BaseResponse.class);
        List<RspChain> list =
            JacksonUtils.stringToObj(JacksonUtils.objToString(response.getData()),
                new TypeReference<List<RspChain>>() {});
        return list;
    }

    public List<RspFront> getFrontListFromMgr(Integer chainId, String chainMgrIp, Integer chainMgrPort) {
        log.info("getFrontListFromMgr chainMgrIp:{},chainMgrPort:{},chainId:{}", chainMgrIp, chainMgrPort,chainId);
        String uri = String.format(RestTools.CHAIN_MGR_URI_FRONT_LIST, chainId);
        BaseResponse response = getFromChainMgr(chainMgrIp, chainMgrPort, uri, BaseResponse.class);
        List<RspFront> list =
            JacksonUtils.stringToObj(JacksonUtils.objToString(response.getData()),
                new TypeReference<List<RspFront>>() {});
        return list;
    }

    /**
     * get from front.
     */
    private <T> T getFromChainMgr(String chainMgrIp, Integer chainMgrPort, String uri,
        Class<T> clazz) {
        log.debug("start getFromFront. chainMgrIp:{} chainMgrPort:{}  uri:{}",
            chainMgrIp, chainMgrPort.toString(), uri);
        return requestChainMgr(chainMgrIp, chainMgrPort, HttpMethod.GET, uri, null, clazz);
    }

    /**
     * request front.
     */
    @SuppressWarnings("rawtypes")
    private <T> T requestChainMgr(String chainMgrIp, Integer chainMgrPort,
        HttpMethod method, String uri, Object param, Class<T> clazz) {
        log.debug(
            "start requestChainMgr. chainMgrIp:{} chainMgrPort:{} httpMethod:{} uri:{}",
            chainMgrIp, chainMgrPort, method.toString(), uri);

        String url = String.format(RestTools.CHAIN_MANAGER_URL, chainMgrIp, chainMgrPort, uri);
        log.debug("requestChainMgr. url:{}", url);

        try {
            HttpEntity entity = RestTools.buildHttpEntity(param);// build entity
            ResponseEntity<T> response = restTemplate.exchange(url, method, entity, clazz);
            return response.getBody();
        } catch (ResourceAccessException e) {
            log.error("requestChainMgr ResourceAccessException.", e);
            throw new BaseException(ConstantCode.REQUEST_CHAIN_MGR_FAIL);
        } catch (HttpStatusCodeException e) {
            JsonNode error = JacksonUtils.stringToJsonNode(e.getResponseBodyAsString());
            log.error("requestChainMgr fail. error:{}", error);
            if (ObjectUtils.isEmpty(error.get("errorMessage"))) {
                throw new BaseException(ConstantCode.REQUEST_CHAIN_MGR_EXCEPTION);
            }
            String errorMessage = error.get("errorMessage").asText();
            if (errorMessage.contains("code")) {
                JsonNode errorInside = error.get("errorMessage").get("error");
                throw new BaseException(errorInside.get("code").asInt(),
                    errorInside.get("message").asText());
            }
            throw new BaseException(error.get("code").asInt(), errorMessage);
        }
    }
}
