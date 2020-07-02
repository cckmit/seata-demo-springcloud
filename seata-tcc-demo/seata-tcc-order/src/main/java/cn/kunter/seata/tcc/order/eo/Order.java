package cn.kunter.seata.tcc.order.eo;

import cn.kunter.seata.tcc.order.enums.OrderStatus;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Order EO
 * @author nature
 * @version 1.0 2020/7/1
 */
@Data
@Builder
@TableName(value = "orders")
public class Order implements Serializable {

    @TableId
    private Long id;
    private Long userId;
    private Long productId;
    private OrderStatus status;
    private Integer number;

}
