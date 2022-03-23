package xyz.lsl.vue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author YIQU
 * @since 2022-03-17 12:16:25
 */
@Data
@TableName("permission")
@ApiModel(value = "Permission对象", description = "权限表")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ps_id", type = IdType.AUTO)
    private Integer psId;

    @ApiModelProperty("权限名称")
    @TableField("ps_name")
    private String psName;

    @ApiModelProperty("父id")
    @TableField("ps_pid")
    private Integer psPid;

    @ApiModelProperty("控制器")
    @TableField("ps_c")
    private String psC;

    @ApiModelProperty("操作方法")
    @TableField("ps_a")
    private String psA;

    @ApiModelProperty("权限等级")
    @TableField("ps_level")
    private String psLevel;


}
