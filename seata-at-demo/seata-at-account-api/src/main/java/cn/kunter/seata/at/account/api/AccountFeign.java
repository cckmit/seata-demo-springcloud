package cn.kunter.seata.at.account.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Account Feign API
 * @author nature
 * @version 1.0 2020/6/24
 */
@FeignClient(name = "seata-at-account", url = "http://localhost:8071")
public interface AccountFeign {

    /**
     * 扣减余额
     * @param userId 用户ID
     * @param amount 待扣减金额
     * @return boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @PostMapping("/account/reduceBalance")
    boolean reduceBalance(@RequestParam("userId") Long userId, @RequestParam("amount") Double amount) throws Exception;

}
