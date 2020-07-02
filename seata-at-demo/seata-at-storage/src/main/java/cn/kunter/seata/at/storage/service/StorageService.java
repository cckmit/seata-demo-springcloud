package cn.kunter.seata.at.storage.service;

/**
 * Storage Service
 * @author nature
 * @version 1.0 2020/6/28
 */
public interface StorageService {

    /**
     * 扣减库存
     * @param productId 商品ID
     * @param number 待扣减数量
     * @return Double 计算出的总价
     * @throws Exception 失败时抛出异常
     */
    Double reduceStock(Long productId, Integer number) throws Exception;

}
