package cn.kunter.seata.at.account;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application Main
 * @author nature
 * @version 1.0 2020/6/24
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@MapperScan("cn.kunter.seata.at.account.dao")
public class SeataAtAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataAtAccountApplication.class, args);
    }

}
