package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.pojo.TCmsTravelRoute;
import cn.lanqiao.dataclass4travel.pojo.TPzAdminUser;
import cn.lanqiao.dataclass4travel.service.TCmsTravelRouteService;
import cn.lanqiao.dataclass4travel.utils.CommonResult;
import cn.lanqiao.dataclass4travel.utils.DateUtils;
import cn.lanqiao.dataclass4travel.utils.PageHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class TravelRouteController {
    @Autowired
    private TCmsTravelRouteService tCmsTravelRouteService;

    /*车票分页查询*/
    @RequestMapping("/travelRoute_list")
    public String list(@RequestParam(defaultValue = "1") Long pageNumber,
                       @RequestParam(defaultValue = "7") Long pageSize,
                       Model model){
        QueryWrapper<TCmsTravelRoute> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS","0");
        queryWrapper.orderByDesc("ADD_TIME");
        IPage page = tCmsTravelRouteService.page(new Page<TCmsTravelRoute>(pageNumber, pageSize), queryWrapper);
        //将page对象存入pageHelper对象中
        PageHelper<TCmsTravelRoute> pageHelper = new PageHelper<TCmsTravelRoute>(pageNumber,pageSize,page.getPages(),page.getTotal(),page.getRecords());
        model.addAttribute("pagerHelper", pageHelper);
        return "travelRoute/travelRouteList";
    }
    //跳转到添加页面
    @RequestMapping("/travelRoute_toadd")
    public String toAdd(){
        return "travelRoute/travelRouteAdd";
    }
    //新增路线
    @RequestMapping("/travelRoute_add")
    @ResponseBody
    public CommonResult addRoute(TCmsTravelRoute tCmsTravelRoute, HttpSession session){
        try {
            //设置当前系统时间
            String nowTime = DateUtils.getNowTime();
            tCmsTravelRoute.setAddTime(nowTime);
            //操作数据库进行添加
            System.out.println("要新增的对象是:"+tCmsTravelRoute);
            //需要响应类
            return new CommonResult(200,"请求成功",tCmsTravelRouteService.save(tCmsTravelRoute));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //跳转到编辑页面
    @RequestMapping("/travelRoute_toEdit")
    public String toEdit(String id,TCmsTravelRoute tCmsTravelRoute, HttpSession session, Model model){
        tCmsTravelRoute.setId(id);
//        tCmsTravelRoute = tCmsTravelRouteService.getById(id);
        model.addAttribute("tCmsTravelRoute", tCmsTravelRoute);
        //session.setAttribute("tCmsTravelRoute",tCmsTravelRoute);
        return "travelRoute/travelRouteEdit";
    }
    //编辑路线
    @RequestMapping("/travelRoute_update")
    @ResponseBody
    public CommonResult editRoute(TCmsTravelRoute tCmsTravelRoute, HttpSession session){
        try {
            //设置当前系统时间
            String nowTime = DateUtils.getNowTime();
            tCmsTravelRoute.setAddTime(nowTime);
            //操作数据库进行添加
            System.out.println("要修改的对象是:"+tCmsTravelRoute);
            //需要响应类
            return new CommonResult(200,"请求成功",tCmsTravelRouteService.updateById(tCmsTravelRoute));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
