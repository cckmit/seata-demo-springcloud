package cn.kunter.seata.at.account.dao;

import cn.kunter.seata.at.account.eo.Account;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Account Dao Test
 * @author nature
 * @version 1.0 2020/6/28
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
    void testSelectById() {
        Long id = 1l;
        Account account = accountDao.selectById(id);
        assertNotNull(account);
        log.info(JSON.toJSONString(account));
    }

    @Test
    void testSelectList() {
        QueryWrapper queryWrapper = new QueryWrapper();
        List<Account> accountList = accountDao.selectList(queryWrapper);
        assertNotNull(accountList);
        log.info(JSON.toJSONString(accountList));
    }

}