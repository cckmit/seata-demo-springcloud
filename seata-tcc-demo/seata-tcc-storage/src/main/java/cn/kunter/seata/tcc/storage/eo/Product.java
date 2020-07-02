package cn.kunter.seata.tcc.storage.eo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Product EO
 * @author nature
 * @version 1.0 2020/7/1
 */
@Data
@TableName(value = "product")
public class Product implements Serializable {

    @TableId
    private Long id;
    private Double price;
    private Integer stock;
    private Integer transStock;
    private Date lastUpdateTime;

}
