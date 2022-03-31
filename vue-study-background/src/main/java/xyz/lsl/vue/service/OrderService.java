package xyz.lsl.vue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public interface OrderService extends IService<Order> {

    List<Order> getOrders(String query);
}
