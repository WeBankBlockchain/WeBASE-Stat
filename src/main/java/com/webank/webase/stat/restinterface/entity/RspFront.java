package com.webank.webase.stat.restinterface.entity;

import lombok.Data;

/**
 * get front list by chainId from chain manager
 */
@Data
public class RspFront {
    private Integer chainId;
    private Integer frontId;
    private String frontIp;
    private Integer frontPort;
    private String description;
}