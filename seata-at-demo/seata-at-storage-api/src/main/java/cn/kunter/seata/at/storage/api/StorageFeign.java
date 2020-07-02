package cn.kunter.seata.at.storage.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Storage Feign API
 * @author nature
 * @version 1.0 2020/6/24
 */
@FeignClient(name = "seata-at-storage", url = "http://localhost:8072")
public interface StorageFeign {

    /**
     * 扣减库存
     * @param productId 商品ID
     * @param number 待扣减数量
     * @return Double 计算出的总价
     * @throws Exception 失败时抛出异常
     */
    @PostMapping("/storage/reduceStock")
    Double reduceStock(@RequestParam("productId") Long productId, @RequestParam("number") Integer number) throws Exception;

}
