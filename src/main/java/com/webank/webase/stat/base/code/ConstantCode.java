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
package com.webank.webase.stat.base.code;

/**
 * A-BB-CCC A:error level. <br/>
 * 1:system exception <br/>
 * 2:business exception <br/>
 * B:project number <br/>
 * WeBASE-Chain-Manager:05 <br/>
 * C: error code <br/>
 */
public class ConstantCode {

    /* return success */
    public static final RetCode SUCCESS = RetCode.mark(0, "success");

    /* system exception */
    public static final RetCode SYSTEM_EXCEPTION = RetCode.mark(105000, "system exception");

    /* Business exception */
    public static final RetCode INVALID_FRONT_ID = RetCode.mark(205000, "invalid front id");

    public static final RetCode FRONT_EXISTS = RetCode.mark(205001, "front already exists");

    public static final RetCode REQUEST_FRONT_FAIL =
            RetCode.mark(205002, "request front fail, please check front");

    public static final RetCode SERVER_CONNECT_FAIL = RetCode.mark(205003, "wrong host or port");

    public static final RetCode REQUEST_NODE_EXCEPTION =
            RetCode.mark(205004, "request node exception");

    /* param exception */
    public static final RetCode PARAM_EXCEPTION = RetCode.mark(305000, "param exception");

}
