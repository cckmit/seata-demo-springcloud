package cn.kunter.seata.tcc.storage.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Storage Service Test
 * @author nature
 * @version 1.0 2020/7/1
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
        Long businessKey = System.currentTimeMillis();
        Long productId = 1l;
        Integer number = 1;
        boolean result = storageService.reduceStock(businessKey, productId, number);
        assertTrue(result);
    }

    @Test
    @Transactional
    void testCommitStock() throws Exception {
        Long businessKey = System.currentTimeMillis();
        Long productId = 1l;
        Integer number = 1;
        boolean reduceResult = storageService.reduceStock(businessKey, productId, number);
        assertTrue(reduceResult);

        Map<String, Object> actionContextMap = new HashMap<>();
        actionContextMap.put("businessKey", businessKey);
        actionContextMap.put("productId", productId);
        actionContextMap.put("number", number);
        BusinessActionContext businessActionContext = new BusinessActionContext();
        businessActionContext.setActionContext(actionContextMap);
        boolean commitResult = storageService.commitStock(businessActionContext);
        assertTrue(commitResult);
    }

    @Test
    @Transactional
    void testRollbackStock() throws Exception {
        Long businessKey = System.currentTimeMillis();
        Long productId = 1l;
        Integer number = 1;
        boolean reduceResult = storageService.reduceStock(businessKey, productId, number);
        assertTrue(reduceResult);

        Map<String, Object> actionContextMap = new HashMap<>();
        actionContextMap.put("businessKey", businessKey);
        actionContextMap.put("productId", productId);
        actionContextMap.put("number", number);
        BusinessActionContext businessActionContext = new BusinessActionContext();
        businessActionContext.setActionContext(actionContextMap);
        boolean rollbackResult = storageService.rollbackStock(businessActionContext);
        assertTrue(rollbackResult);
    }

}