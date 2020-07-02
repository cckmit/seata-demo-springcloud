package cn.kunter.seata.tcc.order.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Order Service Test
 * @author nature
 * @version 1.0 2020/7/1
 */
@Slf4j
@SpringBootTest
class OrderServiceTests {

    @Autowired
    private OrderService orderService;

    @Test
    void contextLoads() {
        assertNotNull(orderService);
    }

    /**
     * 分布式事务，模拟下单成功
     */
    @Test
    void testPlaceOrderSuccess() throws Exception {
        Long userId = 1l;
        Long productId = 1l;
        Integer number = 1;
        boolean result = orderService.placeOrder(null, userId, productId, number);
        assertNotNull(result);
        log.info(JSON.toJSONString(result));
    }

    /**
     * 分布式事务，模拟下单失败，库存不足
     */
    @Test
    void testPlaceOrderFail() {
        Long userId = 1l;
        Long productId = 1l;
        Integer number = 50;
        Exception exception = assertThrows(Exception.class, () -> {
            boolean result = orderService.placeOrder(null, userId, productId, number);
            assertNotNull(result);
            log.info(JSON.toJSONString(result));
        });
    }

}