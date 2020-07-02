package cn.kunter.seata.saga.account.service;

/**
 * Account Service
 * @author nature
 * @version 1.0 2020/6/29
 */
public interface AccountService {

    /**
     * 扣减余额
     * @param businessKey 业务Key
     * @param userId 用户ID
     * @param amount 待扣减金额
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    Boolean reduceBalance(String businessKey, Long userId, Double amount) throws Exception;

    /**
     * 补偿余额
     * @param businessKey 业务Key
     * @param userId 用户ID
     * @param amount 待补偿金额
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    Boolean compensateBalance(String businessKey, Long userId, Double amount) throws Exception;

}
