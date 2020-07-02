package cn.kunter.seata.saga.order.service.impl;

import cn.kunter.seata.saga.order.dao.OrderDao;
import cn.kunter.seata.saga.order.enums.OrderStatus;
import cn.kunter.seata.saga.order.eo.Order;
import cn.kunter.seata.saga.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Order Service Impl
 * @author nature
 * @version 1.0 2020/6/29
 */
@Slf4j
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    /**
     * 创建订单
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param productId 商品ID
     * @param number 订购数量
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean placeOrder(String orderId, Long userId, Long productId, Integer number) throws Exception {

        Order order = new Order();
        order.setId(Long.valueOf(orderId));
        order.setUserId(userId);
        order.setProductId(productId);
        order.setNumber(number);
        order.setStatus(OrderStatus.SUCCESS);

        Integer saveResult = orderDao.insert(order);
        log.info("保存订单 {} 结果: {}", orderId, saveResult > 0 ? "成功" : "失败");

        return saveResult > 0;
    }

    /**
     * 撤销订单
     * @param orderId 订单ID
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean revokeOrder(String orderId) throws Exception {

        Order order = new Order();
        order.setId(Long.valueOf(orderId));
        order.setStatus(OrderStatus.FAIL);
        Integer updateResult = orderDao.updateById(order);
        log.info("撤销订单 {} 结果: {}", orderId, updateResult > 0 ? "成功" : "失败");

        return updateResult > 0;
    }

}
