package cn.kunter.seata.tcc.storage.dao;

import cn.kunter.seata.tcc.storage.eo.Product;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Product Dao Test
 * @author nature
 * @version 1.0 2020/7/1
 */
@Slf4j
@SpringBootTest
class ProductDaoTests {

    @Resource
    private ProductDao productDao;

    @Test
    void contextLoads() {
        assertNotNull(productDao);
    }

    @Test
    @Transactional
    void testInsert() {
        Product product = new Product();
        product.setId(2l);
        product.setPrice(5.0);
        product.setStock(10);
        product.setTransStock(0);
        int result = productDao.insert(product);
        assertNotNull(result);
        assertEquals(result, 1);
    }

    @Test
    @Transactional
    void testSelectById() {
        Long id = 2l;

        Product product = new Product();
        product.setId(id);
        product.setPrice(5.0);
        product.setStock(10);
        product.setTransStock(0);
        int result = productDao.insert(product);
        assertNotNull(result);
        assertEquals(result, 1);

        Product productResult = productDao.selectById(id);
        assertNotNull(productResult);
        log.info(JSON.toJSONString(productResult));
    }

    @Test
    @Transactional
    void testUpdateById() {
        Long id = 2l;

        Product product = new Product();
        product.setId(id);
        product.setPrice(5.0);
        product.setStock(10);
        product.setTransStock(0);
        int result = productDao.insert(product);
        assertNotNull(result);
        assertEquals(result, 1);

        product.setTransStock(1);
        int updateResult = productDao.updateById(product);
        assertNotNull(updateResult);
        assertEquals(updateResult, 1);
    }

}