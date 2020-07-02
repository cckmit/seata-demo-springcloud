package cn.kunter.seata.saga.account.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Account Service Test
 * @author nature
 * @version 1.0 2020/6/29
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
        String businessKey = System.currentTimeMillis() + "";
        Long userId = 1l;
        Double amount = 0.0;
        boolean result = accountService.reduceBalance(businessKey, userId, amount);
        assertTrue(result);
    }

    @Test
    @Transactional
    void testCompensateBalance() throws Exception {
        String businessKey = System.currentTimeMillis() + "";
        Long userId = 1l;
        Double amount = 0.0;
        boolean result = accountService.reduceBalance(businessKey, userId, amount);
        assertTrue(result);

        boolean compensateResult = accountService.compensateBalance(businessKey, userId, amount);
        assertTrue(compensateResult);
    }


}