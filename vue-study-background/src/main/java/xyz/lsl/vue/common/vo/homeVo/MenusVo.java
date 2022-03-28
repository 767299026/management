package xyz.lsl.vue.common.vo.homeVo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenusVo {

    private Integer id;
    private String authName;
    private String paths;
    private String oder;
    private List<MenusVo> children = new ArrayList<>();//二级菜单

}
