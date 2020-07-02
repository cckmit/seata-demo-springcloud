package cn.kunter.seata.tcc.account.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Account Service Test
 * @author nature
 * @version 1.0 2020/7/1
 */
@SpringBootTest
class AccountServiceTests {

    @Autowired
    private AccountService accountService;

    @Test
    void contextLoads() {
        assertNotNull(accountService);
    }

    @Test
    @Transactional
    void testReduceBalance() throws Exception {
        Long businessKey = System.currentTimeMillis();
        Long userId = 1l;
        Double amount = 1.0;
        boolean result = accountService.reduceBalance(businessKey, userId, amount);
        assertTrue(result);
    }

    @Test
    @Transactional
    void testCommitBalance() throws Exception {
        Long businessKey = System.currentTimeMillis();
        Long userId = 1l;
        Double amount = 1.0;
        boolean result = accountService.reduceBalance(businessKey, userId, amount);
        assertTrue(result);

        Map<String, Object> actionContextMap = new HashMap<>();
        actionContextMap.put("businessKey", businessKey);
        actionContextMap.put("userId", userId);
        actionContextMap.put("amount", amount);
        BusinessActionContext businessActionContext = new BusinessActionContext();
        businessActionContext.setActionContext(actionContextMap);
        boolean commitResult = accountService.commitBalance(businessActionContext);
        assertTrue(commitResult);
    }

    @Test
    @Transactional
    void testRollbackBalance() throws Exception {
        Long businessKey = System.currentTimeMillis();
        Long userId = 1l;
        Double amount = 1.0;
        boolean reduceResult = accountService.reduceBalance(businessKey, userId, amount);
        assertTrue(reduceResult);

        Map<String, Object> actionContextMap = new HashMap<>();
        actionContextMap.put("businessKey", businessKey);
        actionContextMap.put("userId", userId);
        actionContextMap.put("amount", amount);
        BusinessActionContext businessActionContext = new BusinessActionContext();
        businessActionContext.setActionContext(actionContextMap);
        boolean rollbackResult = accountService.rollbackBalance(businessActionContext);
        assertTrue(rollbackResult);
    }

}