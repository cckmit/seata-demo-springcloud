package cn.kunter.seata.tcc.order.dao;

import cn.kunter.seata.tcc.order.enums.OrderStatus;
import cn.kunter.seata.tcc.order.eo.Order;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Order Dao Test
 * @author nature
 * @version 1.0 2020/7/1
 */
@Slf4j
@SpringBootTest
class OrderDaoTests {

    private static final Long TEST_ORDER_ID = System.currentTimeMillis();

    @Resource
    private OrderDao orderDao;

    @Test
    void contextLoads() {
        assertNotNull(orderDao);
    }

    @Test
    void testInsert() {
        Order order =
                Order.builder().id(TEST_ORDER_ID).userId(1l).productId(1l).number(1).status(OrderStatus.INIT).build();
        int result = orderDao.insert(order);
        assertNotNull(result);
        assertEquals(result, 1);
    }

    @Test
    void testSelectById() {
        Order order = orderDao.selectById(TEST_ORDER_ID);
        assertNotNull(order);
        log.info(JSON.toJSONString(order));
    }

    @Test
    void testSelectList() {
        QueryWrapper queryWrapper = new QueryWrapper();
        List<Order> orderList = orderDao.selectList(queryWrapper);
        assertNotNull(orderList);
        log.info(JSON.toJSONString(orderList));
    }

}