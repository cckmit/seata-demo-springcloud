package cn.kunter.seata.saga.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application Main
 * @author nature
 * @version 1.0 2020/6/29
 */
@SpringBootApplication
@MapperScan("cn.kunter.seata.saga.account.dao")
public class SeataSagaAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataSagaAccountApplication.class, args);
    }

}
