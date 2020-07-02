package cn.kunter.seata.tcc.storage.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * Storage Service
 * @author nature
 * @version 1.0 2020/7/1
 */
@LocalTCC
public interface StorageService {

    /**
     * 预扣减库存
     * @param businessKey 业务ID
     * @param productId 商品ID
     * @param number 待扣减数量
     * @return Boolean 计算出的总价
     * @throws Exception 失败时抛出异常
     */
    @TwoPhaseBusinessAction(name = "StorageService", commitMethod = "commitStock", rollbackMethod = "rollbackStock")
    Boolean reduceStock(@BusinessActionContextParameter(paramName = "businessKey") Long businessKey,
                        @BusinessActionContextParameter(paramName = "productId") Long productId,
                        @BusinessActionContextParameter(paramName = "number") Integer number) throws Exception;

    /**
     * 扣减库存
     * @param actionContext BusinessActionContext
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    Boolean commitStock(BusinessActionContext actionContext) throws Exception;

    /**
     * 回滚库存
     * @param actionContext BusinessActionContext
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    Boolean rollbackStock(BusinessActionContext actionContext) throws Exception;

    /**
     * 根据商品ID和数量获取总价
     * @param productId 商品ID
     * @param number 商品数量
     * @return Double 计算出的总价
     */
    Double getTotalPrice(Long productId, Integer number);

}
