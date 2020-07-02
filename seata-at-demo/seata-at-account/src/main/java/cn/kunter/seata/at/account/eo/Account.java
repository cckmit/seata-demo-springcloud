package cn.kunter.seata.at.account.eo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Account EO
 * @author nature
 * @version 1.0 2020/6/24
 */
@Data
@TableName(value = "account")
public class Account implements Serializable {

    private Long id;
    private Double balance;
    private Date lastUpdateTime;

}
