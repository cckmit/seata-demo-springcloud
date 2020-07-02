# seata-saga-demo
基于SpringCloud的Seata SAGA模式的Demo。

注意：Demo作为Seata的使用和配置演示，内部代码处理可能存在逻辑不严谨，仅供参考！

## 1、开发步骤
- 1、SAGA是一种长事务解决方案，业务流程中每个参与者都需要提供正向和补偿两个接口，当某一个参与者失败则执行前面已经成功的参与者的补偿服务
- 2、各服务的接口需要自行保证接口幂等，需要注意补偿接口在正向接口之前执行的业务处理，同时如补偿接口先于正向接口之前执行，正向接口不应当再次被执行
- 3、订单服务作为分布式事务发起方，需要添加Seata的依赖spring-cloud-starter-alibaba-seata，添加配置spring.cloud.alibaba.seata.tx-service-group=my_test_tx_group和seata.enable-auto-data-source-proxy=false（关闭数据源代理）
- 4、通过编写状态机JSON文件，来编排分布式事务处理流程，文件存放于订单服务的src/main/resources/statelang目录下
- 5、装配Seata的StateMachineEngine（参考demo的src/main/java/cn/com/gzcb/creditcard/saga/order/config/SeataConfig.java）
- 6、利用装配好的StateMachineEngine发起分布式事务调用（参考demo的cn/com/gzcb/creditcard/saga/order/controller/OrderController.java）

## 2、Demo运行说明
- 1、启动seata-server
- 2、利用doc目录下的db.sql，创建各模块的业务库表以及Seata的undo_log表
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
