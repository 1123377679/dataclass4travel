package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.pojo.TCmsTravelRoute;
import cn.lanqiao.dataclass4travel.pojo.TPzAdminUser;
import cn.lanqiao.dataclass4travel.service.TCmsTravelRouteService;
import cn.lanqiao.dataclass4travel.service.TPzAdminUserService;
import cn.lanqiao.dataclass4travel.utils.CommonResult;
import cn.lanqiao.dataclass4travel.utils.DateUtils;
import cn.lanqiao.dataclass4travel.utils.PageHelper;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.v3.core.util.Json;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Controller
@Slf4j
public class TravelRouteController {
    @Autowired
    private TCmsTravelRouteService tCmsTravelRouteService;
    @Autowired
    private TPzAdminUserService tPzAdminUserService;
    /*车票分页查询*/
    @RequestMapping("/travelRoute_list")
    public String list(@RequestParam(defaultValue = "1") Long pageNumber,
                       @RequestParam(defaultValue = "7") Long pageSize,
                       @RequestParam(defaultValue = "") String title,
                       Model model){
        QueryWrapper<TCmsTravelRoute> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS","0");
        queryWrapper.orderByDesc("ADD_TIME");
        if (!"".equals(title)){
            queryWrapper.like("TITLE", title);
        }
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
    //跳转到详情页面
    @GetMapping("/travelRoute_todetail/{id}")
    public String toDetail(@PathVariable("id") String id, Model model){
        TCmsTravelRoute tCmsTravelRoute = tCmsTravelRouteService.getById(id);
        model.addAttribute("entity", tCmsTravelRoute);//回显数据的对象
        return "travelRoute/travelRouteView";
    }
    //跳转到编辑页面
    @GetMapping("/travelRoute_toEdit/{id}")
    public String toEdit(@PathVariable("id") String id, Model model){
        TCmsTravelRoute tCmsTravelRoute = tCmsTravelRouteService.getById(id);
        model.addAttribute("entity", tCmsTravelRoute);//回显数据的对象
        return "travelRoute/travelRouteEdit";
    }
    //编辑路线
    @PostMapping("/travelRoute_update")
    @ResponseBody
    public CommonResult editRoute(TCmsTravelRoute tCmsTravelRoute, HttpSession session) throws Exception {
        try {
            //对象是数据库中原对象
            TCmsTravelRoute tCmsTravelRoute1 = tCmsTravelRouteService.getById(tCmsTravelRoute.getId());
            if(!tCmsTravelRoute.getImgUrl().equals(tCmsTravelRoute1.getImgUrl())){
                //此处上传了新照片，需要删除旧照片
                String realPath = ResourceUtils.getURL("classpath:").getPath();
                String filePath = tCmsTravelRoute1.getImgUrl();
                realPath= realPath.substring(1,realPath.length())+"static"+filePath;
                File f=new File(realPath);
                if(f.exists()){
                    f.delete();
                    //System.out.println("删除了老照片，地址是："+realPath);
                }
            }
            //设置当前系统时间
            String nowTime = DateUtils.getNowTime();
            tCmsTravelRoute.setAddTime(nowTime);
            //设置修改路线的人ID
            tCmsTravelRoute.setModifyUserId(session.getId());
            //System.out.println("要更新的对象是:"+tCmsTravelRoute);
            //需要响应类
            return new CommonResult(200,"请求成功",tCmsTravelRouteService.updateById(tCmsTravelRoute));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(500,"请求失败");
        }
    }
    //删除路线
    @GetMapping("/travelRoute_delete/{id}")
    @ResponseBody
    public CommonResult deleteRoute(@PathVariable("id") String id) throws Exception {
        try {
            //需要响应类
            TCmsTravelRoute tCmsTravelRoute = tCmsTravelRouteService.getById(id);
            tCmsTravelRoute.setDeleteStatus(1);
            tCmsTravelRouteService.updateById(tCmsTravelRoute);
            return new CommonResult(200,"请求成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(500,"请求失败");
        }
    }



    //前台用户登录的网页
    //跳转到路线页面
    //实现分页查询
    @RequestMapping("/portal_travelRoute_list")
    public String portalList(@RequestParam(defaultValue = "1") Long pageNumber,
                       @RequestParam(defaultValue = "7") Long pageSize,
                       @RequestParam(defaultValue = "") String title,
                       Model model){
        QueryWrapper<TCmsTravelRoute> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS","0");
        queryWrapper.orderByDesc("ADD_TIME");
        if (!"".equals(title)){
            queryWrapper.like("TITLE", title);
        }
        IPage page = tCmsTravelRouteService.page(new Page<TCmsTravelRoute>(pageNumber, pageSize), queryWrapper);
        //将page对象存入pageHelper对象中
        PageHelper<TCmsTravelRoute> pageHelper = new PageHelper<TCmsTravelRoute>(pageNumber,pageSize,page.getPages(),page.getTotal(),page.getRecords());
        model.addAttribute("pagerHelper", pageHelper);
        return "/portal/travelRoute";
    }
    //跳转到详情页面
    @GetMapping("/portal_travelRoute_toview/{id}")
    public String toPortalDetail(@PathVariable("id") String id, Model model){
        TCmsTravelRoute tCmsTravelRoute = tCmsTravelRouteService.getById(id);
        model.addAttribute("entity", tCmsTravelRoute);//回显数据的对象
        return "/portal/travelRouteView";
    }
}
