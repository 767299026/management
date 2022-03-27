package xyz.lsl.vue.common.vo.roleVo;

import lombok.Data;
import xyz.lsl.vue.common.vo.permissionVo.getRightsTreeVo;

import java.util.List;

@Data
public class getRolesVo {

    private Integer roleId;
    private String roleName;
    private String roleDesc;
    private List<getRightsTreeVo> children;

}


