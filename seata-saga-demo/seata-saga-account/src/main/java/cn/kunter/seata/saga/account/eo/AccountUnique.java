package cn.kunter.seata.saga.account.eo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Account Unique EO
 * @author nature
 * @version 1.0 2020/6/29
 */
@Data
@TableName(value = "account_unique")
public class AccountUnique implements Serializable {

    @TableId
    private String uniqueKey;

}
