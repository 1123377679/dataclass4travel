package cn.lanqiao.dataclass4travel.controller;


import cn.lanqiao.dataclass4travel.pojo.TCmsCar;
import cn.lanqiao.dataclass4travel.service.TCmsCarService;
import cn.lanqiao.dataclass4travel.utils.CommonResult;
import cn.lanqiao.dataclass4travel.utils.DateUtils;
import cn.lanqiao.dataclass4travel.utils.PageHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public CommonResult add(TCmsCar tCmsCar){
            //设置当前系统时间
            tCmsCar.setAddTime(DateUtils.getNowTime());

            tCmsCar.setAddUserId("1");
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
