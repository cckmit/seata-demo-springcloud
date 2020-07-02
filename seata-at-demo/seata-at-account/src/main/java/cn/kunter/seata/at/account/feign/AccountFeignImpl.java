package cn.kunter.seata.at.account.feign;

import cn.kunter.seata.at.account.api.AccountFeign;
import cn.kunter.seata.at.account.service.AccountService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

/**
 * Account Feign Impl
 * @author nature
 * @version 1.0 2020/6/24
 */
@Slf4j
@RestController
public class AccountFeignImpl implements AccountFeign {

    @Autowired
    private AccountService accountService;

    /**
     * 扣减余额
     * 重点：事务传播特性设置为 REQUIRES_NEW 开启新的事务
     * @param userId 用户ID
     * @param amount 待扣减金额
     * @return boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public boolean reduceBalance(Long userId, Double amount) throws Exception {
        log.info("当前XID: {}", RootContext.getXID());
        return accountService.reduceBalance(userId, amount);
    }

}
