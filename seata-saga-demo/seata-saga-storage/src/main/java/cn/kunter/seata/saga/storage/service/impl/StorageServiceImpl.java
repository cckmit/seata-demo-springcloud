package cn.kunter.seata.saga.storage.service.impl;

import cn.kunter.seata.saga.storage.dao.ProductDao;
import cn.kunter.seata.saga.storage.dao.ProductUniqueDao;
import cn.kunter.seata.saga.storage.eo.Product;
import cn.kunter.seata.saga.storage.eo.ProductUnique;
import cn.kunter.seata.saga.storage.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Storage Service Impl
 * @author nature
 * @version 1.0 2020/6/29
 */
@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

    /** 扣减唯一约束键后缀 */
    private static final String UNIQUE_KEY_REDUCE = "_reduce";
    /** 补偿唯一约束键后缀 */
    private static final String UNIQUE_KEY_COMPENSATE = "_compensate";

    @Resource
    private ProductDao productDao;
    @Resource
    private ProductUniqueDao productUniqueDao;

    /**
     * 扣减库存
     * 接口幂等性设计：利用数据库的主键或唯一约束来防止重复扣款的情况发生，利用DuplicateKeyException来回滚对账户的操作，
     * 外部Feign实现需要对DuplicateKeyException进行catch处理，返回成功的结果，如有需要可以进行消息提示重复请求。避免直接向外抛出这个异常造成全局事务回滚
     * 如果同一个分布式事务中需要多次对同一个账号进行加减款操作，需要对多次操作的值进行汇总只将最终结果存储到数据库，保证一个事务中对同一条记录只操作一次。
     * 唯一约束的组成：订单ID（businessKey）+账户ID+（扣减|补偿）标记
     * @param businessKey 业务Key
     * @param productId 商品ID
     * @param number 待扣减数量
     * @return Double 计算出的总价
     * @throws Exception 失败时抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean reduceStock(String businessKey, Long productId, Integer number) throws Exception {

        log.info("订单 {} 请求扣减产品 {} 库存 {}", businessKey, productId, number);
        // 检查库存
        checkStock(productId, number);

        // 扣减库存
        log.info("开始扣减产品 {} 库存", productId);
        Product product = productDao.selectById(productId);

        product.setStock(product.getStock() - number);
        Integer record = productDao.updateById(product);
        log.info("扣减产品 {} 库存结果: {}", productId, record > 0 ? "成功" : "失败");

        // 设置扣减履历，用于防重复扣减
        ProductUnique productUnique = new ProductUnique();
        productUnique.setUniqueKey(businessKey + "_" + productId + UNIQUE_KEY_REDUCE);
        productUniqueDao.insert(productUnique);

        return record > 0;
    }

    /**
     * 补偿库存
     * 接口幂等与余额扣减方法一致
     * 空回滚处理：通过查询余额扣减的唯一Key来判定有没做过余额扣减操作，如果KEY不存在则无需进行补偿，此处不能抛出异常或者返回处理失败。
     * @param businessKey 业务Key
     * @param productId 商品ID
     * @param number 待补偿数量
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean compensateStock(String businessKey, Long productId, Integer number) throws Exception {

        log.info("订单 {} 请求补偿产品 {} 库存 {}", businessKey, productId, number);

        // 查询是否已经进行了库存扣减操作
        String reduceKey = businessKey + "_" + productId + UNIQUE_KEY_REDUCE;
        ProductUnique productUnique = productUniqueDao.selectById(reduceKey);
        // ProductUnique为空，则表示未进行余额扣减操作，直接返回成功
        if (productUnique == null) {
            return true;
        }

        // 补偿库存
        log.info("开始补偿产品 {} 库存", productId);
        Product product = productDao.selectById(productId);

        product.setStock(product.getStock() + number);
        Integer record = productDao.updateById(product);
        log.info("补偿产品 {} 库存结果: {}", productId, record > 0 ? "成功" : "失败");

        // 设置补偿履历，用于防重复补偿
        productUnique = new ProductUnique();
        productUnique.setUniqueKey(businessKey + "_" + productId + UNIQUE_KEY_COMPENSATE);
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
        Double totalPrice = product.getPrice() * number;
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
