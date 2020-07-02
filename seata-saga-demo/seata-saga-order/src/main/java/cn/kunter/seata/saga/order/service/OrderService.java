package cn.kunter.seata.saga.order.service;

/**
 * Order Service
 * @author nature
 * @version 1.0 2020/6/29
 */
public interface OrderService {

    /**
     * 创建订单
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param productId 商品ID
     * @param number 订购数量
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    Boolean placeOrder(String orderId, Long userId, Long productId, Integer number) throws Exception;

    /**
     * 撤销订单
     * @param orderId 订单ID
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    Boolean revokeOrder(String orderId) throws Exception;

}
