package cn.kunter.seata.tcc.account.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Account Feign API
 * @author nature
 * @version 1.0 2020/7/1
 */
@FeignClient(name = "seata-tcc-account", url = "http://localhost:8061")
public interface AccountFeign {

    /**
     * 扣减余额
     * @param businessKey 业务ID
     * @param userId 用户ID
     * @param amount 待扣减金额
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @PostMapping("/account/reduceBalance")
    Boolean reduceBalance(@RequestParam("businessKey") Long businessKey, @RequestParam("userId") Long userId,
                          @RequestParam("amount") Double amount) throws Exception;

}
