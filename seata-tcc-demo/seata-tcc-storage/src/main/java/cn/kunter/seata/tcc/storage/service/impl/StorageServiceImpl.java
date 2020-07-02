package cn.kunter.seata.tcc.storage.service.impl;

import cn.kunter.seata.tcc.storage.dao.ProductDao;
import cn.kunter.seata.tcc.storage.dao.ProductUniqueDao;
import cn.kunter.seata.tcc.storage.eo.Product;
import cn.kunter.seata.tcc.storage.eo.ProductUnique;
import cn.kunter.seata.tcc.storage.service.StorageService;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Storage Service Impl
 * @author nature
 * @version 1.0 2020/7/1
 */
@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

    private static final String PREPARE_KEY = "_prepare";
    private static final String COMMIT_KEY = "_commit";
    private static final String ROLLBACK_KEY = "_rollback";

    @Resource
    private ProductDao productDao;
    @Resource
    private ProductUniqueDao productUniqueDao;

    /**
     * 预扣减库存
     * 利用业务约束表防止服务重复调用
     * @param businessKey 业务ID
     * @param productId 商品ID
     * @param number 待扣减数量
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean reduceStock(Long businessKey, Long productId, Integer number) throws Exception {

        // 检查库存
        checkStock(productId, number);

        // 预扣减库存：事务中库存 + 本次待扣减数量
        log.info("开始扣减产品 {} 库存", productId);
        Product product = productDao.selectById(productId);

        product.setTransStock(product.getTransStock() + number);
        int record = productDao.updateById(product);
        log.info("预扣减产品 {} 库存结果: {}", productId, record > 0 ? "成功" : "失败");

        // 设置prepare阶段业务处理标识
        ProductUnique productUnique = new ProductUnique();
        productUnique.setUniqueKey(businessKey + PREPARE_KEY);
        productUniqueDao.insert(productUnique);

        return record > 0;
    }

    /**
     * 扣减库存
     * 利用业务约束表防止服务重复调用
     * 提交之前，先查询prepare阶段是否执行，如若未执行则抛出异常
     * @param actionContext BusinessActionContext
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean commitStock(BusinessActionContext actionContext) throws Exception {

        Long businessKey = Long.valueOf(actionContext.getActionContext("businessKey").toString());
        Long productId = Long.valueOf(actionContext.getActionContext("productId").toString());
        Integer number = Integer.valueOf(actionContext.getActionContext("number").toString());

        // 查询prepare阶段业务处理标识
        ProductUnique productUnique = productUniqueDao.selectById(businessKey + PREPARE_KEY);
        // 如果ProductUnique为null，则表示prepare阶段还未执行
        if (productUnique == null) {
            throw new Exception("业务: " + businessKey + " 的prepare阶段还未执行");
        }

        // 查询commit阶段业务处理标识
        productUnique = productUniqueDao.selectById(businessKey + COMMIT_KEY);
        // 如果ProductUnique不为null，则表示commit阶段已执行
        if (productUnique != null) {
            return true;
        }

        // 开始扣减产品库存，此处需要对库存和事务中库存都进行扣减
        log.info("开始扣减产品 {} 库存", productId);
        Product product = productDao.selectById(productId);
        product.setStock(product.getStock() - number);
        product.setTransStock(product.getTransStock() - number);
        Integer record = productDao.updateById(product);
        log.info("扣减产品 {} 库存结果: {}", productId, record > 0 ? "成功" : "失败");

        // 设置commit阶段业务处理标识
        productUnique = new ProductUnique();
        productUnique.setUniqueKey(businessKey + COMMIT_KEY);
        productUniqueDao.insert(productUnique);

        return record > 0;
    }

    /**
     * 回滚库存
     * 利用业务约束表防止服务重复调用
     * 回滚之前，先查询prepare阶段是否执行，如若未执行则抛出异常
     * @param actionContext BusinessActionContext
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean rollbackStock(BusinessActionContext actionContext) throws Exception {

        Long businessKey = Long.valueOf(actionContext.getActionContext("businessKey").toString());
        Long productId = Long.valueOf(actionContext.getActionContext("productId").toString());
        Integer number = Integer.valueOf(actionContext.getActionContext("number").toString());

        // 查询prepare阶段业务处理标识
        ProductUnique productUnique = productUniqueDao.selectById(businessKey + PREPARE_KEY);
        // 如果ProductUnique为null，则表示prepare阶段还未执行
        if (productUnique == null) {
            return true;
        }

        // 查询rollback阶段业务处理标识
        productUnique = productUniqueDao.selectById(businessKey + ROLLBACK_KEY);
        // 如果ProductUnique不为null，则表示rollback阶段已执行
        if (productUnique != null) {
            return true;
        }

        // 开始回滚产品库存，只需要将事务中库存扣减
        log.info("开始回滚产品 {} 库存", productId);
        Product product = productDao.selectById(productId);
        product.setTransStock(product.getTransStock() - number);
        Integer record = productDao.updateById(product);
        log.info("回滚产品 {} 库存结果: {}", productId, record > 0 ? "成功" : "失败");

        // 设置commit阶段业务处理标识
        productUnique = new ProductUnique();
        productUnique.setUniqueKey(businessKey + ROLLBACK_KEY);
        productUniqueDao.insert(productUnique);

        return record > 0;
    }

    /**
     * 根据商品ID和数量获取总价
     * @param productId 商品ID
     * @param number 商品数量
     * @return Double 计算出的总价
     */
    @Override
    public Double getTotalPrice(Long productId, Integer number) {

        Product product = productDao.selectById(productId);
        double totalPrice = product.getPrice() * number;
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
            throw new Exception("商品 " + productId + " 库存不足, 当前库存: " + stock + ", 订单数量: " + number);
        }
    }

}
