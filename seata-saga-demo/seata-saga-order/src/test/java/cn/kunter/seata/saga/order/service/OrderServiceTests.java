package cn.kunter.seata.saga.order.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Order Service Test
 * @author nature
 * @version 1.0 2020/6/29
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

    @Test
    @Transactional
    void testPlaceOrder() throws Exception {
        String orderId = System.currentTimeMillis() + "";
        Long userId = 1l;
        Long productId = 1l;
        Integer number = 1;
        boolean result = orderService.placeOrder(orderId, userId, productId, number);
        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    @Transactional
    void testRevokeOrder() throws Exception {
        String orderId = System.currentTimeMillis() + "";
        Long userId = 1l;
        Long productId = 1l;
        Integer number = 1;
        boolean result = orderService.placeOrder(orderId, userId, productId, number);
        assertNotNull(result);
        assertTrue(result);

        boolean revokeResult = orderService.revokeOrder(orderId);
        assertNotNull(revokeResult);
        assertTrue(revokeResult);
    }

}