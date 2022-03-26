package xyz.lsl.vue.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class getRolesVo {

    private Integer roleId;
    private String roleName;
    private String roleDesc;
    private List<getRightsTreeVo> children;

}


