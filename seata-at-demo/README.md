# seata-at-demo
基于SpringCloud的Seata AT模式的Demo。

## 1、开发步骤
- 1、AT模式在实现上对业务的入侵性不高，正常完成各服务的业务逻辑开发
- 2、各服务添加Seata的依赖spring-cloud-starter-alibaba-seata，添加配置spring.cloud.alibaba.seata.tx-service-group=my_test_tx_group
- 3、AT模式为数据源代理模式，事务的提交和回滚操作是根据业务的正常处理SQL自动生成，在采用Druid等数据连接池时需要在@SpringBootApplication注解添加exclude=DruidDataSourceAutoConfigure.class来排除Druid连接池的自动装配
- 4、在库存服务和账户服务的feign实现方法上添加@Transaction注解并<font color='red'>**设置事务传播特性为REQUIRES_NEW**</font>（开启新的事务）
    ```
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    ```
- 5、在订单服务的Service实现方法上添加注解@GlobalTransaction注解，标记开启Seata分布式事务，此时分布式事务配置完成

## 2、Demo运行说明
- 1、启动seata-server
- 2、利用doc目录下的db.sql，创建各模块的业务库表以及Seata的undo_log表
- 3、修改各模块的application.properties中MySQL连接配置
- 4、运行各模块的Application类，启动服务
- 5、测试分布式事务
    > 1、通过运行OrderServiceTests单元测试代码的方式测试分布式事务
        
        打开seata-at-order模块的src/test/java/cn/com/gzcb/creditcard/at/order/service/OrderServiceTests.java文件
        通过运行testPlaceOrderSuccess和testPlaceOrderFail来模拟事务成功和回滚
        
    > 2、通过浏览器或者Postman采用Http的方式测试分布式事务
        
        在浏览器或者postman中输入http://localhost:8070/order/place，传入参数：userId、productId、number，请求方法POST/GET

    ```
    在使用Demo提供的建表和添加基础数据脚本的情况下，
    模拟全局事务提交（业务成功）需要传入参数：userId=1、productId=1、number=1；
    模拟全局事务回滚（业务失败）库存不足，需要传入参数：userId=1、productId=1、number=20；
    模拟全局事务回滚（业务失败）余额不足，需要传入参数：userId=1、productId=1、number=5；
    ```
