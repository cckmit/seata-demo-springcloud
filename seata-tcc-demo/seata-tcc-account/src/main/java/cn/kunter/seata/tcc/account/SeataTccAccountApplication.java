package cn.kunter.seata.tcc.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application Main
 * @author nature
 * @version 1.0 2020/7/1
 */
@SpringBootApplication
@MapperScan("cn.kunter.seata.tcc.account.dao")
public class SeataTccAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataTccAccountApplication.class, args);
    }

}
