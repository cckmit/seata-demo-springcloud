package cn.kunter.seata.saga.account.dao;

import cn.kunter.seata.saga.account.eo.Account;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Account Dao Test
 * @author nature
 * @version 1.0 2020/6/29
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
    void testSelectById() {
        Long id = 1l;
        Account account = accountDao.selectById(id);
        assertNotNull(account);
        log.info(JSON.toJSONString(account));
    }

    @Test
    @Transactional
    void testSelectList() {
        QueryWrapper queryWrapper = new QueryWrapper();
        List<Account> accountList = accountDao.selectList(queryWrapper);
        assertNotNull(accountList);
        log.info(JSON.toJSONString(accountList));
    }

    @Test
    @Transactional
    void testUpdateById() {
        Long id = 1l;
        Account account = accountDao.selectById(id);
        assertNotNull(account);
        log.info(JSON.toJSONString(account));

        account.setBalance(50.0);
        int result = accountDao.updateById(account);
        assertNotNull(result);
        assertEquals(result, 1, "");
    }

    @Test
    @Transactional
    void testInsert() {
        Account account = new Account();
        account.setBalance(10.0);
        int result = accountDao.insert(account);
        assertNotNull(result);
        assertEquals(result, 1, "");
    }

}