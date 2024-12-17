package cn.lanqiao.dataclass4travel.controller;


import cn.lanqiao.dataclass4travel.pojo.TCmsCar;
import cn.lanqiao.dataclass4travel.pojo.TPzUser;
import cn.lanqiao.dataclass4travel.pojo.TYwOrder;
import cn.lanqiao.dataclass4travel.pojo.User;
import cn.lanqiao.dataclass4travel.service.TCmsCarService;
import cn.lanqiao.dataclass4travel.service.TYwOrderService;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class TCmsCarController {

    @Autowired
    private TCmsCarService tCmsCarService;
    @Autowired
    private TYwOrderService tYwOrderService;


    /*跳转新增车票页面*/
    @RequestMapping("/car_toadd")
    public String toAdd(){
        return "car/carAdd";
    }


    /*新增车票功能(异步请求)*/
    @RequestMapping("/car_add")
    @ResponseBody
    public CommonResult add(TCmsCar tCmsCar){
            //设置当前系统时间
            tCmsCar.setAddTime(DateUtils.getNowTime());

            tCmsCar.setAddUserId("b496894b89754a848e9b74ff66a05d44");
            //操作数据库进行添加
            log.info("新增车票信息："+tCmsCar);
            //需要响应类
            return new CommonResult(200,"请求成功",tCmsCarService.save(tCmsCar));

    }



    /*跳转详情页面*/
    @GetMapping("/car_todetail/{id}")
    public String todetail(@PathVariable("id") String id,Model model){
        TCmsCar byId = tCmsCarService.getById(id);
        model.addAttribute("entity",byId);
        return "car/carView";
    }



    /*跳转更新页面*/
    @GetMapping("/car_toEdit/{id}")
    public String toEdit(@PathVariable("id") String id,Model model){
        //获取id
        TCmsCar byId = tCmsCarService.getById(id);
        //回显数据的对象
        model.addAttribute("entity",byId);
        return "car/carEdit";
    }



    /*更新车票功能（异步更新）*/
    @RequestMapping("/car_update")
    @ResponseBody
    public CommonResult update(TCmsCar tCmsCar, HttpSession session){


        try{
            //对象是数据库中原来的对象
            TCmsCar old_byId = tCmsCarService.getById(tCmsCar.getId());

            if (!tCmsCar.getImgUrl().equals(old_byId.getImgUrl())){
                //如果修改了图片，则删除原来的图片
                String realPath = ResourceUtils.getURL("classpath:").getPath();
                String filePath = old_byId.getImgUrl();
                realPath = realPath.substring(1,realPath.length())+"static"+filePath;
                File file = new File(realPath);
                if(file.exists()){
                    file.delete();
                    System.out.println("删除了旧照片，地址是："+realPath);
                }
            }

            //设置当前系统时间为更新时间
            tCmsCar.setModifyTime(DateUtils.getNowTime());

            tCmsCar.setModifyUserId("b496894b89754a848e9b74ff66a05d44");
            System.out.println("要更新的对象是："+tCmsCar);
            return new CommonResult(200,"请求成功",tCmsCarService.updateById(tCmsCar));
        } catch (Exception e){
            e.printStackTrace();
            return new CommonResult(500,"请求失败");
        }

    }

    /*删除车票功能*/
    //根据id删除
    @GetMapping("/car_delete/{id}")
    @ResponseBody
    public CommonResult delete(@PathVariable("id") String id){
        //补代码时需要判断这个车票是否在使用，如果在使用要提示暂时不能删除
        TCmsCar byId = tCmsCarService.getById(id);
        byId.setDeleteStatus(1L);//删除状态
        tCmsCarService.updateById(byId);
        log.info("删除车票信息",id);
        tCmsCarService.updateById(id);
        return new CommonResult(200,"请求成功");
    }


    /*车票分页查询*/
    @RequestMapping("/car_list")
    public String list(@RequestParam(defaultValue = "1") Long pageNumber,
                       @RequestParam(defaultValue = "7") Long pageSize,
                       @RequestParam(defaultValue = "") String title,
                       Model model){
        //构造条件
        QueryWrapper<TCmsCar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS","0");
        queryWrapper.orderByDesc("ADD_TIME");
        //条件查询
        if (!"".equals(title)){
            queryWrapper.like("TITLE", title);
        }
        IPage page = tCmsCarService.page(new Page<TCmsCar>(pageNumber, pageSize), queryWrapper);
        //将page对象存入pageHelper对象中
        PageHelper<TCmsCar> pageHelper = new PageHelper<TCmsCar>(pageNumber,pageSize,page.getPages(),page.getTotal(),page.getRecords());
        model.addAttribute("pagerHelper", pageHelper);
        return "car/carList";
    }


    /*跳转前台车票页面*/
    /*前台分页查询*/
    @RequestMapping("/portal_car_list")
    public String carList(@RequestParam(defaultValue = "1") Long pageNumber,
                       @RequestParam(defaultValue = "7") Long pageSize,
                       @RequestParam(defaultValue = "") String title,
                       Model model){
        //构造条件
        QueryWrapper<TCmsCar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS","0");
        queryWrapper.orderByDesc("ADD_TIME");
        //条件查询
        if (!"".equals(title)){
            queryWrapper.like("TITLE", title);
        }
        IPage page = tCmsCarService.page(new Page<TCmsCar>(pageNumber, pageSize), queryWrapper);
        //将page对象存入pageHelper对象中
        PageHelper<TCmsCar> pageHelper = new PageHelper<TCmsCar>(pageNumber,pageSize,page.getPages(),page.getTotal(),page.getRecords());
        model.addAttribute("pagerHelper", pageHelper);
        return "portal/car";
    }


    /*前台车票跳转详情页面*/
    /*跳转详情页面*/
    @GetMapping("/portal_car_toview/{id}")
    public String toview(@PathVariable("id") String id,Model model){
        TCmsCar tCmsCar = tCmsCarService.getById(id);
        model.addAttribute("entity",tCmsCar);
        return "portal/carView";
    }


    /*跳转车票数据分析页面*/
    @RequestMapping ("/tocarData")
    public String carData(Model model){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("DELETE_STATUS",0);
        queryWrapper.select("count(id),STATE");
        queryWrapper.groupBy("STATE");
        queryWrapper.orderByAsc("STATE");

        List<Map<String, Object>> list = tCmsCarService.listMaps(queryWrapper);

        Map<String, Object> map0 = list.get(0);
        Object count_0 = map0.get("count(id)");

        Map<String, Object> map1 = list.get(1);
        Object count_1 = map1.get("count(id)");

        Map<String, Object> map2 = list.get(2);
        Object count_2 = map2.get("count(id)");

        model.addAttribute("count_0",count_0);
        model.addAttribute("count_1",count_1);
        model.addAttribute("count_2",count_2);

        return "data/carData";

    }

    /*车票预定*/

    @GetMapping("/user_createOrder")
    public String createOrder(@RequestParam("id") String carId, @RequestParam("product_type") int productType , Model model,HttpSession session){
        TCmsCar tCmsCar = tCmsCarService.getById(carId);

        try {
            TPzUser user = (TPzUser) session.getAttribute("user");

            TYwOrder tYwOrder = new TYwOrder();
            tYwOrder.setUserId(user.getId());
            tYwOrder.setUserName(user.getUserName());
            tYwOrder.setProductType(productType);
            tYwOrder.setProductId(carId);
            tYwOrder.setProductName(tCmsCar.getTitle());
            tYwOrder.setOrderTime(DateUtils.getNowTime());
            tYwOrder.setDeleteStatus(0L);
            tYwOrder.setState(0L);
            tYwOrderService.save(tYwOrder);
            model.addAttribute("msg","预定成功");


        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("msg","预定失败");
        }finally {
        }
        model.addAttribute("entity",tCmsCar);
        return "portal/carView";


    }
}
