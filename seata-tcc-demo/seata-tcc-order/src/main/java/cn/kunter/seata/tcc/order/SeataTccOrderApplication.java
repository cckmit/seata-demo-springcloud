package cn.kunter.seata.tcc.order;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Application Main
 * @author nature
 * @version 1.0 2020/7/1
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableFeignClients(basePackages = {"cn.kunter.seata.tcc.account.api", "cn.kunter.seata.tcc.storage.api"})
@MapperScan("cn.kunter.seata.tcc.order.dao")
public class SeataTccOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataTccOrderApplication.class, args);
    }

}
