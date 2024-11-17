package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.mapper.HotelMapper;
import cn.lanqiao.dataclass4travel.pojo.Hotel;
import cn.lanqiao.dataclass4travel.pojo.TPzAdminUser;
import cn.lanqiao.dataclass4travel.service.HotelService;
import cn.lanqiao.dataclass4travel.utils.PageHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@Slf4j
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private HotelMapper hotelMapper;
    @RequestMapping("/hotel_list")
    public String list(@RequestParam(defaultValue = "1") long pageNumber,
                       @RequestParam(defaultValue = "7") long pageSize,
                       Model model) {
        // 构造查询条件
        QueryWrapper<Hotel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS", 0);  // 确保查询条件正确
        queryWrapper.orderByDesc("ADD_TIME");
        // 调用 service 层的分页查询方法
        IPage page = hotelService.page(new Page<Hotel>(pageNumber, pageSize), queryWrapper);
        // 包装分页信息
        PageHelper<Hotel> hotelPageHelper = new PageHelper<Hotel>(pageNumber, pageSize, page.getPages(), page.getTotal(),page.getRecords());
        // 将分页数据传递给前端页面
        model.addAttribute("pagerHelper", hotelPageHelper);
        //应该用工具类返回撒,哦哦你用了的
        return "hotel/hotelList";  // 返回视图
    }
}
