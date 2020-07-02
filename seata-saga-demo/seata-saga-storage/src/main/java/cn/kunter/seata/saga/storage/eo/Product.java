package cn.kunter.seata.saga.storage.eo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Product EO
 * @author nature
 * @version 1.0 2020/6/29
 */
@Data
@TableName(value = "product")
public class Product implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Double price;
    private Integer stock;
    private Date lastUpdateTime;

}
