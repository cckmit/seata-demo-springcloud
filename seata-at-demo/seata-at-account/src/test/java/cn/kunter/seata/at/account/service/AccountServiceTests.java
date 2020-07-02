package cn.kunter.seata.at.account.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Account Service Test
 * @author nature
 * @version 1.0 2020/6/28
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
    void testReduceBalanceSuccess() throws Exception {
        Long userId = 1l;
        Double amount = 0.0;
        boolean result = accountService.reduceBalance(userId, amount);
        assertTrue(result);
    }

    @Test
    void testReduceBalanceFail() {
        Long userId = 1l;
        Double amount = 50.0;
        Exception exception = assertThrows(Exception.class, () -> {
            boolean result = accountService.reduceBalance(userId, amount);
            assertFalse(result);
        });
        assertEquals("余额不足", exception.getMessage());
    }


}