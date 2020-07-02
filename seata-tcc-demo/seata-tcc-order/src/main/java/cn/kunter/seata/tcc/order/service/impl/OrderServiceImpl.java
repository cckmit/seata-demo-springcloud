package cn.kunter.seata.tcc.order.service.impl;

import cn.kunter.seata.tcc.order.dao.OrderDao;
import cn.kunter.seata.tcc.order.enums.OrderStatus;
import cn.kunter.seata.tcc.order.eo.Order;
import cn.kunter.seata.tcc.order.service.OrderService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Order Service Impl
 * @author nature
 * @version 1.0 2020/7/1
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    /**
     * 创建订单
     * @param userId 用户ID
     * @param productId 商品ID
     * @param number 订购数量
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean placeOrder(Long orderId, Long userId, Long productId, Integer number) throws Exception {

        log.info("当前XID: {}", RootContext.getXID());

        Order order = Order.builder().id(orderId).userId(userId).productId(productId).number(number)
                           .status(OrderStatus.INIT).build();
        Integer saveRecord = orderDao.insert(order);
        log.info("保存订单 {} 结果: {}", order.getId(), saveRecord > 0 ? "成功" : "失败");

        return saveRecord > 0;
    }

    /**
     * 提交订单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean commitOrder(BusinessActionContext actionContext) throws Exception {

        log.info("当前XID: {}, Commit", actionContext.getXid());
        Long orderId = (long) actionContext.getActionContext("orderId");

        Order order = Order.builder().id(orderId).status(OrderStatus.SUCCESS).build();
        Integer updateRecord = orderDao.updateById(order);
        log.info("提交订单 {} 结果: {}", orderId, updateRecord > 0 ? "成功" : "失败");

        return updateRecord > 0;
    }

    /**
     * 回滚订单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean rollbackOrder(BusinessActionContext actionContext) throws Exception {

        log.info("当前XID: {}, Rollback", actionContext.getXid());
        Long orderId = (long) actionContext.getActionContext("orderId");

        Order order = Order.builder().id(orderId).status(OrderStatus.FAIL).build();
        Integer updateRecord = orderDao.updateById(order);
        log.info("撤销订单 {} 结果: {}", orderId, updateRecord > 0 ? "成功" : "失败");

        return updateRecord > 0;
    }

}
