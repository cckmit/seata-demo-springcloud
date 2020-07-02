package cn.kunter.seata.saga.storage.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Storage Service Test
 * @author nature
 * @version 1.0 2020/6/29
 */
@Slf4j
@SpringBootTest
class StorageServiceTests {

    @Autowired
    private StorageService storageService;

    @Test
    void contextLoads() {
        assertNotNull(storageService);
    }

    @Test
    @Transactional
    void testReduceStock() throws Exception {
        String businessKey = System.currentTimeMillis() + "";
        Long productId = 1l;
        Integer number = 0;
        boolean result = storageService.reduceStock(businessKey, productId, number);
        assertTrue(result);
    }

    @Test
    @Transactional
    void testCompensateStock() throws Exception {
        String businessKey = System.currentTimeMillis() + "";
        Long productId = 1l;
        Integer number = 0;
        boolean result = storageService.reduceStock(businessKey, productId, number);
        assertTrue(result);

        boolean compensateResult = storageService.compensateStock(businessKey, productId, number);
        assertTrue(compensateResult);
    }

}