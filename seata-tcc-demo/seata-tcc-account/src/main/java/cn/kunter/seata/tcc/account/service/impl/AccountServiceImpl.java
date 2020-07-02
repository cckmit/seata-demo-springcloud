package cn.kunter.seata.tcc.account.service.impl;

import cn.kunter.seata.tcc.account.dao.AccountDao;
import cn.kunter.seata.tcc.account.dao.AccountUniqueDao;
import cn.kunter.seata.tcc.account.eo.Account;
import cn.kunter.seata.tcc.account.eo.AccountUnique;
import cn.kunter.seata.tcc.account.service.AccountService;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Account Service Impl
 * @author nature
 * @version 1.0 2020/7/1
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private static final String PREPARE_KEY = "_prepare";
    private static final String COMMIT_KEY = "_commit";
    private static final String ROLLBACK_KEY = "_rollback";

    @Resource
    private AccountDao accountDao;
    @Resource
    private AccountUniqueDao accountUniqueDao;

    /**
     * 预扣减余额
     * 利用业务约束表防止服务重复调用
     * @param businessKey 业务ID
     * @param userId 用户ID
     * @param amount 待扣减金额
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean reduceBalance(Long businessKey, Long userId, Double amount) throws Exception {

        // 检查余额
        checkBalance(userId, amount);

        // 预扣减余额：事务中余额 + 本次待扣减余额
        log.info("开始扣减用户 {} 余额", userId);
        Account account = accountDao.selectById(userId);

        account.setTransBalance(account.getTransBalance() + amount);
        Integer record = accountDao.updateById(account);
        log.info("预扣减用户 {} 余额结果: {}", userId, record > 0 ? "成功" : "失败");

        // 设置prepare阶段业务处理标识
        AccountUnique accountUnique = new AccountUnique();
        accountUnique.setUniqueKey(businessKey + PREPARE_KEY);
        accountUniqueDao.insert(accountUnique);

        return record > 0;
    }

    /**
     * 扣减余额
     * 利用业务约束表防止服务重复调用
     * 提交之前，先查询prepare阶段是否执行，如若未执行则抛出异常
     * @param actionContext BusinessActionContext
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean commitBalance(BusinessActionContext actionContext) throws Exception {

        Long businessKey = Long.valueOf(actionContext.getActionContext("businessKey").toString());
        Long userId = Long.valueOf(actionContext.getActionContext("userId").toString());
        Double amount = Double.valueOf(actionContext.getActionContext("amount").toString());

        // 查询prepare阶段业务处理标识
        AccountUnique accountUnique = accountUniqueDao.selectById(businessKey + PREPARE_KEY);
        // 如果AccountUnique为null，则表示prepare阶段还未执行
        if (accountUnique == null) {
            throw new Exception("业务: " + businessKey + " 的prepare阶段还未执行");
        }

        // 查询commit阶段业务处理标识
        accountUnique = accountUniqueDao.selectById(businessKey + COMMIT_KEY);
        // 如果AccountUnique不为null，则表示commit阶段已执行
        if (accountUnique != null) {
            return true;
        }

        // 开始扣减用户余额，此处需要对余额和事务中余额都进行扣减
        log.info("开始扣减用户 {} 余额", userId);
        Account account = accountDao.selectById(userId);
        account.setBalance(account.getBalance() - amount);
        account.setTransBalance(account.getTransBalance() - amount);
        Integer record = accountDao.updateById(account);
        log.info("扣减用户 {} 余额结果: {}", userId, record > 0 ? "成功" : "失败");

        // 设置commit阶段业务处理标识
        accountUnique = new AccountUnique();
        accountUnique.setUniqueKey(businessKey + COMMIT_KEY);
        accountUniqueDao.insert(accountUnique);

        return record > 0;
    }

    /**
     * 回滚余额
     * 利用业务约束表防止服务重复调用
     * 回滚之前，先查询prepare阶段是否执行，如若未执行则抛出异常
     * @param actionContext BusinessActionContext
     * @return Boolean 操作结果
     * @throws Exception 失败时抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean rollbackBalance(BusinessActionContext actionContext) throws Exception {

        Long businessKey = Long.valueOf(actionContext.getActionContext("businessKey").toString());
        Long userId = Long.valueOf(actionContext.getActionContext("userId").toString());
        Double amount = Double.valueOf(actionContext.getActionContext("amount").toString());

        // 查询prepare阶段业务处理标识
        AccountUnique accountUnique = accountUniqueDao.selectById(businessKey + PREPARE_KEY);
        // 如果AccountUnique为null，则表示prepare阶段还未执行
        if (accountUnique == null) {
            return true;
        }

        // 查询rollback阶段业务处理标识
        accountUnique = accountUniqueDao.selectById(businessKey + ROLLBACK_KEY);
        // 如果AccountUnique不为null，则表示rollback阶段已执行
        if (accountUnique != null) {
            return true;
        }

        // 开始回滚用户余额，只需要将事务中余额扣减
        log.info("开始回滚用户 {} 余额", userId);
        Account account = accountDao.selectById(userId);
        account.setTransBalance(account.getTransBalance() - amount);
        Integer record = accountDao.updateById(account);
        log.info("回滚用户 {} 余额结果: {}", userId, record > 0 ? "成功" : "失败");

        // 设置rollback阶段业务处理标识
        accountUnique = new AccountUnique();
        accountUnique.setUniqueKey(businessKey + ROLLBACK_KEY);
        accountUniqueDao.insert(accountUnique);

        return record > 0;
    }

    /**
     * 检查余额
     * @param userId 用户ID
     * @param amount 待扣减金额
     * @throws Exception 余额不足时抛出异常
     */
    private void checkBalance(Long userId, Double amount) throws Exception {

        log.info("检查用户 {} 可用余额", userId);
        Account account = accountDao.selectById(userId);

        // 余额
        Double balance = account.getBalance();
        // 事务中余额（待扣减）
        Double transBalance = account.getTransBalance();
        // 可用余额（余额-事务中余额）小于待扣减金额
        if ((balance - transBalance) < amount) {
            log.warn("用户 {} 可用余额不足, 当前余额: {}, 事务中余额: {}, 订单金额: {}", userId, balance, transBalance, amount);
            throw new Exception("用户 " + userId + " 可用余额不足, 当前余额: " + balance + ", 事务中余额: " + transBalance + ", 订单金额: " + amount);
        }
    }

}
