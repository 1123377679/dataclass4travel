package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.pojo.TYwOrder;
import cn.lanqiao.dataclass4travel.service.TYwOrderService;
import cn.lanqiao.dataclass4travel.utils.CommonResult;
import cn.lanqiao.dataclass4travel.utils.PageHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TYwOrderController {
    @Autowired
    private TYwOrderService tYwOrderService;
    //订单管理分页查询功能
    @RequestMapping("/userOrder_list")
    public String list(@RequestParam(defaultValue = "1") Long pageNumber,
                       @RequestParam(defaultValue = "7") Long pageSize,
                       Model model){
        QueryWrapper<TYwOrder> queryWrapper = new QueryWrapper();
        queryWrapper.eq("DELETE_STATUS",0);
        queryWrapper.orderByDesc("ADD_TIME");
        IPage page = tYwOrderService.page(new Page<TYwOrder>(pageNumber, pageSize), queryWrapper);
        //封装工具类
        PageHelper<TYwOrder> pagerHelper=new PageHelper<TYwOrder>(pageNumber,pageSize,page.getPages(),page.getTotal(),page.getRecords());
        model.addAttribute("pagerHelper",pagerHelper);
        return "order/orderList";
    }

    //查看详细数据
    @RequestMapping("/userOrder_view/{id}")
    public String viewOrderDetails(@PathVariable String id, Model model) {
        // 获取订单详情
        TYwOrder order = tYwOrderService.getById(id);
        if (order != null) {
            model.addAttribute("entity", order);
        } else {
            model.addAttribute("error", "订单未找到");
        }
        return "order/orderView";  // 返回订单详情页面
    }

    //跳转修改页面
    @RequestMapping("/userOrder_toupdate/{id}")
    public String editOrder(@PathVariable String id, Model model) {
        // 获取订单详情
        TYwOrder order = tYwOrderService.getById(id);
        if (order != null) {
            model.addAttribute("entity", order);
        } else {
            model.addAttribute("error", "订单未找到");
        }
        return "order/orderEdit";  // 返回订单详情页面
    }

    //保存修改代码
    @PostMapping("/userOrder_update")
    @ResponseBody
    public CommonResult  updateOrder(TYwOrder order) {
        System.out.println("收到的要求内容: " + order.getRequirement());
        return new CommonResult(200,"请求成功",tYwOrderService.updateById(order));

        }

        //删除订单1
    @GetMapping("/userOrder_delete/{id}")
    @ResponseBody
    public CommonResult delete(@PathVariable("id") String id){
        //TODO:后期需要补代码判断这个旅游路线是否在使用用，如果在使用中提示暂时不能删除
        TYwOrder tYwOrder = tYwOrderService.getById(id);
        tYwOrder.setDeleteStatus(1L);//删除状态
        tYwOrderService.updateById(tYwOrder);
        return new CommonResult(200,"请求成功");
    }
}
