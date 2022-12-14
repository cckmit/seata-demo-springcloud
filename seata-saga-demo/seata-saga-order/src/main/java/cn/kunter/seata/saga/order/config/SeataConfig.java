package cn.kunter.seata.saga.order.config;

import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.engine.config.DbStateMachineConfig;
import io.seata.saga.engine.impl.ProcessCtrlStateMachineEngine;
import io.seata.saga.rm.StateMachineEngineHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Seata Config
 * @author nature
 * @version 1.0 2020/6/29
 */
@Configuration
public class SeataConfig {

    @Value("${spring.application.name}")
    private String applicationId;
    @Value("${spring.cloud.alibaba.seata.tx-service-group}")
    private String txServiceGroup;


    @Bean
    public StateMachineEngine getStateMachineEngine(DbStateMachineConfig dbStateMachineConfig) {

        ProcessCtrlStateMachineEngine stateMachineEngine = new ProcessCtrlStateMachineEngine();
        stateMachineEngine.setStateMachineConfig(dbStateMachineConfig);

        return stateMachineEngine;
    }

    @Bean
    public DbStateMachineConfig getDbStateMachineConfig(DataSource dataSource, ThreadPoolExecutor threadPoolExecutor) throws IOException {

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources("classpath:statelang/*.json");

        DbStateMachineConfig dbStateMachineConfig = new DbStateMachineConfig();
        dbStateMachineConfig.setDataSource(dataSource);
        dbStateMachineConfig.setResources(resources);
        dbStateMachineConfig.setEnableAsync(true);
        dbStateMachineConfig.setThreadPoolExecutor(threadPoolExecutor);
        dbStateMachineConfig.setApplicationId(applicationId);
        dbStateMachineConfig.setTxServiceGroup(txServiceGroup);

        return dbStateMachineConfig;
    }

    /**
     * ???????????????????????????????????????, ???????????????????????????????????????????????????
     */
    @Bean
    public ThreadPoolExecutor getThreadPoolExecutor() {

        ThreadPoolTaskExecutor threadExecutor = new ThreadPoolTaskExecutor();
        threadExecutor.initialize();
        threadExecutor.setThreadNamePrefix("SAGA_ASYNC_EXE_");
        threadExecutor.setCorePoolSize(1);
        threadExecutor.setMaxPoolSize(20);

        return threadExecutor.getThreadPoolExecutor();
    }

    /**
     * Seata Server???????????????????????????????????????Holder??????stateMachineEngine??????
     */
    @Bean
    public StateMachineEngineHolder getStateMachineEngineHolder(StateMachineEngine stateMachineEngine) {

        StateMachineEngineHolder stateMachineEngineHolder = new StateMachineEngineHolder();
        stateMachineEngineHolder.setStateMachineEngine(stateMachineEngine);

        return stateMachineEngineHolder;
    }

}
