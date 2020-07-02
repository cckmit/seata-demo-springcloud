package cn.kunter.seata.tcc.account.dao;

import cn.kunter.seata.tcc.account.eo.Account;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Account Dao Test
 * @author nature
 * @version 1.0 2020/7/1
 */
@Slf4j
@SpringBootTest
class AccountDaoTests {

    @Resource
    private AccountDao accountDao;

    @Test
    void contextLoads() {
        assertNotNull(accountDao);
    }

    @Test
    @Transactional
    void testInsert() {
        Account account = new Account();
        account.setId(2l);
        account.setBalance(200.0);
        account.setTransBalance(0.0);
        int result = accountDao.insert(account);
        assertNotNull(result);
        assertEquals(result, 1);
    }

    @Test
    @Transactional
    void testSelectById() {
        Long id = 2l;

        Account account = new Account();
        account.setId(id);
        account.setBalance(200.0);
        account.setTransBalance(0.0);
        int result = accountDao.insert(account);
        assertNotNull(result);
        assertEquals(result, 1);

        Account accountResult = accountDao.selectById(id);
        assertNotNull(accountResult);
        log.info(JSON.toJSONString(accountResult));
    }

    @Test
    @Transactional
    void testUpdateById() {
        Account account = new Account();
        account.setId(2l);
        account.setBalance(200.0);
        account.setTransBalance(0.0);
        int result = accountDao.insert(account);
        assertNotNull(result);
        assertEquals(result, 1);

        account.setTransBalance(1.0);
        int updateResult = accountDao.updateById(account);
        assertNotNull(updateResult);
        assertEquals(updateResult, 1);
    }

}