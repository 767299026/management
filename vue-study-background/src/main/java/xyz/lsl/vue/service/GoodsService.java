package xyz.lsl.vue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import xyz.lsl.vue.entity.Goods;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author YIQU
 * @since 2022-03-30 12:07:26
 */
@Transactional
public interface GoodsService extends IService<Goods> {

    List<Goods> getGoods(String query);
}
