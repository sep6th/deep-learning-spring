## 安装RabbitMQ

RabbitMQ是用Erlang开发的，所以需要先安装Erlang环境。  

1. 安装Erlang环境  

下载地址：https://bintray.com/rabbitmq/rpm/erlang/20.3.4-1  

```
rpm -ivh erlang-20.3.4-1.el6.x86_64.rpm
```
2. 安装rabbitmq-server  

下载地址：https://dl.bintray.com/rabbitmq/all/rabbitmq-server/  

rabbitmq依赖于socat，因此在安装rabbitmq前要安装socat

由于默认的CentOS-Base.repo源中没有socat，所以执行yum install socat会出现以下错误：No package socat available  

epel是yum的一个软件源，里面包含了许多基本源里没有的软件。  

epel(CentOS7)  

```
wget -O /etc/yum.repos.d/epel.repo http://mirrors.aliyun.com/repo/epel-7.repo
```
epel(CentOS6)  

```
wget -O /etc/yum.repos.d/epel.repo http://mirrors.aliyun.com/repo/epel-6.repo
```
安装epel之后  

```
yum -y install socat
rpm -ivh rabbitmq-server-3.7.4-1.el6.noarch.rpm
```

MQ基本操作  

```
service rabbitmq-server start
service rabbitmq-server stop
service rabbitmq-server restart
chkconfig rabbitmq-server on    //开机自启
```
开启web界面管理工具，在浏览器中可以查看
```
rabbitmq-plugins enable rabbitmq_management
service rabbitmq-server restart|start
```
如果启动不了，报hosts的问题。  
解决方法：  
```
[root@www ~]# vim /etc/hosts
10.77.210.11 www
```

开启guest用户远程登录访问  
```
vim /usr/lib/rabbitmq/lib/rabbitmq_server-3.7.4/ebin/rabbit.app
```
去掉{loopback_users, [<<"guest">>]}中的<<"guest">>  


开放端口15672（rabbitmq web默认端口）和5672（rabbitmq默认端口）

```
vim /etc/sysconfig/iptables
```
添加一下内容  
-A INPUT -p tcp -m tcp --dport 15672 -j ACCEPT  
-A INPUT -p tcp -m tcp --dport 5672 -j ACCEPT  

浏览器访问：http://<ip>:15672
输入默认的用户名guest、密码guest，进行登录

添加新用户
```
rabbitmqctl list_users  //查看用户列表  
rabbitmqctl add_user rabbit@user rabbit@pwd  //添加用户  
rabbitmqctl set_user_tags rabbit@user administrator   //授权
```

## 卸载RabbitMQ及Erlang环境
 
```
rpm -qa | grep rabbitmq    //查看rpm文件名  
rpm -e rabbitmq-server-3.7.4-1.el6.noarch   //卸载  
rpm -qa | grep rabbitmq    //确认是否卸载  
```

```
rpm -qa | grep erlang
rpm -e erlang-20.3.4-1.el6.x86_64
rpm -qa | grep erlang
```
关闭开放的端口  

```
vim /etc/sysconfig/iptables
```

## 拓展
  
1.添加user，在web添加admin，并设置权限。  

2.rabbitmq配置文件的位置  

```
vim /usr/share/doc/rabbitmq-server-3.7.4/rabbitmq.config.example
```


