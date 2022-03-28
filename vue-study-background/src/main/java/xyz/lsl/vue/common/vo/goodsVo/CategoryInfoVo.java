package xyz.lsl.vue.common.vo.goodsVo;

import lombok.Data;

@Data
public class CategoryInfoVo {

    private Integer categoryId;
    private String categoryName;
    private Integer[] parentId;
    private Integer categoryLevel;
    private boolean categoryStatus;
}
