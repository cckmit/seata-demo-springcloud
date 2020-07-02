package cn.kunter.seata.tcc.account.feign;

import cn.kunter.seata.tcc.account.api.AccountFeign;
import cn.kunter.seata.tcc.account.service.AccountService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

/**
 * Account Feign Impl
 * @author nature
 * @version 1.0 2020/7/1
 */
@Slf4j
@RestController
public class AccountFeignImpl implements AccountFeign {

    @Autowired
    private AccountService accountService;

    /**
     * 预扣减余额
     * 此处利用数据库的唯一约束来防止服务重复调用，对于DuplicateKeyException需要正常返回成功
     * @param businessKey 业务ID
     * @param userId 用户ID
     * @param amount 待扣减金额
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Override
    public Boolean reduceBalance(Long businessKey, Long userId, Double amount) throws Exception {

        log.info("当前XID: {}, prepare", RootContext.getXID());
        try {
            return accountService.reduceBalance(businessKey, userId, amount);
        } catch (DuplicateKeyException exception) {
            return true;
        }
    }

}
