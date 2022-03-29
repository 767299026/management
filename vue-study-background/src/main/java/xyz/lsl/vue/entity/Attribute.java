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
 * 属性表
 * </p>
 *
 * @author YIQU
 * @since 2022-03-28 21:54:01
 */
@Getter
@Setter
@TableName("attribute")
@ApiModel(value = "Attribute对象", description = "属性表")
public class Attribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "attr_id", type = IdType.AUTO)
    private Integer attrId;

    @ApiModelProperty("属性名称")
    @TableField("attr_name")
    private String attrName;

    @ApiModelProperty("外键，类型id")
    @TableField("cat_id")
    private Integer catId;

    @ApiModelProperty("only:输入框(唯一)  many:后台下拉列表/前台单选框")
    @TableField("attr_sel")
    private String attrSel;

    @ApiModelProperty("manual:手工录入  list:从列表选择")
    @TableField("attr_write")
    private String attrWrite;

    @ApiModelProperty("可选值列表信息,例如颜色：白色,红色,绿色,多个可选值通过逗号分隔")
    @TableField("attr_vals")
    private String attrVals;

    @ApiModelProperty("删除时间标志")
    @TableField("delete_time")
    private Integer deleteTime;


}
