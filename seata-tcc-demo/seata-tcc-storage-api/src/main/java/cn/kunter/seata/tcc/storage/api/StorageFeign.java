package cn.kunter.seata.tcc.storage.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Storage Feign API
 * @author nature
 * @version 1.0 2020/7/1
 */
@FeignClient(name = "seata-tcc-storage", url = "http://localhost:8062")
public interface StorageFeign {

    /**
     * 扣减库存
     * @param businessKey 业务ID
     * @param productId 商品ID
     * @param number 待扣减数量
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @PostMapping("/storage/reduceStock")
    Boolean reduceStock(@RequestParam("businessKey") Long businessKey, @RequestParam("productId") Long productId,
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
