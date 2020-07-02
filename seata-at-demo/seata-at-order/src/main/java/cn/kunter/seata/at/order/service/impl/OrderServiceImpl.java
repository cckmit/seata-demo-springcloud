package cn.kunter.seata.at.order.service.impl;

import cn.kunter.seata.at.account.api.AccountFeign;
import cn.kunter.seata.at.order.dao.OrderDao;
import cn.kunter.seata.at.order.enums.OrderStatus;
import cn.kunter.seata.at.order.eo.Order;
import cn.kunter.seata.at.order.service.OrderService;
import cn.kunter.seata.at.storage.api.StorageFeign;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Order Service Impl
 * @author nature
 * @version 1.0 2020/6/28
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private StorageFeign storageFeign;
    @Resource
    private AccountFeign accountFeign;

    @GlobalTransactional
    @Override
    public Boolean placeOrder(Long userId, Long productId, Integer number) throws Exception {

        log.info("当前XID: {}", RootContext.getXID());

        Order order = Order.builder().userId(userId).productId(productId).number(number).status(OrderStatus.INIT)
                           .build();
        Integer saveOrderRecord = orderDao.insert(order);
        log.info("保存订单结果: {}", saveOrderRecord > 0 ? "成功" : "失败");

        // 扣减库存并计算总价
        Double totalPrice = storageFeign.reduceStock(productId, number);
        // 扣减余额
        boolean balanceResult = accountFeign.reduceBalance(userId, totalPrice);

        order.setStatus(OrderStatus.SUCCESS);
        Integer updateOrderRecord = orderDao.updateById(order);
        log.info("更新订单 {} 结果: {}", order.getId(), updateOrderRecord > 0 ? "成功" : "失败");

        return updateOrderRecord > 0;
    }

}
