# seata-tcc-demo
基于SpringCloud的Seata TCC模式的Demo。

注意：Demo作为Seata的使用和配置演示，内部代码处理可能存在逻辑不严谨，仅供参考！

## 1、开发步骤
- 1、TCC模式对业务入侵性很大，需要业务自行设计实现事务两阶段的prepare、commit、rollback三个行为接口
- 2、各服务接口需要保证幂等性，同时需要注意commit、rollback在prepare之前执行的处理
- 3、各服务添加Seata的依赖spring-cloud-starter-alibaba-seata，添加配置spring.cloud.alibaba.seata.tx-service-group=my_test_tx_group和seata.enable-auto-data-source-proxy=false（关闭数据源代理）
- 4、各服务的Service层添加类注解@LocalTCC，在prepare接口上添加注解@TwoPhaseBusinessAction，并设置其name、commitMethod、rollbackMethod参数，需要保证name的全局唯一性，commitMethod和rollbackMethod为commit、rollback行为接口方法名
- 5、prepare行为接口方法的参数需要被注解@BusinessActionContextParameter标记，并设置parmaName参数；commit和rollback行为接口的入参只能为BusinessActionContext
- 6、订单服务的Controller层作为分布式事务发起入口，需要在方法上添加注解@GlobalTransaction，标记分布式事务的开启
- 7、业务入口只需要调用各服务的prepare接口，commit和rollback接口为Seata根据各服务的prepare接口执行结果自动发起调用

## 2、Demo运行说明
- 1、启动seata-server
- 2、利用doc目录下的db.sql，创建各模块的业务库表
- 3、修改各模块的application.properties中MySQL连接配置
- 4、运行各模块的Application类，启动服务
- 5、测试分布式事务
    > 通过浏览器或者Postman采用Http的方式测试分布式事务
        
        在浏览器或者postman中输入http://localhost:8080/order/place，传入参数：userId、productId、number，请求方法POST/GET
    
    ```
    在使用Demo提供的建表和添加基础数据脚本的情况下，
    模拟全局事务提交（业务成功）需要传入参数：userId=1、productId=1、number=1；
    模拟全局事务回滚（业务失败）库存不足，需要传入参数：userId=1、productId=1、number=20；
    模拟全局事务回滚（业务失败）余额不足，需要传入参数：userId=1、productId=1、number=5；
    ```
