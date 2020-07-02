package cn.kunter.seata.at.account.service;

/**
 * Account Service
 * @author nature
 * @version 1.0 2020/6/24
 */
public interface AccountService {

    /**
     * 扣减余额
     * @param userId 用户ID
     * @param amount 待扣减金额
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    Boolean reduceBalance(Long userId, Double amount) throws Exception;

}
