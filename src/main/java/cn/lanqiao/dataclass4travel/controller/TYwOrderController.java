package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.pojo.OrderData;
import cn.lanqiao.dataclass4travel.pojo.TPzUser;
import cn.lanqiao.dataclass4travel.pojo.TYwOrder;
import cn.lanqiao.dataclass4travel.service.TYwOrderService;

import cn.lanqiao.dataclass4travel.utils.CommonResult;
import cn.lanqiao.dataclass4travel.utils.PageHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;


import java.nio.file.FileStore;
import java.util.ArrayList;
import java.util.List;
//import java.util.stream.Collectors;

@Controller
public class TYwOrderController {
    @Autowired
    private TYwOrderService tYwOrderService;

    //订单管理分页查询功能
    @RequestMapping("/userOrder_list")
    public String list(@RequestParam(defaultValue = "1") Long pageNumber,
                       @RequestParam(defaultValue = "7") Long pageSize,
                       @RequestParam(defaultValue = "") String userName,
                       Model model){

        QueryWrapper<TYwOrder> queryWrapper = new QueryWrapper();
        queryWrapper.eq("DELETE_STATUS",0);
        queryWrapper.orderByDesc("ADD_TIME");
        if (!"".equals(userName)){
            queryWrapper.like("USER_NAME", userName);
        }
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



    //前端订单管理分页查询

    @RequestMapping("/user_myorderlist")
    public String listWeb(@RequestParam(defaultValue = "1") Long pageNumber,
                          @RequestParam(defaultValue = "7") Long pageSize,
                          HttpSession session,
                          Model model) {
        // 1. 设置分页查询的条件
        QueryWrapper<TYwOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS", 0);  // 过滤删除状态为 0 的订单
        queryWrapper.orderByDesc("ADD_TIME"); // 按照添加时间降序排列
        TPzUser user = (TPzUser) session.getAttribute("user");
        String userId = user.getId();
        queryWrapper.eq("id",userId);
        // 2. 使用 MyBatis-Plus 的分页查询
        IPage<TYwOrder> page = tYwOrderService.page(new Page<TYwOrder>(pageNumber, pageSize), queryWrapper);
        // 3. 封装分页数据到 PageHelper（假设你有自己定义的分页工具类）
        PageHelper<TYwOrder> pagerHelper = new PageHelper<>(pageNumber, pageSize, page.getPages(), page.getTotal(), page.getRecords());
        // 4. 将分页数据传递给前端
        model.addAttribute("pagerHelper", pagerHelper);
        // 5. 返回到对应的页面
        return "portal/myOrder";
    }


    //跳转付款页面
    @RequestMapping("/user_topayOrder/{id}")
    public String editOrderWeb(@PathVariable String id, Model model) {
        // 获取订单详情
        TYwOrder order = tYwOrderService.getById(id);
        if (order != null) {
            // 将订单编号和费用添加到模型
            model.addAttribute("order_code", order.getOrderCode());
            model.addAttribute("fee", order.getFee());
        } else {
            model.addAttribute("error", "订单未找到");
        }
        return "portal/pay";  // 返回订单详情页面
    }


//数据分析
    @RequestMapping("/toorderData")
    public String toorderData(Model model) throws Exception {

        List<OrderData> orderDataList = new ArrayList<>();
        int i = Math.toIntExact(tYwOrderService.lambdaQuery().eq(TYwOrder::getProductType, 0).eq(TYwOrder::getDeleteStatus, 0).count());

        int j = Math.toIntExact(tYwOrderService.lambdaQuery().eq(TYwOrder::getProductType, 1).eq(TYwOrder::getDeleteStatus, 0).count());

        int k = Math.toIntExact(tYwOrderService.lambdaQuery().eq(TYwOrder::getProductType, 2).eq(TYwOrder::getDeleteStatus, 0).count());

        int l = Math.toIntExact(tYwOrderService.lambdaQuery().eq(TYwOrder::getProductType, 3).eq(TYwOrder::getDeleteStatus, 0).count());

        int m = Math.toIntExact(tYwOrderService.lambdaQuery().eq(TYwOrder::getProductType, 4).eq(TYwOrder::getDeleteStatus, 0).count());

            if(i>0){
                orderDataList.add(new OrderData(i,"旅游路线",0)); //旅游路线
            }
            if(j>0){
                orderDataList.add(new OrderData(j,"旅游景点",1)); //旅游景点
            }
            if(k>0){
                orderDataList.add(new OrderData(k,"旅游酒店",2)); //旅游酒店
            }
            if(l>0){
                orderDataList.add(new OrderData(l,"旅游车票",3)); //旅游车票
            }
            if(m>0){
                orderDataList.add(new OrderData(m,"旅游保险",4)); //旅游保险
            }


            //对象转JSON
            ObjectMapper objectMapper=new ObjectMapper();
            String datas = objectMapper.writeValueAsString(orderDataList);
            System.out.println(datas);
            model.addAttribute("datas",datas);
            return "data/orderData";
    }


    //撤销功能
    @RequestMapping("/user_cancelOrder/{orderId}/{pageNumber}")
    public String cancelOrder(@PathVariable String orderId, @PathVariable Long pageNumber, Model model) {
        // 1. 根据订单ID获取订单对象
        TYwOrder order = tYwOrderService.getById(orderId);

        // 2. 判断订单是否存在，并且是否允许取消
        if (order != null && order.getState() == 0) { // 假设 0 代表未付款的状态
            // 3. 修改订单状态为取消
            order.setState(2);  // 假设 2 代表已取消状态
            tYwOrderService.updateById(order); // 更新数据库中的订单状态

            // 4. 取消订单后，我们依然需要返回分页后的订单列表
            return "redirect:/user_myorderlist?pageNumber=" + pageNumber;  // 保持分页状态
        } else {
            // 如果订单不存在或者不允许取消，返回错误提示页面或者重定向
            model.addAttribute("error", "订单取消失败");
            return "portal/myOrder"; // 返回订单列表页面，展示错误信息
        }
    }
}
