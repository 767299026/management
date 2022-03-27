package xyz.lsl.vue.common.vo.permissionVo;

import lombok.Data;

@Data
public class getRightsListVo {

    private Integer id;
    private String authName;
    private String paths;
    private String level;
    private String pid;
}
