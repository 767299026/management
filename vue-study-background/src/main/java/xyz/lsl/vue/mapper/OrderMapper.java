package xyz.lsl.vue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xyz.lsl.vue.entity.Order;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author YIQU
 * @since 2022-03-29 21:25:46
 */
public interface OrderMapper extends BaseMapper<Order> {

    List<Order> getOrdersByOderNumber(@Param("orderNumber") String orderNumber);
}
