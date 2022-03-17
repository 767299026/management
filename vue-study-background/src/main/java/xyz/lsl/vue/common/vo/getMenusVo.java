package xyz.lsl.vue.common.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class getMenusVo {

    Integer id;
    String authName;
    String paths;
    String oder;
    List<getMenusVo> children = new ArrayList<>();//二级菜单

}
