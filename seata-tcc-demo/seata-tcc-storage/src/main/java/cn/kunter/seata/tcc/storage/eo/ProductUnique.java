package cn.kunter.seata.tcc.storage.eo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Product Unique EO
 * @author nature
 * @version 1.0 2020/7/1
 */
@Data
@TableName(value = "product_unique")
public class ProductUnique implements Serializable {

    @TableId
    private String uniqueKey;

}
