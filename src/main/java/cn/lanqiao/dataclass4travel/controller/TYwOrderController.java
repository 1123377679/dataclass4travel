package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.pojo.TYwOrder;
import cn.lanqiao.dataclass4travel.service.TYwOrderService;
import cn.lanqiao.dataclass4travel.utils.PageHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    //添加新数据
    @RequestMapping("/manager/orderSave")
     public  String save (){
        return "order/orderView";
     }
}
