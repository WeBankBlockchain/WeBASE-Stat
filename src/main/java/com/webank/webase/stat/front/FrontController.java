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
package com.webank.webase.stat.front;


import com.webank.webase.stat.base.code.ConstantCode;
import com.webank.webase.stat.base.controller.BaseController;
import com.webank.webase.stat.base.entity.BasePageResponse;
import com.webank.webase.stat.base.entity.BaseResponse;
import com.webank.webase.stat.base.exception.BaseException;
import com.webank.webase.stat.front.entity.FrontParam;
import com.webank.webase.stat.front.entity.ReqNewFront;
import com.webank.webase.stat.front.entity.TbFront;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * front controller
 */
@Log4j2
@RestController
@RequestMapping("front")
public class FrontController extends BaseController {

    @Autowired
    private FrontService frontService;

    /**
     * add new front
     */
    @PostMapping("/add")
    public BaseResponse newFront(@RequestBody @Valid ReqNewFront reqNewFront,
            BindingResult result) {
        checkBindResult(result);
        Instant startTime = Instant.now();
        log.info("start newFront.");
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        TbFront tbFront = frontService.newFront(reqNewFront);
        baseResponse.setData(tbFront);
        log.info("end newFront useTime:{}", Duration.between(startTime, Instant.now()).toMillis());
        return baseResponse;
    }


    /**
     * qurey front info list.
     */
    @GetMapping(value = "/list")
    public BasePageResponse queryFrontList(
            @RequestParam(value = "frontId", required = false) Integer frontId)
            throws BaseException {
        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start queryFrontList.frontId:{}", frontId);

        // param
        FrontParam param = new FrontParam();
        param.setFrontId(frontId);

        // query front info
        int count = frontService.getFrontCount(param);
        pagesponse.setTotalCount(count);
        if (count > 0) {
            List<TbFront> list = frontService.getFrontList(param);
            pagesponse.setData(list);
        }

        log.info("end queryFrontList useTime:{} result:{}",
                Duration.between(startTime, Instant.now()).toMillis());
        return pagesponse;
    }

    /**
     * delete by frontId
     */
    @DeleteMapping(value = "/{frontId}")
    public BaseResponse removeFront(@PathVariable("frontId") Integer frontId) {
        Instant startTime = Instant.now();
        log.info("start removeFront. frontId:{}", frontId);
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);

        // remove
        frontService.removeByFrontId(frontId);

        log.info("end removeFront useTime:{}",
                Duration.between(startTime, Instant.now()).toMillis());
        return baseResponse;
    }
}
