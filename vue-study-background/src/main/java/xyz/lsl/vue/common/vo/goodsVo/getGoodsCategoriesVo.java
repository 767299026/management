package xyz.lsl.vue.common.vo.goodsVo;

import lombok.Data;

import java.util.List;

@Data
public class getGoodsCategoriesVo {

    private Integer categoryId;
    private String categoryName;
    private Integer parentId;
    private Integer categoryLevel;
    private boolean categoryStatus;
    List<goodsCategories> children;

    @Data
    public static class goodsCategories {
        private Integer categoryId;
        private String categoryName;
        private Integer parentId;
        private Integer categoryLevel;
        private boolean categoryStatus;
        List<children> children;

        @Data
        public static class children {
            private Integer categoryId;
            private String categoryName;
            private Integer parentId;
            private Integer categoryLevel;
            private boolean categoryStatus;
        }
    }

}
