package cn.kunter.seata.saga.storage.service;

/**
 * Storage Service
 * @author nature
 * @version 1.0 2020/6/29
 */
public interface StorageService {

    /**
     * 扣减库存
     * @param businessKey 业务Key
     * @param productId 商品ID
     * @param number 待扣减数量
     * @return Double 计算出的总价
     * @throws Exception 失败时抛出异常
     */
    Boolean reduceStock(String businessKey,Long productId, Integer number) throws Exception;

    /**
     * 补偿库存
     * @param businessKey 业务Key
     * @param productId 商品ID
     * @param number 待补偿数量
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    Boolean compensateStock(String businessKey,Long productId, Integer number) throws Exception;

    /**
     * 根据商品ID和数量获取总价
     * @param productId 商品ID
     * @param number 商品数量
     * @return Double 计算出的总价
     */
    Double getTotalPrice(Long productId, Integer number);

}
