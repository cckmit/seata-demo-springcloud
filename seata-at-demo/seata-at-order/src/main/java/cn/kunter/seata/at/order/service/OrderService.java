package cn.kunter.seata.at.order.service;

/**
 * Order Service
 * @author nature
 * @version 1.0 2020/6/28
 */
public interface OrderService {

    /**
     * 创建订单
     * @param userId 用户ID
     * @param productId 商品ID
     * @param number 订购数量
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    Boolean placeOrder(Long userId, Long productId, Integer number) throws Exception;

}
