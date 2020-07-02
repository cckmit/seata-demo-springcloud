package cn.kunter.seata.at.order.dao;

import cn.kunter.seata.at.order.eo.Order;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Order Dao Test
 * @author nature
 * @version 1.0 2020/6/28
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
    void testSelectById() {
        Long id = 1l;
        Order order = orderDao.selectById(id);
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