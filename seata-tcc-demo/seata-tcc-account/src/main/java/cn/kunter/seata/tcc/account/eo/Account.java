package cn.kunter.seata.tcc.account.eo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Account EO
 * @author nature
 * @version 1.0 2020/7/1
 */
@Data
@TableName(value = "account")
public class Account implements Serializable {

    @TableId
    private Long id;
    private Double balance;
    private Double transBalance;
    private Date lastUpdateTime;

}
