package cn.kunter.seata.at.order;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Application Main
 * @author nature
 * @version 1.0 2020/6/28
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableFeignClients(basePackages = {"cn.kunter.seata.at.account.api", "cn.kunter.seata.at.storage.api"})
@MapperScan("cn.kunter.seata.at.order.dao")
public class SeataAtOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataAtOrderApplication.class, args);
    }

}
