package cn.kunter.seata.saga.account.service.impl;

import cn.kunter.seata.saga.account.dao.AccountDao;
import cn.kunter.seata.saga.account.dao.AccountUniqueDao;
import cn.kunter.seata.saga.account.eo.Account;
import cn.kunter.seata.saga.account.eo.AccountUnique;
import cn.kunter.seata.saga.account.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Account Service Impl
 * @author nature
 * @version 1.0 2020/6/29
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    /** 扣减唯一约束键后缀 */
    private static final String UNIQUE_KEY_REDUCE = "_reduce";
    /** 补偿唯一约束键后缀 */
    private static final String UNIQUE_KEY_COMPENSATE = "_compensate";

    @Resource
    private AccountDao accountDao;
    @Resource
    private AccountUniqueDao accountUniqueDao;

    /**
     * 扣减余额
     * 接口幂等性设计：利用数据库的主键或唯一约束来防止重复扣款的情况发生，利用DuplicateKeyException来回滚对账户的操作，
     * 外部Feign实现需要对DuplicateKeyException进行catch处理，返回成功的结果，如有需要可以进行消息提示重复请求。避免直接向外抛出这个异常造成全局事务回滚
     * 如果同一个分布式事务中需要多次对同一个账号进行加减款操作，需要对多次操作的值进行汇总只将最终结果存储到数据库，保证一个事务中对同一条记录只操作一次。
     * 唯一约束的组成：订单ID（businessKey）+账户ID+（扣减|补偿）标记
     * @param businessKey 业务Key
     * @param userId 用户ID
     * @param amount 待扣减金额
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean reduceBalance(String businessKey, Long userId, Double amount) throws Exception {

        log.info("订单 {} 请求扣减用户 {} 余额 {}", businessKey, userId, amount);
        // 检查余额
        checkBalance(userId, amount);

        // 扣减余额
        log.info("开始扣减用户 {} 余额", userId);
        Account account = accountDao.selectById(userId);

        account.setBalance(account.getBalance() - amount);
        Integer record = accountDao.updateById(account);
        log.info("扣减用户 {} 余额结果: {}", userId, record > 0 ? "成功" : "失败");

        // 设置扣减履历，用于防重复扣款
        AccountUnique accountUnique = new AccountUnique();
        accountUnique.setUniqueKey(businessKey + "_" + userId + UNIQUE_KEY_REDUCE);
        accountUniqueDao.insert(accountUnique);

        return record > 0;
    }

    /**
     * 补偿余额
     * 接口幂等与余额扣减方法一致
     * 空回滚处理：通过查询余额扣减的唯一Key来判定有没做过余额扣减操作，如果KEY不存在则无需进行补偿，此处不能抛出异常或者返回处理失败。
     * @param businessKey 业务Key
     * @param userId 用户ID
     * @param amount 待补偿金额
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean compensateBalance(String businessKey, Long userId, Double amount) throws Exception {

        log.info("订单 {} 请求补偿用户 {} 余额 {}", businessKey, userId, amount);

        // 查询是否已经进行了余额扣减操作
        String reduceKey = businessKey + "_" + userId + UNIQUE_KEY_REDUCE;
        AccountUnique accountUnique = accountUniqueDao.selectById(reduceKey);
        // AccountUnique为空，则表示未进行余额扣减操作，直接返回成功
        if (accountUnique == null) {
            return true;
        }

        // 补偿余额
        log.info("开始补偿用户 {} 余额", userId);
        Account account = accountDao.selectById(userId);

        account.setBalance(account.getBalance() + amount);
        Integer record = accountDao.updateById(account);
        log.info("补偿用户 {} 余额结果: {}", userId, record > 0 ? "成功" : "失败");

        // 设置补偿履历，用于防重复补偿
        accountUnique = new AccountUnique();
        accountUnique.setUniqueKey(businessKey + "_" + userId + UNIQUE_KEY_COMPENSATE);
        accountUniqueDao.insert(accountUnique);

        return record > 0;
    }

    /**
     * 检查余额
     * @param userId 用户ID
     * @param amount 待扣减金额
     */
    private void checkBalance(Long userId, Double amount) throws Exception {

        log.info("检查用户 {} 余额", userId);
        Account account = accountDao.selectById(userId);

        Double balance = account.getBalance();
        if (balance < amount) {
            log.warn("用户 {} 余额不足, 当前余额: {}", userId, balance);
            throw new Exception("用户 " + userId + " 余额不足, 当前余额: " + balance);
        }
    }

}
