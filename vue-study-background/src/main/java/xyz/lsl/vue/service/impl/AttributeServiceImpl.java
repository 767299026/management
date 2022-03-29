package xyz.lsl.vue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.lsl.vue.entity.Attribute;
import xyz.lsl.vue.mapper.AttributeMapper;
import xyz.lsl.vue.service.AttributeService;

/**
 * <p>
 * 属性表 服务实现类
 * </p>
 *
 * @author YIQU
 * @since 2022-03-28 21:54:01
 */
@Service
public class AttributeServiceImpl extends ServiceImpl<AttributeMapper, Attribute> implements AttributeService {

}
