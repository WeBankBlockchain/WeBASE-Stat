# 接口说明

[TOC]

## 1 前置管理模块

### 1.1 新增节点前置信息

#### 1.1.1 传输协议规范

- 网络传输协议：使用HTTP协议
- 请求地址�? **/front/add**
- 请求方式：POST
- 请求头：Content-type: application/json
- 返回格式：JSON

#### 1.1.2 请求参数

***1）入参表***

| 序号 | 输入参数    | 类型   | 可为�? | 备注         |
| ---- | ----------- | ------ | ------ | ------------ |
| 1    | frontId     | Int    | �?     | 节点编号     |
| 2    | frontIp     | String | �?     | 前置ip       |
| 3    | frontPort   | Int    | �?     | 前置服务端口 |
| 5    | description | String | �?     | 备注         |

***2）入参示�?***

```
http://127.0.0.1:5008/WeBASE-Stat/front/add
```

```
{
    "frontId": 1001,
    "frontIp": "127.0.0.1",
    "frontPort": 5002,
    "description": "test"
}
```

#### 2.1.3 返回参数

***1）出参表***

| 序号 | 输出参数        | 类型          |      | 备注                         |
| ---- | --------------- | ------------- | ---- | ---------------------------- |
| 1    | code            | Int           | �?   | 返回码，0：成�? 其它：失�?   |
| 2    | message         | String        | �?   | 描述                         |
| 3    |                 | Object        |      | 节点信息对象                 |
| 3.1  | frontId         | Int           | �?   | 前置编号                     |
| 3.2  | frontIp         | String        | �?   | 前置ip                       |
| 3.3  | frontPort       | Int           | �?   | 前置端口                     |
| 3.4  | memoryTotalSize | String        | �?   | 内存总量（单位：KB�?         |
| 3.5  | memoryUsedSize  | String        | �?   | 内存使用量（单位：KB�?       |
| 3.6  | cpuSize         | String        | �?   | CPU的大小（单位：MHz�?       |
| 3.7  | cpuAmount       | String        | �?   | CPU的核数（单位：个�?        |
| 3.8  | diskTotalSize   | String        | �?   | 文件系统总量（单位：KB�?     |
| 3.9  | diskUsedSize    | String        | �?   | 文件系统已使用量（单位：KB�? |
| 3.10 | description     | String        | �?   | 备注                         |
| 3.11 | createTime      | LocalDateTime | �?   | 落库时间                     |
| 3.12 | modifyTime      | LocalDateTime | �?   | 修改时间                     |

***2）出参示�?***

- 成功�?

```
{
    "code": 0,
    "message": "success",
    "data": {
      "frontId": 1001,
      "frontIp": "127.0.0.1",
      "frontPort": 5302,
      "memoryTotalSize": "8008840",
      "memoryUsedSize": "7652872",
      "cpuSize": "2599",
      "cpuAmount": "4",
      "diskTotalSize": "51474044",
      "diskUsedSize": "44137936",
      "description": "dd",
      "createTime": "2020-04-29T12:15:40",
      "modifyTime": "2020-05-03T12:04:08"
    }
}
```

- 失败�?

```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```

### 1.2 获取�?有前置列�? 

#### 1.2.1 传输协议规范

- 网络传输协议：使用HTTP协议
- 请求地址�?**/front/list?frontId={frontId}**
- 请求方式：GET
- 返回格式：JSON

#### 1.2.2 请求参数

***1）入参表***

| 序号 | 输入参数 | 类型 | 可为�? | 备注     |
| ---- | -------- | ---- | ------ | -------- |
| 1    | frontId  | Int  | �?     | 前置编号 |

***2）入参示�?***

```
http://127.0.0.1:5008/WeBASE-Stat/front/list?frontId=1001
```

#### 1.2.3 返回参数 

***1）出参表***

| 序号   | 输出参数        | 类型          |      | 备注                         |
| ------ | --------------- | ------------- | ---- | ---------------------------- |
| 1      | code            | Int           | �?   | 返回码，0：成�? 其它：失�?   |
| 2      | message         | String        | �?   | 描述                         |
| 3      | totalCount      | Int           | �?   | 总记录数                     |
| 4      | data            | List          | �?   | 列表                         |
| 4.1    |                 | Object        |      | 信息对象                     |
| 4.1.1  | frontId         | Int           | �?   | 前置编号                     |
| 4.1.2  | frontIp         | String        | �?   | 前置ip                       |
| 4.1.3  | frontPort       | Int           | �?   | 前置端口                     |
| 4.1.4  | memoryTotalSize | String        | �?   | 内存总量（单位：KB�?         |
| 4.1.5  | memoryUsedSize  | String        | �?   | 内存使用量（单位：KB�?       |
| 4.1.6  | cpuSize         | String        | �?   | CPU的大小（单位：MHz�?       |
| 4.1.7  | cpuAmount       | String        | �?   | CPU的核数（单位：个�?        |
| 4.1.8  | diskTotalSize   | String        | �?   | 文件系统总量（单位：KB�?     |
| 4.1.9  | diskUsedSize    | String        | �?   | 文件系统已使用量（单位：KB�? |
| 4.1.10 | description     | String        | �?   | 备注                         |
| 4.1.11 | createTime      | LocalDateTime | �?   | 落库时间                     |
| 4.1.12 | modifyTime      | LocalDateTime | �?   | 修改时间                     |

***2）出参示�?***

- 成功�?

```
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "frontId": 1001,
      "frontIp": "127.0.0.1",
      "frontPort": 5302,
      "memoryTotalSize": "8008840",
      "memoryUsedSize": "7652872",
      "cpuSize": "2599",
      "cpuAmount": "4",
      "diskTotalSize": "51474044",
      "diskUsedSize": "44137936",
      "description": "dd",
      "createTime": "2020-04-29T12:15:40",
      "modifyTime": "2020-05-03T12:04:08"
    }
  ],
  "totalCount": 1
}
```

- 失败�?

```
{
   "code": 102000,
   "message": "system exception",
   "data": {}
}
```

### 1.3 删除前置信息

#### 1.3.1 传输协议规范

- 网络传输协议：使用HTTP协议
- 请求地址�?**/front/{frontId}**
- 请求方式：DELETE
- 请求头：Content-type: application/json
- 返回格式：JSON

#### 1.3.2 请求参数

***1）入参表***

| 序号 | 输入参数 | 类型 | 可为�? | 备注     |
| ---- | -------- | ---- | ------ | -------- |
| 1    | frontId  | Int  | �?     | 前置编号 |

***2）入参示�?***

```
http://127.0.0.1:5008/WeBASE-Stat/front/1001
```

#### 1.3.3 返回参数 

***1）出参表***

| 序号 | 输出参数 | 类型   |      | 备注                       |
| ---- | -------- | ------ | ---- | -------------------------- |
| 1    | code     | Int    | �?   | 返回码，0：成�? 其它：失�? |
| 2    | message  | String | �?   | 描述                       |
| 3    | data     | object | �?   | 返回信息实体（空�?         |

***2）出参示�?***

- 成功�?

```
{
    "code": 0,
    "data": {},
    "message": "success"
}
```

- 失败�?

```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```

## 2 群组管理模块

### 2.1 获取群组列表

#### 2.1.1 传输协议规范

- 网络传输协议：使用HTTP协议
- 请求地址�?**/group/list/{frontId}**
- 请求方式：GET
- 返回格式：JSON

#### 2.1.2 请求参数

***1）入参表***

| 序号 | 输入参数 | 类型 | 可为�? | 备注     |
| ---- | -------- | ---- | ------ | -------- |
| 1    | frontId  | Int  | �?     | 前置编号 |

***2）入参示�?***

```
http://127.0.0.1:5008/WeBASE-Stat/group/list/1001
```

#### 2.1.3 返回参数 

***1）出参表***

| 序号  | 输出参数    | 类型          |      | 备注                       |
| ----- | ----------- | ------------- | ---- | -------------------------- |
| 1     | code        | Int           | �?   | 返回码，0：成�? 其它：失�? |
| 2     | message     | String        | �?   | 描述                       |
| 3     | totalCount  | Int           | �?   | 总记录数                   |
| 4     | data        | List          | �?   | 列表                       |
| 4.1   |             | Object        |      | 信息对象                   |
| 4.1.1 | frontId     | Int           | �?   | 前置编号                   |
| 4.1.2 | groupId     | Int           | �?   | 群组编号                   |
| 4.1.3 | description | String        | �?   | 描述                       |
| 4.1.4 | createTime  | LocalDateTime | �?   | 落库时间                   |
| 4.1.5 | modifyTime  | LocalDateTime | �?   | 修改时间                   |

***2）出参示�?***

- 成功�?

```
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "frontId": 1001,
      "groupId": 1,
      "description": null,
      "createTime": "2020-04-29T12:15:40",
      "modifyTime": "2020-04-29T12:15:40"
    },
    {
      "frontId": 1001,
      "groupId": 2,
      "description": null,
      "createTime": "2020-04-29T12:15:40",
      "modifyTime": "2020-04-29T12:15:40"
    }
  ],
  "totalCount": 2
}
```

- 失败�?

```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```

## 3 数据管理模块

### 3.1 查询群组基本数据

#### 3.1.1 传输协议规范

- 网络传输协议：使用HTTP协议
- 请求地址�?

```
/data/groupBasicData?frontId={frontId}&groupId={groupId}&pageSize={pageSize}&pageNumber={pageNumber}&beginDate={beginDate}&endDate={endDate}
```

- 请求方式：GET
- 返回格式：JSON

#### 3.1.2 请求参数

***1）入参表***

| 序号 | 输入参数   | 类型          | 可为�? | 备注                                                      |
| ---- | ---------- | ------------- | ------ | --------------------------------------------------------- |
| 1    | frontId    | Int           | �?     | 前置编号                                                  |
| 2    | groupId    | Int           | �?     | 群组编号                                                  |
| 3    | pageSize   | Int           | �?     | 每页记录�?                                                |
| 4    | pageNumber | Int           | �?     | 当前页码                                                  |
| 5    | beginDate  | LocalDateTime | �?     | �?始时间（yyyy-MM-dd'T'HH:mm:ss.SSS 2019-03-13T00:00:00�? |
| 6    | endDate    | LocalDateTime | �?     | 结束时间                                                  |

***2）入参示�?***

```
http://127.0.0.1:5008/WeBASE-Stat/data/groupBasicData?frontId=1001&groupId=1&pageSize=1&pageNumber=1&beginDate=2020-04-30T16%3A57%3A35&endDate=2020-04-30T22%3A57%3A35
```

#### 3.1.3 返回参数 

***1）出参表***

| 序号  | 输出参数   | 类型          |      | 备注                       |
| ----- | ---------- | ------------- | ---- | -------------------------- |
| 1     | code       | Int           | �?   | 返回码，0：成�? 其它：失�? |
| 2     | message    | String        | �?   | 描述                       |
| 3     | totalCount | Int           | �?   | 总记录数                   |
| 4     | data       | List          | �?   | 列表                       |
| 4.1   |            | Object        |      | 信息对象                   |
| 4.1.1 | id         | Long          | �?   | 编号                       |
| 4.1.2 | frontId    | Int           | �?   | 前置编号                   |
| 4.1.3 | groupId    | Int           | �?   | 群组编号                   |
| 4.1.4 | size       | Long          | �?   | 群组大小                   |
| 4.1.5 | transCount | Long          | �?   | 群组交易数量               |
| 4.1.6 | comment    | String        | �?   | 备注                       |
| 4.1.7 | createTime | LocalDateTime | �?   | 落库时间                   |
| 4.1.8 | modifyTime | LocalDateTime | �?   | 修改时间                   |

***2）出参示�?***

- 成功�?

```
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 13475,
      "frontId": 1001,
      "groupId": 1,
      "size": 17157,
      "transCount": 121,
      "comment": null,
      "createTime": "2020-04-30T20:21:23",
      "modifyTime": "2020-04-30T20:21:23"
    }
  ],
  "totalCount": 14
}
```

- 失败�?

```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```

### 3.2 查询网络统计日志数据

#### 3.2.1 传输协议规范

- 网络传输协议：使用HTTP协议
- 请求地址�?

```
/data/netWorkData?frontId={frontId}&groupId={groupId}&pageSize={pageSize}&pageNumber={pageNumber}&beginDate={beginDate}&endDate={endDate}
```

- 请求方式：GET
- 返回格式：JSON

#### 3.2.2 请求参数

***1）入参表***

| 序号 | 输入参数   | 类型          | 可为�? | 备注                                                      |
| ---- | ---------- | ------------- | ------ | --------------------------------------------------------- |
| 1    | frontId    | Int           | �?     | 前置编号                                                  |
| 2    | groupId    | Int           | �?     | 群组编号                                                  |
| 3    | pageSize   | Int           | �?     | 每页记录�?                                                |
| 4    | pageNumber | Int           | �?     | 当前页码                                                  |
| 5    | beginDate  | LocalDateTime | �?     | �?始时间（yyyy-MM-dd'T'HH:mm:ss.SSS 2019-03-13T00:00:00�? |
| 6    | endDate    | LocalDateTime | �?     | 结束时间                                                  |

***2）入参示�?***

```
http://127.0.0.1:5008/WeBASE-Stat/data/netWorkData?frontId=1001&groupId=1&pageSize=1&pageNumber=1&beginDate=2020-04-30T16%3A57%3A35&endDate=2020-04-30T17%3A57%3A35
```

#### 3.2.3 返回参数 

***1）出参表***

| 序号  | 输出参数   | 类型   |      | 备注                                    |
| ----- | ---------- | ------ | ---- | --------------------------------------- |
| 1     | code       | Int    | �?   | 返回码，0：成�? 其它：失�?              |
| 2     | message    | String | �?   | 描述                                    |
| 3     | totalCount | Int    | �?   | 总记录数                                |
| 4     | data       | List   | �?   | 列表                                    |
| 4.1   |            | Object |      | 信息对象                                |
| 4.1.1 | id         | Long   | �?   | 编号                                    |
| 4.1.2 | frontId    | Int    | �?   | 前置编号                                |
| 4.1.3 | groupId    | Int    | �?   | 群组编号                                |
| 4.1.4 | totalIn    | Long   | �?   | 总入流量（P2P_InBytes + SDK_InBytes�?   |
| 4.1.5 | totalOut   | Long   | �?   | 总出流量（P2P_OutBytes + SDK_OutBytes�? |
| 4.1.6 | timestamp  | Long   | �?   | 统计时间                                |

***2）出参示�?***

- 成功�?

```
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 202055,
      "frontId": 1001,
      "groupId": 1,
      "totalIn": 91952,
      "totalOut": 92934,
      "timestamp": 1588240648000
    }
  ],
  "totalCount": 60
}
```

- 失败�?

```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```

### 3.3 查询交易Gas统计日志数据

#### 3.3.1 传输协议规范

- 网络传输协议：使用HTTP协议
- 请求地址�?

```
/data/gasData?frontId={frontId}&groupId={groupId}&pageSize={pageSize}&pageNumber={pageNumber}&beginDate={beginDate}&endDate={endDate}
```

- 请求方式：GET
- 返回格式：JSON

#### 3.3.2 请求参数

***1）入参表***

| 序号 | 输入参数   | 类型          | 可为�? | 备注                                                      |
| ---- | ---------- | ------------- | ------ | --------------------------------------------------------- |
| 1    | frontId    | Int           | �?     | 前置编号                                                  |
| 2    | groupId    | Int           | �?     | 群组编号                                                  |
| 3    | pageSize   | Int           | �?     | 每页记录�?                                                |
| 4    | pageNumber | Int           | �?     | 当前页码                                                  |
| 5    | beginDate  | LocalDateTime | �?     | �?始时间（yyyy-MM-dd'T'HH:mm:ss.SSS 2019-03-13T00:00:00�? |
| 6    | endDate    | LocalDateTime | �?     | 结束时间                                                  |

***2）入参示�?***

```
http://127.0.0.1:5008/WeBASE-Stat/data/gasData?frontId=1001&groupId=1&pageSize=1&pageNumber=1&beginDate=2020-04-20T16%3A57%3A35&endDate=2020-04-30T16%3A57%3A35
```

#### 3.3.3 返回参数 

***1）出参表***

| 序号  | 输出参数   | 类型   |      | 备注                       |
| ----- | ---------- | ------ | ---- | -------------------------- |
| 1     | code       | Int    | �?   | 返回码，0：成�? 其它：失�? |
| 2     | message    | String | �?   | 描述                       |
| 3     | totalCount | Int    | �?   | 总记录数                   |
| 4     | data       | List   | �?   | 列表                       |
| 4.1   |            | Object |      | 信息对象                   |
| 4.1.1 | id         | Long   | �?   | 编号                       |
| 4.1.2 | frontId    | Int    | �?   | 前置编号                   |
| 4.1.3 | groupId    | Int    | �?   | 群组编号                   |
| 4.1.4 | transHash  | String | �?   | 交易hash                   |
| 4.1.5 | gasUsed    | Long   | �?   | 交易消�?�的gas              |
| 4.1.6 | timestamp  | Long   | �?   | 统计时间                   |

***2）出参示�?***

- 成功�?

```
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 123,
      "frontId": 1001,
      "groupId": 1,
      "transHash": "0x1c232b58a6bf7a718d1434f160b6aa4dbbc9b65e831a2a10f9e36ffaa8b88ac6",
      "gasUsed": 41758,
      "timestamp": 1588230163000
    }
  ],
  "totalCount": 54
}
```

- 失败�?

```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```

### 3.4 查询前置节点监控数据

#### 3.4.1 传输协议规范

- 网络传输协议：使用HTTP协议
- 请求地址�?

```
/data/nodeMonitor?frontId={frontId}&groupId={groupId}&pageSize={pageSize}&pageNumber={pageNumber}&beginDate={beginDate}&endDate={endDate}
```

- 请求方式：GET
- 返回格式：JSON

#### 3.4.2 请求参数

***1）入参表***

| 序号 | 输入参数   | 类型          | 可为�? | 备注                                                      |
| ---- | ---------- | ------------- | ------ | --------------------------------------------------------- |
| 1    | frontId    | Int           | �?     | 前置编号                                                  |
| 2    | groupId    | Int           | �?     | 群组编号                                                  |
| 3    | pageSize   | Int           | �?     | 每页记录�?                                                |
| 4    | pageNumber | Int           | �?     | 当前页码                                                  |
| 5    | beginDate  | LocalDateTime | �?     | �?始时间（yyyy-MM-dd'T'HH:mm:ss.SSS 2019-03-13T00:00:00�? |
| 6    | endDate    | LocalDateTime | �?     | 结束时间                                                  |

***2）入参示�?***

```
http://127.0.0.1:5008/WeBASE-Stat/data/nodeMonitor?frontId=1001&groupId=1&pageSize=1&pageNumber=1&beginDate=2020-04-30T16%3A57%3A35&endDate=2020-04-30T18%3A57%3A35
```

#### 3.4.3 返回参数 

***1）出参表***

| 序号  | 输出参数                | 类型   |      | 备注                       |
| ----- | ----------------------- | ------ | ---- | -------------------------- |
| 1     | code                    | Int    | �?   | 返回码，0：成�? 其它：失�? |
| 2     | message                 | String | �?   | 描述                       |
| 3     | totalCount              | Int    | �?   | 总记录数                   |
| 4     | data                    | List   | �?   | 列表                       |
| 4.1   |                         | Object |      | 信息对象                   |
| 4.1.1 | id                      | Long   | �?   | 编号                       |
| 4.1.2 | frontId                 | Int    | �?   | 前置编号                   |
| 4.1.3 | groupId                 | Int    | �?   | 群组编号                   |
| 4.1.4 | blockHeight             | Long   | �?   | 块高                       |
| 4.1.5 | pbftView                | Long   | �?   | view                       |
| 4.1.6 | pendingTransactionCount | Int    | �?   | 待交易数                   |
| 4.1.7 | timestamp               | Long   | �?   | 统计时间                   |

***2）出参示�?***

- 成功�?

```
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 44144,
      "frontId": 1001,
      "groupId": 1,
      "blockHeight": 121,
      "pbftView": 11754,
      "pendingTransactionCount": 0,
      "timestamp": 1588242055000
    }
  ],
  "totalCount": 1001
}
```

- 失败�?

```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```

### 3.5 查询前置服务器�?�能数据

#### 3.5.1 传输协议规范

- 网络传输协议：使用HTTP协议
- 请求地址�?

```
/data/serverPerformance?frontId={frontId}&pageSize={pageSize}&pageNumber={pageNumber}&beginDate={beginDate}&endDate={endDate}
```

- 请求方式：GET
- 返回格式：JSON

#### 3.5.2 请求参数

***1）入参表***

| 序号 | 输入参数   | 类型          | 可为�? | 备注                                                      |
| ---- | ---------- | ------------- | ------ | --------------------------------------------------------- |
| 1    | frontId    | Int           | �?     | 前置编号                                                  |
| 2    | pageSize   | Int           | �?     | 每页记录�?                                                |
| 3    | pageNumber | Int           | �?     | 当前页码                                                  |
| 4    | beginDate  | LocalDateTime | �?     | �?始时间（yyyy-MM-dd'T'HH:mm:ss.SSS 2019-03-13T00:00:00�? |
| 5    | endDate    | LocalDateTime | �?     | 结束时间                                                  |

***2）入参示�?***

```
http://127.0.0.1:5008/WeBASE-Stat/data/serverPerformance?frontId=1001&pageSize=1&pageNumber=1&beginDate=2020-04-20T16%3A57%3A35&endDate=2020-04-30T16%3A57%3A35
```

#### 3.5.3 返回参数 

***1）出参表***

| 序号  | 输出参数       | 类型       |      | 备注                       |
| ----- | -------------- | ---------- | ---- | -------------------------- |
| 1     | code           | Int        | �?   | 返回码，0：成�? 其它：失�? |
| 2     | message        | String     | �?   | 描述                       |
| 3     | totalCount     | Int        | �?   | 总记录数                   |
| 4     | data           | List       | �?   | 列表                       |
| 4.1   |                | Object     |      | 信息对象                   |
| 4.1.1 | id             | Long       | �?   | 编号                       |
| 4.1.2 | frontId        | Int        | �?   | 前置编号                   |
| 4.1.3 | groupId        | Int        | �?   | 群组编号                   |
| 4.1.4 | cpuUseRatio    | BigDecimal | �?   | cpu利用�?                  |
| 4.1.5 | diskUseRatio   | BigDecimal | �?   | 硬盘利用�?                 |
| 4.1.6 | memoryUseRatio | BigDecimal | �?   | 内存利用�?                 |
| 4.1.7 | rxbps          | BigDecimal | �?   | 上行bandwith               |
| 4.1.8 | txbps          | BigDecimal | �?   | 下行bandwith               |
| 4.1.9 | timestamp      | Long       | �?   | 统计时间                   |

***2）出参示�?***

- 成功�?

```
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 22078,
      "frontId": 1001,
      "cpuUseRatio": 10,
      "diskUseRatio": 89,
      "memoryUseRatio": 72,
      "rxbps": 21,
      "txbps": 21,
      "timestamp": 1588240650003
    }
  ],
  "totalCount": 502
}
```

- 失败�?

```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```

## 附录 

### 1. 返回码信息列�?

| Code   | message                                | 描述           |
| ------ | -------------------------------------- | -------------- |
| 0      | success                                | 正常           |
| 105000 | system error                           | 系统异常       |
| 205000 | invalid front id                       | 无效的前置编�? |
| 205001 | front already exists                   | 前置已存�?     |
| 205002 | request front fail, please check front | 保存前置失败   |
| 205003 | wrong host or port                     | 地址或端口错�? |
| 205004 | request node exception                 | 请求节点失败   |
| 305000 | param exception                        | 参数异常       |