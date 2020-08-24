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
package com.webank.webase.stat.scheduler;

import com.webank.webase.stat.base.properties.ConstantProperties;
import com.webank.webase.stat.base.tools.HttpRequestTools;
import com.webank.webase.stat.front.FrontService;
import com.webank.webase.stat.restinterface.ChainMgrInterfaceService;
import com.webank.webase.stat.restinterface.entity.IpPortInfo;
import com.webank.webase.stat.restinterface.entity.RspChain;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class UpdateFrontInfoTask {

    @Autowired
    private FrontService frontService;
    @Autowired
    private ConstantProperties constants;
    @Autowired
    private ChainMgrInterfaceService chainMgrInterfaceService;

    @Scheduled(fixedDelayString = "${constant.updateFrontInfoInterval}", initialDelay = 1000)
    public void taskStart() {
        // fetch front list
        pullFrontList();
        // update front size info
        frontService.updateFrontInfo();
    }

    /**
     * get chain list and pull frontList by chain
     */
    private void pullFrontList() {
        log.info("start pullFrontList");
        IpPortInfo ipPortInfo = HttpRequestTools.getIpPort(constants.getChainMgrServer());
        String mgrIp = ipPortInfo.getIp();
        Integer mgrPort = ipPortInfo.getPort();
        List<RspChain> chainList = chainMgrInterfaceService.getChainListFromMgr(mgrIp, mgrPort);
        chainList.forEach(chain ->
            frontService.pullFrontList(chain.getChainId(), mgrIp, mgrPort));
        log.info("end pullFrontList");
    }
}
