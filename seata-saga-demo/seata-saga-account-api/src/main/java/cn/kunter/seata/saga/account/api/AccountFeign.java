package cn.kunter.seata.saga.account.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Account Feign API
 * @author nature
 * @version 1.0 2020/6/29
 */
@FeignClient(name = "seata-saga-account", url = "http://localhost:8081")
public interface AccountFeign {

    /**
     * 扣减余额
     * @param businessKey 业务Key
     * @param userId 用户ID
     * @param amount 待扣减金额
     * @return Boolean 操作结果
     */
    @PostMapping("/account/reduceBalance")
    Boolean reduceBalance(@RequestParam("businessKey") String businessKey, @RequestParam("userId") Long userId, @RequestParam("amount") Double amount) throws Exception;

    /**
     * 补偿余额
     * @param businessKey 业务Key
     * @param userId 用户ID
     * @param amount 待补偿金额
     * @return Boolean 操作结果
     */
    @PostMapping("/account/compensateBalance")
    Boolean compensateBalance(@RequestParam("businessKey") String businessKey, @RequestParam("userId") Long userId, @RequestParam("amount") Double amount) throws Exception;

}
