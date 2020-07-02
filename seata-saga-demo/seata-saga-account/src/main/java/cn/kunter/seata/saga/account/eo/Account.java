package cn.kunter.seata.saga.account.eo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Account EO
 * @author nature
 * @version 1.0 2020/6/29
 */
@Data
@TableName(value = "account")
public class Account implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Double balance;
    private Date lastUpdateTime;

}
