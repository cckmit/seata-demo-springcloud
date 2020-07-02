package cn.kunter.seata.at.account.service.impl;

import cn.kunter.seata.at.account.dao.AccountDao;
import cn.kunter.seata.at.account.eo.Account;
import cn.kunter.seata.at.account.service.AccountService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Account Service Impl
 * @author nature
 * @version 1.0 2020/6/28
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountDao accountDao;

    @Override
    public Boolean reduceBalance(Long userId, Double amount) throws Exception {

        log.info("当前XID: {}", RootContext.getXID());
        // 检查余额
        checkBalance(userId, amount);

        // 扣减余额
        log.info("开始扣减用户 {} 余额", userId);
        Account account = accountDao.selectById(userId);

        account.setBalance(account.getBalance() - amount);
        Integer record = accountDao.updateById(account);
        log.info("扣减用户 {} 余额结果: 扣减余额{}", userId, record > 0 ? "成功" : "失败");

        return record > 0;
    }

    /**
     * 检查余额
     * @param userId 用户ID
     * @param amount 待扣减金额
     * @throws Exception 余额不足时抛出异常
     */
    private void checkBalance(Long userId, Double amount) throws Exception {

        log.info("检查用户 {} 余额", userId);
        Account account = accountDao.selectById(userId);

        Double balance = account.getBalance();
        if (balance < amount) {
            log.warn("用户 {} 余额不足, 当前余额: {}", userId, balance);
            throw new Exception("余额不足");
        }
    }

}
