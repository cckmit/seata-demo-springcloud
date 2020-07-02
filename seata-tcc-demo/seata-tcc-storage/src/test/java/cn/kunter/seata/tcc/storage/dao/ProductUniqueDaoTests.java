package cn.kunter.seata.tcc.storage.dao;

import cn.kunter.seata.tcc.storage.eo.ProductUnique;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Product Unique Dao Test
 * @author nature
 * @version 1.0 2020/7/1
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
    void testInsert() {
        ProductUnique productUnique = new ProductUnique();
        productUnique.setUniqueKey(System.currentTimeMillis() + "");
        int result = productUniqueDao.insert(productUnique);
        assertNotNull(result);
        assertEquals(result, 1);
    }

    @Test
    @Transactional
    void testSelectById() {
        String key = System.currentTimeMillis() + "";

        ProductUnique productUnique = new ProductUnique();
        productUnique.setUniqueKey(key);
        int result = productUniqueDao.insert(productUnique);
        assertNotNull(result);
        assertEquals(result, 1);

        ProductUnique productUniqueResult = productUniqueDao.selectById(key);
        assertNotNull(productUniqueResult);
        log.info(JSON.toJSONString(productUniqueResult));
    }

}