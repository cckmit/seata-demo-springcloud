package cn.kunter.seata.tcc.account.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * Account Service
 * @author nature
 * @version 1.0 2020/7/1
 */
@LocalTCC
public interface AccountService {

    /**
     * 预扣减余额
     * @param businessKey 业务ID
     * @param userId 用户ID
     * @param amount 待扣减金额
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @TwoPhaseBusinessAction(name = "AccountService", commitMethod = "commitBalance", rollbackMethod = "rollbackBalance")
    Boolean reduceBalance(@BusinessActionContextParameter(paramName = "businessKey") Long businessKey,
                          @BusinessActionContextParameter(paramName = "userId") Long userId,
                          @BusinessActionContextParameter(paramName = "amount") Double amount) throws Exception;

    /**
     * 扣减余额
     * @param actionContext BusinessActionContext
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    Boolean commitBalance(BusinessActionContext actionContext) throws Exception;

    /**
     * 回滚余额
     * @param actionContext BusinessActionContext
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    Boolean rollbackBalance(BusinessActionContext actionContext) throws Exception;

}
