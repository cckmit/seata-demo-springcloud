package cn.kunter.seata.at.order.controller;

import cn.kunter.seata.at.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Order Controller
 * @author nature
 * @version 1.0 2020/6/28
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/place")
    public String placeOrder(@RequestParam("userId") Long userId, @RequestParam("productId") Long productId,
                             @RequestParam("number") Integer number) throws Exception {

        log.info("收到下单请求, 用户: {}, 商品: {}, 数量: {}", userId, productId, number);
        Boolean result = orderService.placeOrder(userId, productId, number);
        log.info("下单完成, 响应结果: {}", result ? "下单成功" : "下单失败");

        return result ? "下单成功" : "下单失败";
    }

}
