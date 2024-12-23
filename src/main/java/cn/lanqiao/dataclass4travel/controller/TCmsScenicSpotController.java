package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.pojo.*;
import cn.lanqiao.dataclass4travel.service.TYwOrderService;
import cn.lanqiao.dataclass4travel.utils.CommonResult;
import cn.lanqiao.dataclass4travel.utils.DateUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import cn.lanqiao.dataclass4travel.service.TCmsScenicSpotService;
import cn.lanqiao.dataclass4travel.utils.PageHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;

@Controller
@Slf4j
public class TCmsScenicSpotController {

    @Autowired
    private TCmsScenicSpotService tCmsScenicSpotService;

    @Autowired
    private TYwOrderService tYwOrderService;

    /**
     * 01-分页查询
     */
    @RequestMapping("/scenicSpot_list")
    public String list(@RequestParam(defaultValue = "1") Long pageNumber,
                       @RequestParam(defaultValue = "7") Long pageSize,
                       Model model){
        QueryWrapper<TCmsScenicSpot> queryWrapper=new QueryWrapper();
        queryWrapper.eq("DELETE_STATUS",0);
        queryWrapper.orderByDesc("ADD_TIME");
        IPage page = tCmsScenicSpotService.page(new Page<TCmsScenicSpot>(pageNumber, pageSize), queryWrapper);
        // 封装
        PageHelper<TCmsScenicSpot> pagerHelper = new PageHelper<TCmsScenicSpot>(pageNumber, pageSize, page.getPages(), page.getTotal(), page.getRecords());
        model.addAttribute("pagerHelper",pagerHelper);
        return "scenicSpot/scenicSpotList";
    }

    /**
     * 02-跳转新增页面
     */
    @RequestMapping("/scenicSpot_toAdd")
    public String toAdd(){
        return "scenicSpot/scenicSpotAdd";
    }

    /**
     * 03-新增(异步)
     */
    @ResponseBody
    @RequestMapping("/scenicSpot_add")
    public CommonResult add(TCmsScenicSpot tCmsScenicSpot, HttpSession session){
        try {
            //设置当前系统时间
            String nowTime = DateUtils.getNowTime();
            tCmsScenicSpot.setAddTime(nowTime);
            //设置添加人的id,从session中获取addUserId
            // 获取当前管理员信息
            TPzAdminUser addAdmin = (TPzAdminUser) session.getAttribute("admin");
            // 账号登录检测
            if (addAdmin == null) {
                return new CommonResult(304, "用户未登录或Session已过期");
            }
            tCmsScenicSpot.setAddUserId(addAdmin.getId());
            //操作数据库进行添加
            System.out.println("要新增的对象是:"+tCmsScenicSpot);
            //需要响应类
            return new CommonResult(200,"请求成功",tCmsScenicSpotService.save(tCmsScenicSpot));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(304,"请求异常");
        }
    }


    /**
     * 04-跳转详情
     */
    @GetMapping("/scenicSpot_View/{id}")
    public String todetail(@PathVariable("id") String id, Model model){
        TCmsScenicSpot ById = tCmsScenicSpotService.getById(id);
        if (ById != null) {
            model.addAttribute("entity", ById);
        }else {
            model.addAttribute("error", "账号未登录");
        }
        return "scenicSpot/scenicSpotView";
    }

    /**
     * 05-跳转更新
     */
    @GetMapping("/scenicSpot_toupdate/{id}")
    public String toEdit(@PathVariable("id") String id, Model model){
        TCmsScenicSpot ById = tCmsScenicSpotService.getById(id);
        model.addAttribute("entity", ById);
        return "scenicSpot/scenicSpotEdit";
    }

    /**
     * 06-更新
     */
    @ResponseBody
    @PostMapping("/scenicSpot_update")
    public CommonResult update(TCmsScenicSpot tCmsScenicSpot, HttpSession session){
        try {
            // 是数据库中原对象
            TCmsScenicSpot old_tCmsScenicSpot = tCmsScenicSpotService.getById(tCmsScenicSpot.getId());
            // 如果上传了新照片，就需要删除老照片
            // 所以if来判断旧新的图片地址是不是一样的，不一样，就删掉老照片
            if (!tCmsScenicSpot.getImgUrl().equals(old_tCmsScenicSpot.getImgUrl())){
                String realPath = ResourceUtils.getURL("classpath:").getPath();
                String filePath = old_tCmsScenicSpot.getImgUrl();
                realPath = realPath.substring(1,realPath.length())+"static"+filePath;
                File f = new File(realPath);
                if (f.exists()){
                    f.delete();
                    System.out.println("删除了老照片，地址是："+realPath);
                }
            }
            //设置当前系统时间
            tCmsScenicSpot.setModifyTime(DateUtils.getNowTime());
            //设置新增酒店的人
            TPzAdminUser addAdmin = (TPzAdminUser) session.getAttribute("admin");
            if (addAdmin == null) {
                return new CommonResult(304, "用户未登录或Session已过期");
            }
            // 设置添加人的id
            tCmsScenicSpot.setAddUserId(addAdmin.getId());
            System.out.println("要更新的对象是："+tCmsScenicSpot);
            return new CommonResult(200,"请求成功",tCmsScenicSpotService.updateById(tCmsScenicSpot));

        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult(500,"请求失败");
        }
    }

    /**
     * 07-删除
     */
    @GetMapping("/scenicSpot_delete/{id}")
    public CommonResult delete(@PathVariable("id") String id){
        TCmsScenicSpot ById = tCmsScenicSpotService.getById(id);
        ById.setDeleteStatus(1L);
        tCmsScenicSpotService.updateById(ById);
        return new CommonResult(200, "请求成功");
    }


    /*跳转前台景点页面*/
    /* 01 - 分页查询
    */
    @RequestMapping("/portal_scenicSpot_list")
    public String carList(@RequestParam(defaultValue = "1") Long pageNumber,
                          @RequestParam(defaultValue = "7") Long pageSize,
                          @RequestParam(defaultValue = "") String title,
                          Model model){
        //构造条件
        QueryWrapper<TCmsScenicSpot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS","0");
        queryWrapper.orderByDesc("ADD_TIME");
        //条件查询
        if (!"".equals(title)){
            queryWrapper.like("TITLE", title);
        }
        IPage page = tCmsScenicSpotService.page(new Page<TCmsScenicSpot>(pageNumber, pageSize), queryWrapper);
        //将page对象存入pageHelper对象中
        PageHelper<TCmsScenicSpot> pageHelper = new PageHelper<TCmsScenicSpot>(pageNumber,pageSize,page.getPages(),page.getTotal(),page.getRecords());
        model.addAttribute("pagerHelper", pageHelper);
        return "/portal/travelSpot";
    }

    /*
     * 02 - 详情
     */
    @GetMapping("portal_scenicSpot_view/{id}")
    public String view(@PathVariable("id") String id, Model model){
        TCmsScenicSpot tCmsScenicSpot = tCmsScenicSpotService.getById(id);
        model.addAttribute("entity", tCmsScenicSpot);
        return "/portal/travelSpotView";
    }

    /**
     * 数据分析
     * @return
     */
    @GetMapping("/toscenicSpotData")
    public String toscenicSpotData(Model model){

        System.out.println("数据分析");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("DELETE_STATUS", 0);
        queryWrapper.select("count(id),STATE");
        queryWrapper.groupBy("STATE");
        queryWrapper.orderByAsc("STATE");

        List<Map<String,Object>> list = tCmsScenicSpotService.listMaps(queryWrapper);

        Object count_0 = 0;
        if (list.size() > 0) {
            Map<String, Object> map0 = list.get(0);
            count_0 = map0.get("count(id)");
        }

        Object count_1 = 0;
        if (list.size() > 1) {
            Map<String, Object> map1 = list.get(1);
            count_1 = map1.get("count(id)");
        }

        Object count_2 = 0;
        if (list.size() > 2) {
            Map<String, Object> map2 = list.get(2);
            count_2 = map2.get("count(id)");
        }

        String datas="["+count_0+", "+count_1+", "+count_2+"]";
//
        model.addAttribute("datas",datas);

        return "data/scenicSpotData";
    }

    /*
     * 预约
     */
    @GetMapping("/user_createScenicSpotOrder")
    public String createScenicSpotOrder(@RequestParam("id") String id , Model model,HttpSession session){

        TCmsScenicSpot tCmsScenicSpot = tCmsScenicSpotService.getById(id);

        try {
            TPzUser user = (TPzUser) session.getAttribute("user");
            TYwOrder tYwOrder = new TYwOrder();
            tYwOrder.setAddUserId(user.getId());
            tYwOrder.setAddTime(DateUtils.getNowTime());
            tYwOrder.setDeleteStatus(0L);
            tYwOrder.setUserId(user.getId());
            tYwOrder.setUserName(user.getUserName());
            tYwOrder.setProductId(id);
            tYwOrder.setProductType(1L);
            tYwOrder.setState(0L);
            tYwOrder.setOrderCode(DateUtils.getOrderId());
            tYwOrder.setOrderTime(DateUtils.getNowTime());
            tYwOrder.setLinkTel(user.getLinkTel());
            tYwOrder.setIcCode(user.getIcCode());

            tYwOrder.setProductName(tCmsScenicSpot.getSpotName());
            tYwOrder.setFee(tCmsScenicSpot.getTicketsMessage());
            tYwOrder.setSetoffTime(tCmsScenicSpot.getOpenTime());
            tYwOrder.setImgUrl(tCmsScenicSpot.getImgUrl());

            tYwOrderService.save(tYwOrder);
            model.addAttribute("msg", "预定成功");

        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("msg", "预定失败");
        }finally {

        }

        model.addAttribute("entity", tCmsScenicSpot);
        return "/portal/travelSpotView";
    }
}