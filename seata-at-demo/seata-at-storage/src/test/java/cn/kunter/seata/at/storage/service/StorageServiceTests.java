package cn.kunter.seata.at.storage.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Storage Service Test
 * @author nature
 * @version 1.0 2020/6/28
 */
@Slf4j
@SpringBootTest
class StorageServiceTests {

    @Autowired
    private StorageService storageService;

    @Test
    void contextLoads() {
        assertNotNull(storageService);
    }

    @Test
    void testReduceStockSuccess() throws Exception {
        Long productId = 1l;
        Integer number = 0;
        Double result = storageService.reduceStock(productId, number);
        assertNotNull(result);
        log.info(JSON.toJSONString(result));
    }

    @Test
    void testReduceStockFail() {
        Long productId = 1l;
        Integer number = 50;
        Exception exception = assertThrows(Exception.class, () -> {
            Double result = storageService.reduceStock(productId, number);
            assertNotNull(result);
            log.info(JSON.toJSONString(result));
        });
        assertEquals("库存不足", exception.getMessage());
    }

}