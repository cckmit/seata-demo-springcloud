package cn.kunter.seata.saga.storage.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Storage Feign API
 * @author nature
 * @version 1.0 2020/6/29
 */
@FeignClient(name = "seata-saga-storage", url = "http://localhost:8082")
public interface StorageFeign {

    /**
     * 扣减库存
     * @param businessKey 业务Key
     * @param productId 商品ID
     * @param number 待扣减数量
     * @return Double 计算出的总价
     */
    @PostMapping("/storage/reduceStock")
    Boolean reduceStock(@RequestParam("businessKey") String businessKey, @RequestParam("productId") Long productId,
                        @RequestParam("number") Integer number) throws Exception;

    /**
     * 补偿库存
     * @param businessKey 业务Key
     * @param productId 商品ID
     * @param number 待补偿数量
     * @return Boolean 操作结果
     */
    @PostMapping("/storage/compensateStock")
    Boolean compensateStock(@RequestParam("businessKey") String businessKey, @RequestParam("productId") Long productId,
                            @RequestParam("number") Integer number) throws Exception;

    /**
     * 根据商品ID和数量获取总价
     * @param productId 商品ID
     * @param number 商品数量
     * @return Double 计算出的总价
     */
    @PostMapping("/storage/getTotalPrice")
    Double getTotalPrice(@RequestParam("productId") Long productId, @RequestParam("number") Integer number);

}
