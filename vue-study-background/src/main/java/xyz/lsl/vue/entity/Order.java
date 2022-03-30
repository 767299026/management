package xyz.lsl.vue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author YIQU
 * @since 2022-03-29 21:25:46
 */
@Getter
@Setter
@TableName("orders")
@ApiModel(value = "Order对象", description = "订单表")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    @ApiModelProperty("下订单会员id")
    @TableField("user_id")
    private Integer userId;

    @ApiModelProperty("订单编号")
    @TableField("order_number")
    private String orderNumber;

    @ApiModelProperty("订单总金额")
    @TableField("order_price")
    private BigDecimal orderPrice;

    @ApiModelProperty("支付方式  0未支付 1支付宝  2微信  3银行卡")
    @TableField("order_pay")
    private String orderPay;

    @ApiModelProperty("订单是否已经发货")
    @TableField("is_send")
    private String isSend;

    @ApiModelProperty("支付宝交易流水号码")
    @TableField("trade_no")
    private String tradeNo;

    @ApiModelProperty("发票抬头 个人 公司")
    @TableField("order_invoice_title")
    private String orderInvoiceTitle;

    @ApiModelProperty("公司名称")
    @TableField("order_invoice_company")
    private String orderInvoiceCompany;

    @ApiModelProperty("发票内容")
    @TableField("order_invoice_content")
    private String orderInvoiceContent;

    @ApiModelProperty("consignee收货人地址")
    @TableField("consignee_address")
    private String consigneeAddress;

    @ApiModelProperty("订单状态： 0未付款、1已付款")
    @TableField("pay_status")
    private String payStatus;

    @ApiModelProperty("记录生成时间")
    @TableField("create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("记录修改时间")
    @TableField("update_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
