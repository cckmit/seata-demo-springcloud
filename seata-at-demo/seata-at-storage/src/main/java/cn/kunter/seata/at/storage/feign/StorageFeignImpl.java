package cn.kunter.seata.at.storage.feign;

import cn.kunter.seata.at.storage.api.StorageFeign;
import cn.kunter.seata.at.storage.service.StorageService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

/**
 * Storage Feign Impl
 * @author nature
 * @version 1.0 2020/6/28
 */
@Slf4j
@RestController
public class StorageFeignImpl implements StorageFeign {

    @Autowired
    private StorageService storageService;

    /**
     * 扣减库存
     * 重点：事务传播特性设置为 REQUIRES_NEW 开启新的事务
     * @param productId 商品ID
     * @param number 待扣减数量
     * @return boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public Double reduceStock(Long productId, Integer number) throws Exception {

        log.info("当前XID: {}", RootContext.getXID());
        return storageService.reduceStock(productId, number);
    }

}
