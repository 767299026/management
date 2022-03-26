package xyz.lsl.vue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author YIQU
 * @since 2022-03-24 20:03:48
 */
@Getter
@Setter
@TableName("role")
@ApiModel(value = "Role对象", description = "")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;

    @ApiModelProperty("角色名称")
    @TableField("role_name")
    private String roleName;

    @ApiModelProperty("权限ids,1,2,5")
    @TableField("ps_ids")
    private String psIds;

    @ApiModelProperty("控制器-操作,控制器-操作,控制器-操作")
    @TableField("ps_ca")
    private String psCa;

    @ApiModelProperty("角色描述")
    @TableField("role_desc")
    private String roleDesc;


}
