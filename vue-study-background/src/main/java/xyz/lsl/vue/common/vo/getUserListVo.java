package xyz.lsl.vue.common.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class getUserListVo {

    private Integer id;
    private String username;
    private String avatar;
    private String email;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String role;
}
