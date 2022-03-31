package xyz.lsl.vue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xyz.lsl.vue.entity.Goods;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author YIQU
 * @since 2022-03-30 12:07:26
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<Goods> getGoodsByGoodsName(@Param("goodsName") String goodsName);
}
