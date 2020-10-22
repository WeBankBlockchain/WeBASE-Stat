# 部署说明

## 1. 前提条件

| 序号 | 软件                |
| ---- | ------------------- |
| 1    | FISCO-BCOS bsn |
| 2    | WeBASE-Front bsn |
| 3    | MySQL5.6或以上版本  |
| 4    | Java8或以上版本     |


## 2. 注意事项
*  Java推荐使用[OpenJDK](https://openjdk.java.net/ )，建议从OpenJDK网站自行下载（CentOS的yum仓库的OpenJDK缺少JCE(Java Cryptography Extension)，导致Web3SDK无法正常连接区块链节点）[下载地址](https://jdk.java.net/java-se-ri/11) [安装指南](https://openjdk.java.net/install/index.html)
*  安装说明可以参考 [安装示例](./appendix.md#1-安装示例)
*  在服务搭建的过程中，如碰到问题，请查看 [常见问题解答](./appendix.md#2-常见问题)
*  安全温馨提示： 强烈建议设置复杂的数据库登录密码，且严格控制数据操作的权限和网络策略


## 3. 拉取代码
执行命令：
```shell
git clone https://github.com/WeBankFinTech/WeBASE-Stat.git
```
进入目录：

```shell
cd WeBASE-Stat
```

## 4. 编译代码

方式一：如果服务器已安装Gradle，且版本为Gradle-4.10或以上

```shell
gradle build -x test
```

方式二：如果服务器未安装Gradle，或者版本不是Gradle-4.10或以上，使用gradlew编译

```shell
chmod +x ./gradlew && ./gradlew build -x test
```

构建完成后，会在根目录WeBASE-Stat下生成已编译的代码目录dist。

## 5. 服务配置及启停
### 5.1 服务配置修改
（1）回到dist目录，dist目录提供了一份配置模板conf_template：

```
根据配置模板生成一份实际配置conf。初次部署可直接拷贝。
例如：cp conf_template conf -r
```

（2）修改服务配置，完整配置项说明请查看 [配置说明](./appendix.md#3-applicationyml配置项说明)
```shell
修改服务端口：sed -i "s/5008/${your_server_port}/g" conf/application.yml
修改数据库IP：sed -i "s/mysql://127.0.0.1/mysql://${your_db_ip}/g" conf/application.yml
修改数据库端口：sed -i "s/3306/${your_db_port}/g" conf/application.yml
修改数据库名称：sed -i "s/webasestat/${your_db_name}/g" conf/application.yml
修改数据库用户：sed -i "s/defaultAccount/${your_db_account}/g" conf/application.yml
修改数据库密码：sed -i "s/defaultPassword/${your_db_password}/g" conf/application.yml
修改ChainMgr的IPPort：sed -i "s/127.0.0.1:5005/${your_chainmgr_ip_port}/g" conf/application.yml
```

### 5.2 服务启停
在dist目录下执行：
```shell
启动：bash start.sh
停止：bash stop.sh
检查：bash status.sh
```
**备注**：服务进程起来后，需通过日志确认是否正常启动，出现以下内容表示正常；如果服务出现异常，确认修改配置后，重启。如果提示服务进程在运行，则先执行stop.sh，再执行start.sh。

```
...
	Application() - main run success...
```

## 6. 访问

可以通过swagger查看调用接口：

```
http://{deployIP}:{deployPort}/WeBASE-Stat/swagger-ui.html
示例：http://localhost:5008/WeBASE-Stat/swagger-ui.html
```

**备注：** 

- 部署服务器IP和服务端口需对应修改，网络策略需开通

## 7. 查看日志

在dist目录查看：
```shell
全量日志：tail -f log/WeBASE-Stat.log
错误日志：tail -f log/WeBASE-Stat-error.log
```

## 8. 执行数据表分表与定时分表

#### 数据库数据表初始化

数据表的初始化已在启动项目的时候，自动完成。启动完成后，需要执行下文的步骤中，通过`crontab`执行数据分表的定时任务

#### 初始化数据表分区

1. 初始化数据库后，回到项目父目录`cd ..`，运行第一次分区初始化，
```
bash partition.sh initNow
```

#### crontab定时更新数据表分区

初始化成功后，在crontab中添加每月1日0时更新数据表下一个月分区的定时任务，表达式如下

```
0 * 1 * * bash /WeBASE-Stat/partition.sh >>/WeBASE-Stat/cron.log 2>&1 &
```

将在crontab中运行该表达式
```shell
# 检查crontab是否启动，否则先启动
service crond status
service crond start
# 检查已有的crontab任务
crontab -l
# 导出已有的crontab任务到.tmp文件
crontab -l >crontabs.tmp
# 在.tmp中将表达式的命令加到.tmp文件中
vi crontabs.tmp
# 表达式为，每月1日0时执行/WeBASE-Stat/路径中的partition.sh脚本
# 路径需要修改为对应项目的目录
0 * 1 * * bash /WeBASE-Stat/partition.sh >>/WeBASE-Stat/cron.log 2>&1 &
# crontab + 文件名，让命令生效
crontab crontabs.tmp
# 检查是否修改配置成功
crontab -l
# 修改后reload加载配置
service crond reload 
```
