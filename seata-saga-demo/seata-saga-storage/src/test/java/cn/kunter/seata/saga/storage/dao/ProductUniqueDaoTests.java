package cn.kunter.seata.saga.storage.dao;

import cn.kunter.seata.saga.storage.eo.ProductUnique;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Product Unique Dao Test
 * @author nature
 * @version 1.0 2020/6/29
 */
@Slf4j
@SpringBootTest
class ProductUniqueDaoTests {

    @Resource
    private ProductUniqueDao productUniqueDao;

    @Test
    void contextLoads() {
        assertNotNull(productUniqueDao);
    }

    @Test
    @Transactional
    void testSelectById() {
        String key = "1593412269845";
        ProductUnique productUnique = productUniqueDao.selectById(key);
        assertNotNull(productUnique);
        log.info(JSON.toJSONString(productUnique));
    }

    @Test
    @Transactional
    void testInsert() {
        ProductUnique productUnique = new ProductUnique();
        productUnique.setUniqueKey(System.currentTimeMillis() + "");
        int result = productUniqueDao.insert(productUnique);
        assertNotNull(result);
    }

}