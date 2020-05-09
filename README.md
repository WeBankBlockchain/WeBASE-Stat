# 数据统计服务子系统
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](https://webasedoc.readthedocs.io/zh_CN/latest/docs/WeBASE/CONTRIBUTING.html)
[![CodeFactor](https://www.codefactor.io/repository/github/webankfintech/WeBASE-Chain-Manager/badge)](https://www.codefactor.io/repository/github/webankfintech/WeBASE-Chain-Manager)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/92c4a706a51d4ad5a6a0387233d4650e)](https://www.codacy.com/gh/WeBankFinTech/WeBASE-Chain-Manager?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=WeBankFinTech/WeBASE-Chain-Manager&amp;utm_campaign=Badge_Grade)
[![Code Lines](https://tokei.rs/b1/github/WeBankFinTech/WeBASE-Chain-Manager?category=code)](https://github.com/WeBankFinTech/WeBASE-Chain-Manager)
[![license](http://img.shields.io/badge/license-Apache%20v2-blue.svg)](http://www.apache.org/licenses/)


## 简介
​	WeBASE-Stat为微众区块链中间件平台-数据统计服务子系统，统计数据服务以前置为基础，拉取CPU、内存、IO、群组大小、群组gas、群组网络流量的数据，记录数据库。主要包括以下模块：

| 序号 | 模块     | 描述                                                         |
| ---- | -------- | ------------------------------------------------------------ |
| 1    | 前置管理 | 维护WeBASE-Front服务信息，可以新增、查询和删除前置（调用请查看接口说明1） |
| 2    | 群组管理 | 查询前置群组信息（调用请查看接口说明2）                      |
| 3    | 数据管理 | 查询统计数据信息（调用请查看接口说明3）、定时拉取数据（后台定时处理） |

​	部署和接口说明如下：

- [部署说明](./install.md)

- [接口说明](./interface.md)

## 贡献说明
请阅读我们的贡献文档，了解如何贡献代码，并提交你的贡献。

希望在您的参与下，WeBASE会越来越好！

## 社区
联系我们：webase@webank.com
