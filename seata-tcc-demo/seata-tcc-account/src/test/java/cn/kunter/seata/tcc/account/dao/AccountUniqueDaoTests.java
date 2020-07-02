package cn.kunter.seata.tcc.account.dao;

import cn.kunter.seata.tcc.account.eo.AccountUnique;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Account Unique Dao Test
 * @author nature
 * @version 1.0 2020/7/1
 */
@Slf4j
@SpringBootTest
class AccountUniqueDaoTests {

    @Resource
    private AccountUniqueDao accountUniqueDao;

    @Test
    void contextLoads() {
        assertNotNull(accountUniqueDao);
    }

    @Test
    @Transactional
    void testInsert() {
        AccountUnique accountUnique = new AccountUnique();
        accountUnique.setUniqueKey(System.currentTimeMillis() + "");
        int result = accountUniqueDao.insert(accountUnique);
        assertNotNull(result);
        assertEquals(result, 1);
    }

    @Test
    @Transactional
    void testSelectById() {
        String key = System.currentTimeMillis() + "";

        AccountUnique accountUnique = new AccountUnique();
        accountUnique.setUniqueKey(key);
        int result = accountUniqueDao.insert(accountUnique);
        assertNotNull(result);
        assertEquals(result, 1);

        AccountUnique accountUniqueResult = accountUniqueDao.selectById(key);
        assertNotNull(accountUniqueResult);
        log.info(JSON.toJSONString(accountUniqueResult));
    }

}