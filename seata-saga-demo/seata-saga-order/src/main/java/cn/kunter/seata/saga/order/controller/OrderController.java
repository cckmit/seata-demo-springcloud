package cn.kunter.seata.saga.order.controller;

import cn.kunter.seata.saga.storage.api.StorageFeign;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.statelang.domain.StateMachineInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Order Controller
 * @author nature
 * @version 1.0 2020/6/29
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private StorageFeign storageFeign;
    @Autowired
    private StateMachineEngine stateMachineEngine;

    @RequestMapping("/place")
    public String placeOrder(@RequestParam("userId") Long userId, @RequestParam("productId") Long productId,
                             @RequestParam("number") Integer number) {

        log.info("收到下单请求, 用户: {}, 商品: {}, 数量: {}", userId, productId, number);

        // 根据商品ID和数量，获取总价格
        Double totalPrice = storageFeign.getTotalPrice(productId, number);
        log.info("获取到商品: {} 的总价格为: {}", productId, totalPrice);

        String businessKey = String.valueOf(System.currentTimeMillis());
        Map<String, Object> startParams = new HashMap<>();
        startParams.put("businessKey", businessKey);
        startParams.put("userId", userId);
        startParams.put("productId", productId);
        startParams.put("number", number);
        startParams.put("amount", totalPrice);

        StateMachineInstance instance = stateMachineEngine
                .startWithBusinessKey("PlaceOrder", null, businessKey, startParams);
        log.info("下单完成, 响应结果: {}", instance.getStatus().getStatusString());

        return instance.getStatus().getStatusString();
    }

}
