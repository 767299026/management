package xyz.lsl.vue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.lsl.vue.entity.Order;
import xyz.lsl.vue.mapper.OrderMapper;
import xyz.lsl.vue.service.OrderService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author YIQU
 * @since 2022-03-29 21:25:46
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public List<Order> getOrders(String query) {
        return orderMapper.getOrdersByOderNumber(query);
    }
}
