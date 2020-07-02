package cn.kunter.seata.at.storage.service.impl;

import cn.kunter.seata.at.storage.dao.ProductDao;
import cn.kunter.seata.at.storage.eo.Product;
import cn.kunter.seata.at.storage.service.StorageService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Storage Service Impl
 * @author nature
 * @version 1.0 2020/6/28
 */
@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

    @Resource
    private ProductDao productDao;

    @Override
    public Double reduceStock(Long productId, Integer number) throws Exception {

        log.info("当前XID: {}", RootContext.getXID());
        // 检查库存
        checkStock(productId, number);

        // 扣减库存
        log.info("开始扣减产品 {} 库存", productId);
        Product product = productDao.selectById(productId);

        product.setStock(product.getStock() - number);
        productDao.updateById(product);
        Double totalPrice = product.getPrice() * number;
        log.info("扣减产品 {} 库存成功, 订单总价为: {}", productId, totalPrice);

        return totalPrice;
    }

    /**
     * 检查库存
     * @param productId 商品ID
     * @param number 待扣减数量
     * @throws Exception 库存不足时抛出异常
     */
    private void checkStock(Long productId, Integer number) throws Exception {

        log.info("检查商品 {} 库存", productId);
        Product product = productDao.selectById(productId);

        Integer stock = product.getStock();
        if (stock < number) {
            log.warn("商品 {} 库存不足, 当前库存: {}, 订单数量: {}", productId, stock, number);
            throw new Exception("库存不足");
        }
    }

}
