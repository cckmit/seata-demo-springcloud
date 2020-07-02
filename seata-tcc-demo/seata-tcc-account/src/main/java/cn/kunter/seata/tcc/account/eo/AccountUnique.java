package cn.kunter.seata.tcc.account.eo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Account Unique EO
 * @author nature
 * @version 1.0 2020/7/1
 */
@Data
@TableName(value = "account_unique")
public class AccountUnique implements Serializable {

    @TableId
    private String uniqueKey;

}
