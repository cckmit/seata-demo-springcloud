package cn.kunter.seata.tcc.storage.feign;

import cn.kunter.seata.tcc.storage.api.StorageFeign;
import cn.kunter.seata.tcc.storage.service.StorageService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

/**
 * Storage Feign Impl
 * @author nature
 * @version 1.0 2020/7/1
 */
@Slf4j
@RestController
public class StorageFeignImpl implements StorageFeign {

    @Autowired
    private StorageService storageService;

    /**
     * 预扣减库存
     * 此处利用数据库的唯一约束来防止服务重复调用，对于DuplicateKeyException需要正常返回成功
     * @param productId 商品ID
     * @param number 待扣减数量
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Override
    public Boolean reduceStock(Long businessKey, Long productId, Integer number) throws Exception {

        log.info("当前XID: {}, prepare", RootContext.getXID());
        try {
            return storageService.reduceStock(businessKey, productId, number);
        } catch (DuplicateKeyException exception) {
            return true;
        }
    }

    /**
     * 根据商品ID和数量获取总价
     * @param productId 商品ID
     * @param number 商品数量
     * @return Double 计算出的总价
     */
    @Override
    public Double getTotalPrice(Long productId, Integer number) {
        return storageService.getTotalPrice(productId, number);
    }

}
