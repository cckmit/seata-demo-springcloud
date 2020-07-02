package cn.kunter.seata.saga.storage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application Main
 * @author nature
 * @version 1.0 2020/6/29
 */
@SpringBootApplication
@MapperScan("cn.kunter.seata.saga.storage.dao")
public class SeataSagaStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataSagaStorageApplication.class, args);
    }

}
