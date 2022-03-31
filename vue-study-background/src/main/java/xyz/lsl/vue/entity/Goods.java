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
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author YIQU
 * @since 2022-03-30 12:07:26
 */
@Getter
@Setter
@TableName("goods")
@ApiModel(value = "Goods对象", description = "商品表")
public class  Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "goods_id", type = IdType.AUTO)
    private Integer goodsId;

    @ApiModelProperty("商品名称")
    @TableField("goods_name")
    private String goodsName;

    @ApiModelProperty("商品价格")
    @TableField("goods_price")
    private BigDecimal goodsPrice;

    @ApiModelProperty("商品数量")
    @TableField("goods_number")
    private Integer goodsNumber;

    @ApiModelProperty("商品重量")
    @TableField("goods_weight")
    private Integer goodsWeight;

    @ApiModelProperty("类型id")
    @TableField("cat_id")
    private Integer catId;

    @ApiModelProperty("商品详情介绍")
    @TableField("goods_introduce")
    private String goodsIntroduce;

    @ApiModelProperty("图片logo大图")
    @TableField("goods_big_logo")
    private String goodsBigLogo;

    @ApiModelProperty("图片logo小图")
    @TableField("goods_small_logo")
    private String goodsSmallLogo;

    @ApiModelProperty("0:正常  1:删除")
    @TableField("is_del")
    private String isDel;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("delete_date")
    private LocalDateTime deleteDate;

    @ApiModelProperty("热卖数量")
    @TableField("hot_number")
    private Integer hotNumber;

    @ApiModelProperty("是否促销")
    @TableField("is_promote")
    private Integer isPromote;

    @ApiModelProperty("商品状态 0: 未通过 1: 审核中 2: 已审核")
    @TableField("goods_state")
    private Integer goodsState;

    @ApiModelProperty("一级分类id")
    @TableField("cat_one_id")
    private Integer catOneId;

    @ApiModelProperty("二级分类id")
    @TableField("cat_two_id")
    private Integer catTwoId;

    @ApiModelProperty("三级分类id")
    @TableField("cat_three_id")
    private Integer catThreeId;


}
