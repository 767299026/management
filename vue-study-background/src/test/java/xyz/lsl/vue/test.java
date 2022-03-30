package xyz.lsl.vue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.lsl.vue.entity.User;
import xyz.lsl.vue.service.OrderService;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@SpringBootTest
public class test {

    @Resource
    private OrderService orderService;

    @Test
    void changeDate() {
        /*List<Order> list = orderService.list();
        for (Order order : list) {
            Timestamp time1 = new Timestamp(order.getCreateTime());
            LocalDateTime create = time1.toLocalDateTime();
            order.setTime1(create);
            Timestamp time2 = new Timestamp(order.getCreateTime());
            LocalDateTime update = time2.toLocalDateTime();
            order.setTime2(update);
            orderService.saveOrUpdate(order);
        }*/

    }

    @Test
    void date() {
        long date = 1512535620;
        Timestamp timestamp = new Timestamp(date);
        LocalDateTime create = timestamp.toLocalDateTime();
        User user = new User();
        user.setCreateTime(create);
        System.out.println(user);
    }
}
