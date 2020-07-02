package cn.kunter.seata.saga.account.feign;

import cn.kunter.seata.saga.account.api.AccountFeign;
import cn.kunter.seata.saga.account.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

/**
 * Account Feign Impl
 * @author nature
 * @version 1.0 2020/6/29
 */
@Slf4j
@RestController
public class AccountFeignImpl implements AccountFeign {

    @Autowired
    private AccountService accountService;

    /**
     * 扣减余额
     * 此处需要对DuplicateKeyException进行处理，并返回成功
     * 在Feign API层，需要对Exception进行处理，返回友好消息给调用者
     * @param businessKey 业务Key
     * @param userId 用户ID
     * @param amount 待扣减金额
     * @return Boolean 操作结果
     */
    @Override
    public Boolean reduceBalance(String businessKey, Long userId, Double amount) throws Exception {

        try {
            return accountService.reduceBalance(businessKey, userId, amount);
        } catch (DuplicateKeyException exception) {
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 补偿余额
     * 此处需要对DuplicateKeyException进行处理，并返回成功
     * 在Feign API层，需要对Exception进行处理，返回友好消息给调用者
     * @param businessKey 业务Key
     * @param userId 用户ID
     * @param amount 待补偿金额
     * @return Boolean 操作结果
     */
    @Override
    public Boolean compensateBalance(String businessKey, Long userId, Double amount) throws Exception {

        try {
            return accountService.compensateBalance(businessKey, userId, amount);
        } catch (DuplicateKeyException exception) {
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

}
