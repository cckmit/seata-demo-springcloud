package cn.kunter.seata.tcc.storage;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application Main
 * @author nature
 * @version 1.0 2020/7/1
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@MapperScan("cn.kunter.seata.tcc.storage.dao")
public class SeataTccStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataTccStorageApplication.class, args);
    }

}
