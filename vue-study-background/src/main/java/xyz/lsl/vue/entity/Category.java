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
 * @since 2022-03-26 20:47:00
 */
@Getter
@Setter
@TableName("category")
@ApiModel(value = "Category对象", description = "")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分类唯一ID")
    @TableId(value = "cat_id", type = IdType.AUTO)
    private Integer catId;

    @ApiModelProperty("分类名称")
    @TableField("cat_name")
    private String catName;

    @ApiModelProperty("分类父ID")
    @TableField("cat_pid")
    private Integer catPid;

    @ApiModelProperty("分类层级 0: 顶级 1:二级 2:三级")
    @TableField("cat_level")
    private Integer catLevel;

    @ApiModelProperty("是否删除 1为删除")
    @TableField("cat_deleted")
    private Integer catDeleted;

    @TableField("cat_icon")
    private String catIcon;

    @TableField("cat_src")
    private String catSrc;


}
