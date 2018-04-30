## ZooKeeper

ZooKeeper 可以作为 Dubbo 的注册中心。

Dubbo 未对 Zookeeper 服务器端做任何侵入修改，只需安装原生的 Zookeeper 服务器即可，所有注册中心逻辑适配都在调用 Zookeeper 客户端时完成。  

**下载与安装[linux版]**

[ZooKeeper下载](https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/) 

上传到/usr/local目录下，解压  

```
tar -zxvf zookeeper-3.4.10.tar.gz  

```

在zookeeper-3.4.10文件下创建data文件夹，pwd输出一下路径（配置需要）  

进入conf文件夹下，修改、配置zoo.cfg（重命名、data路径、端口号等）  

```
mv zoo_sample.cfg zoo.cfg  
vim zoo.cfg  
dataDir=/usr/local/zookeeper-3.4.10/data
```



**不需要集群**，`zoo.cfg` 的内容如下：  

```
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/home/dubbo/zookeeper-3.3.3/data
clientPort=2181
```

**需要集群**，`zoo.cfg` 的内容如下：  

```
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/home/dubbo/zookeeper-3.3.3/data
clientPort=2181
server.1=192.168.1.107:2555:3555
server.2=192.168.1.108:2555:3555

```

并在 data 目录，下放置 myid 文件  

```
mkdir data
vi myid
```  
myid 指明自己的 id，对应上面 `zoo.cfg` 中 `server.` 后的数字，第一台的内容为 1，第二台的内容为 2。  

开放端口，启动，查看状态

```
vim /etc/sysconfig/iptables
-A INPUT -p tcp -m tcp --dport 2181 -j ACCEPT
service iptables restart
cd ../bin
./zkServer.sh start
./zkServer.sh status
```
Linux 下执行 `bin/zkServer.sh` ；Windows `bin/zkServer.cmd` 启动 ZooKeeper 。  


在dubbo配置中使用
```xml
<!-- 使用zookeeper注册中心暴露服务地址  -->
<dubbo:registry address="zookeeper://192.168.1.107:2181"/>
```

## Dubbo

Dubbo 采用全 Spring 配置方式，透明化接入应用，对应用没有任何 API 侵入，只需用 Spring 加载 Dubbo 的配置即可，Dubbo 基于 Spring 的 Schema 扩展进行加载。

### 服务提供者  

特点：只有业务逻辑层、数据访问层  


applicationContext-dubbo.xml 配置  

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
    xsi:schemaLocation="http://www.springframework.org/schema/beans 
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://code.alibabatech.com/schema/dubbo 
    http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
    
    <!-- dubbod 的应用名称，通常是项目名 -->
    <dubbo:application name="dubbo-service"/>

    <!-- 使用zookeeper注册中心暴露服务地址  -->
    <dubbo:registry address="zookeeper://192.168.1.107:2181"/>
	
    <!-- 用dubbo协议在20880端口暴露服务 -->  
    <dubbo:protocol name="dubbo" port="20880" />
	
    <!-- 配置扫描器 -->
    <dubbo:annotation package="com.sep6th.service.impl"/>

</beans>

```


### 服务消费者  

特点：复制服务提供方service接口  

applicationContext-dubbo.xml 配置  

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
   xsi:schemaLocation="http://www.springframework.org/schema/beans 
   http://www.springframework.org/schema/beans/spring-beans.xsd
   http://code.alibabatech.com/schema/dubbo
   http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
    
   <!-- 消费方应用名，用于计算依赖关系，不是匹配条件，不要与提供方一样 -->
   <dubbo:application name="dubbo-consumer"/>

   <!-- 使用zookeeper作为dubbo注册中心  -->
   <dubbo:registry address="zookeeper://192.168.1.107:2181"/>

   <!-- 配置dubbo扫描器，在MVC配置文件中(先扫dubbo, 后扫MVC) -->
   <!-- <dubbo:annotation package="com.sep6th.controller"/>-->
   <!-- <context:component-scan base-package="com.sep6th.controller"/> -->
   
</beans>
```

## 资料

**Dubbo**

[Github](https://github.com/alibaba/dubbo) | [用户手册](https://dubbo.gitbooks.io/dubbo-user-book/content/) | [开发手册](https://dubbo.gitbooks.io/dubbo-dev-book/content/) | [管理员手册](https://dubbo.gitbooks.io/dubbo-admin-book/content/)

**ZooKeeper**

[官网](http://zookeeper.apache.org/) | [官方文档](http://zookeeper.apache.org/doc/trunk/)

