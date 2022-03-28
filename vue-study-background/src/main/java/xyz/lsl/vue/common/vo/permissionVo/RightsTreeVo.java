package xyz.lsl.vue.common.vo.permissionVo;

import lombok.Data;

import java.util.List;


@Data
public class RightsTreeVo {

    private Integer id;
    private String authName;
    private String paths;
    private List<permission> children;//二级权限

    @Data
    public static class permission {
        private Integer id;
        private String authName;
        private String paths;
        private List<children> children;//三级权限

        @Data
        public static class children {
            private Integer id;
            private String authName;
            private String paths;
        }
    }
}
