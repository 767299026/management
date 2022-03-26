package xyz.lsl.vue.common.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class getMenusVo {

    private Integer id;
    private String authName;
    private String paths;
    private String oder;
    private List<getMenusVo> children = new ArrayList<>();//二级菜单

}
