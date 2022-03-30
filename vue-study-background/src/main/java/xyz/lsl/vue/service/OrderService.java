package xyz.lsl.vue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.lsl.vue.entity.Order;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author YIQU
 * @since 2022-03-29 21:25:46
 */
public interface OrderService extends IService<Order> {

    List<Order> getOrders(String query);
}
