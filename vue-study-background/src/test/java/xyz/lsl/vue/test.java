package xyz.lsl.vue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.lsl.vue.entity.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@SpringBootTest
public class test {

    /*@Resource
    private GoodsService goodsService;*/

    @Test
    void changeDate() {
        /*List<Goods> list = goodsService.list();
        for (Goods goods : list) {
            Timestamp time1 = new Timestamp(goods.getAddTime());
            LocalDateTime create = time1.toLocalDateTime();
            goods.setCreateTime(create);
            Timestamp time2 = new Timestamp(goods.getUpdTime());
            LocalDateTime update = time2.toLocalDateTime();
            goods.setUpdateTime(update);
            if(goods.getDeleteTime()!=null){
                Timestamp time3 = new Timestamp(goods.getDeleteTime());
                LocalDateTime delete = time3.toLocalDateTime();
                goods.setDeleteDate(delete);
            }
            goodsService.saveOrUpdate(goods);
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
