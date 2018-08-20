## Spring整合RabbitMQ 

pom.xml  
```  
<properties>
    <spring-rabbit.version>1.7.1.RELEASE</spring-rabbit.version>  
</properties>
<dependencies>
    <dependency>
        <groupId>org.springframework.amqp</groupId>
        <artifactId>spring-rabbit</artifactId>
        <version>${spring-rabbit.version}</version>
    </dependency>  
</dependencies>
```

发送消息配置  
applicationContext-rabbit-send.xml  
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
	  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	  xmlns:context="http://www.springframework.org/schema/context"
	  xmlns:rabbit="http://www.springframework.org/schema/rabbit"
	  xsi:schemaLocation="http://www.springframework.org/schema/beans 
	  http://www.springframework.org/schema/beans/spring-beans.xsd
	  http://www.springframework.org/schema/context 
	  http://www.springframework.org/schema/context/spring-context.xsd
	  http://www.springframework.org/schema/rabbit 
	  http://www.springframework.org/schema/rabbit/spring-rabbit.xsd">
    
    <context:annotation-config/>
    <context:component-scan base-package="com.sep6th.base.core.listener"/>

    <!-- 配置ConnectionFactory,连接工厂 -->
    <rabbit:connection-factory id="connectionFactory"
        host="10.77.210.11" username="rabbit@user" 
        password="rabbit@pwd" port="5672" />

    <!-- RabbitAdmin主要用于创建队列和交换器以及绑定关系等 -->
    <rabbit:admin id="rabbitAdmin" connection-factory="connectionFactory" />

    <!-- 声明一个队列 -->
    <rabbit:queue name="myQueue" durable="true"  auto-delete="false"/>

    <!-- 声明一个topic类型的exchange，并把上面声明的队列绑定在上面，routingKey="foo.*" -->
    <rabbit:topic-exchange  name="myExchange" >
        <rabbit:bindings>
            <rabbit:binding queue="myQueue" pattern="foo.*" />
            <!-- 这里还可以继续绑定其他队列 -->
            
        </rabbit:bindings>
    </rabbit:topic-exchange>

    <!-- 声明一个rabbitTemplate，指定连接信息，发送消息到myExchange上，routingKey在程序中设置，此处的配置在程序中可以用set修改 -->
    <rabbit:template id="rabbitTemplate" connection-factory="connectionFactory"
        exchange="myExchange" routing-key="foo.bar" message-converter="jsonMessageConverter"/>
	
	  <!-- 消息对象json转换类 -->
    <bean id="jsonMessageConverter" class="org.springframework.amqp.support.converter.Jackson2JsonMessageConverter" />
    
</beans>

```  

发送Java代码  

```  

package com.sep6th.base.core.listener;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/** 
 * 
 * @author liuzy
 * @date 2018/08/06 11:28
 */
public class RabbitTest {

	public static void main(String[] args) throws InterruptedException {
	    //启动Spring环境
	    AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
	    		"spring/applicationContext-rabbit-send.xml");
	    //假装是Autowired的
	    RabbitTemplate template = ctx.getBean(RabbitTemplate.class);
	    //设置routingKey
	    template.setRoutingKey("foo.bar");
	    //发送，exchange，routingKey什么的都配好了
	    for(int i=1;i<=30;i++) {
	    	template.convertAndSend("用户行为数据"+i);
	    }
	    //template.convertAndSend("Hello, world!");

	    //关掉环境
	    //Thread.sleep(1000);
	    ctx.close();
	}  
}  
```


接收消息配置  
applicationContext-rabbit.xml  

```  
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
	  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	  xmlns:context="http://www.springframework.org/schema/context"
	  xmlns:rabbit="http://www.springframework.org/schema/rabbit"
	  xsi:schemaLocation="http://www.springframework.org/schema/beans 
	  http://www.springframework.org/schema/beans/spring-beans.xsd
	  http://www.springframework.org/schema/context 
	  http://www.springframework.org/schema/context/spring-context.xsd
	  http://www.springframework.org/schema/rabbit 
	  http://www.springframework.org/schema/rabbit/spring-rabbit.xsd">
    
    <context:annotation-config/>
    <context:component-scan base-package="com.sep6th.base.core.listener"/>

    <!-- 配置ConnectionFactory,连接工厂 -->
    <rabbit:connection-factory id="connectionFactory"
        host="10.77.210.11" username="rabbit@user" 
        password="rabbit@pwd" port="5672" />

    <!-- RabbitAdmin主要用于创建队列和交换器以及绑定关系等 -->
    <rabbit:admin id="rabbitAdmin" connection-factory="connectionFactory" />

    <!-- 声明一个队列 -->
    <rabbit:queue name="myQueue" durable="true" auto-delete="false"/>

    <!-- 配置监听容器，指定消息处理类，处理方法，还可以配置自动确认等-->
    <rabbit:listener-container connection-factory="connectionFactory"  acknowledge="auto">
        <rabbit:listener ref="rabbitListener" queues="myQueue" />
        <!-- 可以继续注册监听 -->
    </rabbit:listener-container>  
    
</beans>  
```  

接收Java代码  

```  
package com.sep6th.base.core.listener;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

/** 
 * 
 * @author liuzy
 * @date 2018/08/06 11:21
 */
@Component
public class RabbitListener implements MessageListener  {
	
	Logger logger = LoggerFactory.getLogger(RabbitListener.class);

	@Override
	public void onMessage(Message message) {
		String msg = null;
        try {
            msg = new String(message.getBody(),"UTF-8");
            logger.debug("received:"+msg);
            logger.debug("received:"+message.toString());
            if(msg.contains("15")) {
            	throw new UnsupportedEncodingException("测试添加数据库时，出现异常！队列里的消息还是被消耗掉了。要捕捉保存异常数据。");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
	}	
}  
``` 
web.xml要加载到以上配置文件。最好把变量配置提到rabbit.properties文件里。  
比如：  
rabbit.properties  
```  
rabbit.host=10.77.210.11  
rabbit.username=rabbit@user  
rabbit.password=rabbit@pwd  
rabbit.port=5672  
//其他你想提出的变量  
```  
记得在applicationContext.xml加载rabbit.properties
