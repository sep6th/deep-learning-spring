# scheduling-quartz

启动Tomcat，观察控制台打印信息。

## 添加Maven依赖


```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
	http://maven.apache.org/xsd/maven-4.0.0.xsd">
	
	<modelVersion>4.0.0</modelVersion>
  	<groupId>org.springframework.samples.service.service</groupId>
  	<artifactId>scheduling-quartz</artifactId>
  	<version>0.0.1-SNAPSHOT</version>
  	<packaging>war</packaging>
  
  	<parent>
		<groupId>com.sep6th</groupId>
		<artifactId>parent</artifactId>
		<version>1.0</version>
  	</parent>
  	
    <properties>
		<java.version>1.8</java.version>
		<maven.compiler.source>${java.version}</maven.compiler.source>
		<maven.compiler.target>${java.version}</maven.compiler.target>
		<spring-framework.version>5.0.2.RELEASE</spring-framework.version>

	</properties>
	
	<dependencies>
		
		<!-- web.xml 中的以下这个类在 [spring-web]
			-org.springframework.web.context.ContextLoaderListener
		-->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-web</artifactId>
			<version>${spring-framework.version}</version>
		</dependency>
		
		<!-- applicationContext-quartz.xml 中的以下三个类都在 [spring-context-support]
			-org.springframework.scheduling.quartz.SchedulerFactoryBean
			-org.springframework.scheduling.quartz.CronTriggerFactoryBean
			-org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean
		-->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context-support</artifactId>
			<version>${spring-framework.version}</version>
		</dependency>
		
		<dependency>
		    <groupId>org.quartz-scheduler</groupId>
		    <artifactId>quartz</artifactId>
		    <version>2.2.3</version>
		</dependency>
		
	</dependencies>
	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.tomcat.maven</groupId>
				<artifactId>tomcat7-maven-plugin</artifactId>
				<version>2.2</version>
				<configuration>
					<port>8090</port>
					<useBodyEncodingForURI>true</useBodyEncodingForURI>
					<path>/</path>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId> 
				<artifactId>maven-compiler-plugin</artifactId> 
				<version>3.7.0</version>
				<configuration> 
					<source>${maven.compiler.source}</source> 
					<target>${maven.compiler.target}</target> 
					<encoding>UTF-8</encoding>
				</configuration> 
			</plugin>
		</plugins>
	</build>
	
</project>

```
## applicationContext-quartz.xml


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans 
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://www.springframework.org/schema/context 
	http://www.springframework.org/schema/context/spring-context.xsd">

	<!-- 作业的bean -->
	<bean id="jobEntity" class="com.sep6th.core.job.TaskTime" />

	<!-- 任务1配置 -->
	<bean id="jobDetail_one" class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">  
		<!-- 执行的类 --> 
		<property name="targetObject">  
            <ref bean="jobEntity" />  
        </property>
        <!-- 类中的方法 -->  
        <property name="targetMethod">  
            <value>first_Job</value>  
        </property>
	</bean>
	<!-- 为任务1，添加定时 -->
	<bean id="cronTrigger_one" class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">
		<property name="jobDetail"><ref bean="jobDetail_one" /></property>
		<property name="cronExpression"><value>${task.first.cron}</value></property>
	</bean>
	
	<!-- 任务2配置 -->
	<bean id="jobDetail_two" class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">  
		<!-- 执行的类 --> 
		<property name="targetObject">  
            <ref bean="jobEntity" />  
        </property>
        <!-- 类中的方法 -->  
        <property name="targetMethod">  
            <value>second_Job</value>  
        </property>
	</bean>
	<!-- 为任务2，添加定时 -->
	<bean id="cronTrigger_two" class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">
		<property name="jobDetail"><ref bean="jobDetail_two" /></property>
		<property name="cronExpression"><value>${task.second.cron}</value></property>
	</bean>

	<!--总调度工厂-->
	 <bean class="org.springframework.scheduling.quartz.SchedulerFactoryBean"> 
		<!-- 添加触发器 -->  
        <property name="triggers">  
            <list>  
                <ref bean="cronTrigger_one" />  
                <ref bean="cronTrigger_two" />  
            </list>  
        </property> 
	</bean>
	
</beans>

```
## task.properties


```properties
task.first.cron=0/10 * * * * ?
task.second.cron=0/15 * * * * ?
```
## applicationContext.xml


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans 
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://www.springframework.org/schema/context 
	http://www.springframework.org/schema/context/spring-context.xsd">
    
   	
	<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
		<!-- 忽略没有找到的资源文件 -->
		<property name="ignoreResourceNotFound" value="true" />
		<!-- 加载配置资源文件 -->
		<property name="locations">
			<list>
				<value>classpath:task.properties</value>
			</list>
		</property>
	</bean>
	
</beans>

```
## web.xml


```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns="http://java.sun.com/xml/ns/javaee" 
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee 
	http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" 
	id="WebApp_ID" version="2.5">
	
	<display-name>scheduling-quartz</display-name>
	
  	<welcome-file-list>
    	<welcome-file>index.jsp</welcome-file>
  	</welcome-file-list>
  	
  	<context-param>
    	<param-name>contextConfigLocation</param-name>
    	<param-value>classpath:spring/applicationContext*.xml</param-value>
  	</context-param>
  	
  	<listener>
    	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  	</listener>
  	
</web-app>
```
## TaskTime.java


```java
package com.sep6th.core.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 */

public class TaskTime {

	private static Logger log = LoggerFactory.getLogger(TaskTime.class);
	
	public void first_Job(){
		log.info("[first_Job]-----begin-----");
		first();
		log.info("[first_Job]-----end-----");
	}
	
	public void second_Job(){
		log.info("[second_Job]-----begin-----");
		second();
		log.info("[second_Job]-----end-----");
	}
	
	private void first(){
		log.info("[first_Job]-----doing-----");
	}
	
	private void second(){
		log.info("[second_Job]-----doing-----");
	}
	
	
}

```






