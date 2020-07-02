package cn.kunter.seata.saga.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Application Main
 * @author nature
 * @version 1.0 2020/6/29
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"cn.kunter.seata.saga.account.api",
        "cn.kunter.seata.saga.storage.api"})
@MapperScan("cn.com.gzcb.creditcard.saga.order.dao")
public class SeataSagaOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataSagaOrderApplication.class, args);
    }

}
