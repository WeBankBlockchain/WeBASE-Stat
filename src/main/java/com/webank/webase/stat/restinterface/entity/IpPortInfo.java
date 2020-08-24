package com.webank.webase.stat.restinterface.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IpPortInfo {
    private String ip;
    private Integer port;
}