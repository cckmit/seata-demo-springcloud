package cn.kunter.seata.tcc.order.controller;

import cn.kunter.seata.tcc.account.api.AccountFeign;
import cn.kunter.seata.tcc.order.service.OrderService;
import cn.kunter.seata.tcc.storage.api.StorageFeign;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Order Controller
 * @author nature
 * @version 1.0 2020/7/1
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StorageFeign storageFeign;
    @Autowired
    private AccountFeign accountFeign;

    @GlobalTransactional
    @RequestMapping("/place")
    public String placeOrder(@RequestParam("userId") Long userId, @RequestParam("productId") Long productId,
                             @RequestParam("number") Integer number) throws Exception {

        log.info("收到下单请求, 用户: {}, 商品: {}, 数量: {}", userId, productId, number);
        // 获取到商品总价
        double totalPrice = storageFeign.getTotalPrice(productId, number);
        log.info("获取到商品: {}, 数量: {}, 总价格: {}", productId, number, totalPrice);

        // 采用当前时间戳作为订单ID和businessKey
        Long orderId = System.currentTimeMillis();

        // 创建订单
        boolean orderResult = orderService.placeOrder(orderId, userId, productId, number);
        log.info("创建订单: {}, 响应结果: {}", orderId, orderResult ? "成功" : "失败");
        // 响应结果为false，抛出异常，回滚事务
        if (!orderResult) {
            throw new Exception("创建订单: " + orderId + "失败");
        }

        // 扣减库存
        boolean stockResult = storageFeign.reduceStock(orderId, productId, number);
        log.info("订单: {}, 扣减库存, 响应结果: {}", orderId, stockResult ? "成功" : "失败");
        // 响应结果为false，抛出异常，回滚事务
        if (!stockResult) {
            throw new Exception("订单: " + orderId + ", 扣减库存失败");
        }

        // 扣减余额
        boolean accountResult = accountFeign.reduceBalance(orderId, userId, totalPrice);
        log.info("订单: {}, 扣减余额, 响应结果: {}", orderId, accountResult ? "成功" : "失败");
        // 响应结果为false，抛出异常，回滚事务
        if (!accountResult) {
            throw new Exception("订单: " + orderId + ", 扣减余额失败");
        }

        return orderResult ? "下单成功" : "下单失败";
    }

}
