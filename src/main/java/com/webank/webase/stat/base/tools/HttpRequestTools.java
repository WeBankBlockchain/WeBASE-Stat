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
package com.webank.webase.stat.base.tools;


import com.webank.webase.stat.base.code.ConstantCode;
import com.webank.webase.stat.base.exception.BaseException;
import com.webank.webase.stat.restinterface.entity.IpPortInfo;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class HttpRequestTools {

    /**
     * get uri.
     */
    public static String getUri(HttpServletRequest httpRequest) {
        String uri = httpRequest.getRequestURI().replace("//", "/");
        String contextPath = httpRequest.getContextPath();
        return StringUtils.removeStart(uri, contextPath);
    }


    /**
     * convert map to query params
     * ex: uri:permission,
     *     params: (groupId, 1) (address, 0x01)
     *
     * result: permission?groupId=1&address=0x01
     */
    public static String getQueryUri(String uriHead, Map<String, String> map) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            params.add(entry.getKey(), entry.getValue());
        }

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .queryParams(params).build();

        return uriHead + uriComponents.toString();
    }

    /**
     * get first ip port in rest properties
     * ex: 127.0.0.1:6003,127.0.0.1:6004, get previous one
     * @param ipPort
     * @return
     */
    public static IpPortInfo getIpPort(String ipPort) {
        String[] ipAndPort = ipPort.trim().split(":");
        if (ipAndPort.length < 2) {
            throw new BaseException(ConstantCode.REST_SERVER_ADDRESS_NOT_CONFIG);
        }
        String ip = ipAndPort[0];
        int port = Integer.parseInt(ipAndPort[1]);
        return new IpPortInfo(ip, port);
    }
}
