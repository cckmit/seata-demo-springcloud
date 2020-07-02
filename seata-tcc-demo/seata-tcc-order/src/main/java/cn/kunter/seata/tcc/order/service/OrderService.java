package cn.kunter.seata.tcc.order.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * Order Service
 * @author nature
 * @version 1.0 2020/7/1
 */
@LocalTCC
public interface OrderService {

    /**
     * 创建订单
     * @param userId 用户ID
     * @param productId 商品ID
     * @param number 订购数量
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @TwoPhaseBusinessAction(name = "OrderService", commitMethod = "commitOrder", rollbackMethod = "rollbackOrder")
    Boolean placeOrder(@BusinessActionContextParameter(paramName = "orderId") Long orderId,
                       @BusinessActionContextParameter(paramName = "userId") Long userId,
                       @BusinessActionContextParameter(paramName = "productId") Long productId,
                       @BusinessActionContextParameter(paramName = "number") Integer number) throws Exception;

    /**
     * 提交订单
     */
    Boolean commitOrder(BusinessActionContext actionContext) throws Exception;

    /**
     * 回滚订单
     */
    Boolean rollbackOrder(BusinessActionContext actionContext) throws Exception;

}
