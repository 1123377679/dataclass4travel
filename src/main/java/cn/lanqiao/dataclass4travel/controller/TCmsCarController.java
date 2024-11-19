package cn.lanqiao.dataclass4travel.controller;


import cn.lanqiao.dataclass4travel.pojo.TCmsCar;
import cn.lanqiao.dataclass4travel.service.TCmsCarService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class TCmsCarController {

    @Autowired
    private TCmsCarService tCmsCarService;


    /*跳转新增车票页面*/
    @RequestMapping("/car_toadd")
    public String toAdd(){
        return "car/carAdd";
    }


    /*新增车票功能(异步请求)*/
    @RequestMapping("/car_add")
    @ResponseBody
    public CommonResult add(TCmsCar tCmsCar, HttpSession session){
        try {
            //需要设置当前系统的时间
            String nowTime = DateUtils.getNowTime();
            tCmsCar.setAddTime(nowTime);
            //车票管理不需要添加人的id
//            //设置添加人的id，从session中获取addUserId
//            TCmsCar admin = (TCmsCar) session.getAttribute("admin");
//            tCmsCar.setAddUserId(admin.getAddUserId());
            //操作数据库进行添加
            System.out.println("要新增的对象是:" + tCmsCar);
            //需要响应类
            return new CommonResult(200,"请求成功",tCmsCarService.save(tCmsCar));
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult(304,"请求失败",false);
        }

    }


//    /*删除车票功能*/
//    @DeleteMapping("/car_delete")
//    @ResponseBody
//    public CommonResult delete(Long carId){
//
//    }


    /*车票分页查询*/
    @RequestMapping("/car_list")
    public String list(@RequestParam(defaultValue = "1") Long pageNumber,
                       @RequestParam(defaultValue = "7") Long pageSize,
                       Model model){
        //构造条件
        QueryWrapper<TCmsCar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS","0");
        queryWrapper.orderByDesc("ADD_TIME");
        IPage page = tCmsCarService.page(new Page<TCmsCar>(pageNumber, pageSize), queryWrapper);
        //将page对象存入pageHelper对象中
        PageHelper<TCmsCar> pageHelper = new PageHelper<TCmsCar>(pageNumber,pageSize,page.getPages(),page.getTotal(),page.getRecords());
        model.addAttribute("pagerHelper", pageHelper);
        return "car/carList";
    }
}
