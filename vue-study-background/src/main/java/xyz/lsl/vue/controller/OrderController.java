package xyz.lsl.vue.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.lsl.vue.entity.Order;
import xyz.lsl.vue.service.OrderService;
import xyz.lsl.vue.util.ResultUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author YIQU
 * @since 2022-03-29 21:25:46
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping("/orders")
    public ResultUtil getOrdersList(@RequestParam(defaultValue = "1") Integer currentPage,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    String query){
        if (currentPage < 1 || pageSize < 1)
            return ResultUtil.fail("参数不合法，非法攻击将被封禁 IP");
        List<Order> list = new LinkedList<>();
        if (StringUtils.isNotBlank(query))
            list = orderService.getOrders(query);
        else {
            LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByAsc(Order::getCreateTime);
            list = orderService.list(queryWrapper);
        }
        int size = list.size();
        Page page = new Page(currentPage,pageSize);
        if(size == 0)
            return ResultUtil.fail("数据库中没有订单");
        if (pageSize > size)
            pageSize = size;
        // 求出最大页数，防止currentPage越界
        int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;
        if (currentPage > maxPage)
            currentPage = maxPage;
        // 当前页第一条数据的下标
        int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;
        List pageList = new ArrayList();
        // 将当前页的数据放进pageList
        for (int i = 0; i < pageSize && curIdx + i < size; i++)
            pageList.add(list.get(curIdx + i));
        page.setTotal(list.size()).setRecords(pageList);
        return ResultUtil.success("获取当前页订单信息成功",page);
    }

}
