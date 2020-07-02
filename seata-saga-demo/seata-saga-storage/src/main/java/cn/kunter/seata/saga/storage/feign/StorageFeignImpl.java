package cn.kunter.seata.saga.storage.feign;

import cn.kunter.seata.saga.storage.api.StorageFeign;
import cn.kunter.seata.saga.storage.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

/**
 * Storage Feign Impl
 * @author nature
 * @version 1.0 2020/6/29
 */
@Slf4j
@RestController
public class StorageFeignImpl implements StorageFeign {

    @Autowired
    private StorageService storageService;

    /**
     * 扣减库存
     * 此处需要对DuplicateKeyException进行处理，并返回成功
     * 在Feign API层，需要对Exception进行处理，返回友好消息给调用者
     * @param businessKey 业务Key
     * @param productId 商品ID
     * @param number 待扣减数量
     * @return Double 计算出的总价
     */
    @Override
    public Boolean reduceStock(String businessKey, Long productId, Integer number) throws Exception {

        try {
            return storageService.reduceStock(businessKey, productId, number);
        } catch (DuplicateKeyException exception) {
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 补偿库存
     * 此处需要对DuplicateKeyException进行处理，并返回成功
     * 在Feign API层，需要对Exception进行处理，返回友好消息给调用者
     * @param businessKey 业务Key
     * @param productId 商品ID
     * @param number 待补偿数量
     * @return Boolean 操作结果
     */
    @Override
    public Boolean compensateStock(String businessKey, Long productId, Integer number) throws Exception {

        try {
            return storageService.compensateStock(businessKey, productId, number);
        } catch (DuplicateKeyException exception) {
            return true;
        } catch (Exception exception) {
            return false;
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
