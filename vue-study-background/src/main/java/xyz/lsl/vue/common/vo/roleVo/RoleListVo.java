package xyz.lsl.vue.common.vo.roleVo;

import lombok.Data;
import xyz.lsl.vue.common.vo.permissionVo.RightsTreeVo;

import java.util.List;

@Data
public class RoleListVo {

    private Integer roleId;
    private String roleName;
    private String roleDesc;
    private List<RightsTreeVo> children;

}


