package cn.kunter.seata.saga.order.dao;

import cn.kunter.seata.saga.order.eo.Order;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Order Dao Test
 * @author nature
 * @version 1.0 2020/6/29
 */
@Slf4j
@SpringBootTest
class OrderDaoTests {

    @Resource
    private OrderDao orderDao;

    @Test
    void contextLoads() {
        assertNotNull(orderDao);
    }

    @Test
    @Transactional
    void testSelectById() {
        Long id = 1l;
        Order order = orderDao.selectById(id);
        assertNotNull(order);
        log.info(JSON.toJSONString(order));
    }

}