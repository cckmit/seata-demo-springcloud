package cn.kunter.seata.at.storage;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application Main
 * @author nature
 * @version 1.0 2020/6/28
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@MapperScan("cn.kunter.seata.at.storage.dao")
public class SeataAtStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataAtStorageApplication.class, args);
    }

}
