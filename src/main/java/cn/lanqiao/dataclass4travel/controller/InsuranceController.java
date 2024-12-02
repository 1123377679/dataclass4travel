package cn.lanqiao.dataclass4travel.controller;


import cn.lanqiao.dataclass4travel.pojo.Hotel;
import cn.lanqiao.dataclass4travel.pojo.TCmsCar;
import cn.lanqiao.dataclass4travel.pojo.TCmsInsurance;
import cn.lanqiao.dataclass4travel.pojo.TPzAdminUser;
import cn.lanqiao.dataclass4travel.service.InsuranceService;
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

@Controller
@Slf4j
public class InsuranceController {
    @Autowired
    private InsuranceService insuranceService;

    /**
     * 01.保险分页查询
     */
    @RequestMapping("/insurance_list")
    public String insuranceList(@RequestParam(defaultValue = "1") Long pageNumber,
                                @RequestParam(defaultValue = "7") Long pageSize,
                                Model model){
        QueryWrapper<TCmsInsurance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS","0");
        queryWrapper.orderByDesc("ADD_TIME");
        IPage page = insuranceService.page(new Page<TCmsInsurance>(pageNumber, pageSize), queryWrapper);
        //将page对象存入pageHelper对象中
        PageHelper<TCmsInsurance> pageHelper = new PageHelper<TCmsInsurance>(pageNumber,pageSize,page.getPages(),page.getTotal(),page.getRecords());
        model.addAttribute("pagerHelper", pageHelper);
        //返回
        return "insurance/insuranceList";
    }

    /**
    *跳转新增页面
    */
    @RequestMapping("/insurance_toadd")
    public String toAdd(){
        return "insurance/insuranceAdd";
    }

    /**
     * 新增功能
     */
    @PostMapping("/insurance_add")
    @ResponseBody
    public CommonResult add(TCmsInsurance tCmsInsurance, HttpSession session){
        try {
            // 当前时间
            String nowTime = DateUtils.getNowTime();
            tCmsInsurance.setAddTime(nowTime);
            // 获取当前管理员信息
            TPzAdminUser addAdmin = (TPzAdminUser) session.getAttribute("admin");
            // 没什么必要，以防万一
            if (addAdmin == null) {
                return new CommonResult(304, "用户未登录或Session已过期");
            }
            // 设置添加人的id
            tCmsInsurance.setAddUserId(addAdmin.getId());
            // 操作数据库进行添加
            System.out.println("要新增的对象是:" + tCmsInsurance);
            Object savedHotel = insuranceService.save(tCmsInsurance);
            if (savedHotel == null) {
                return new CommonResult(304, "保存失败");
            }
            // 返回成功结果
            return new CommonResult(200, "请求成功", savedHotel);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(500, "请求异常: " + e.getMessage());  // 返回500状态码并附带异常信息
        }

    }

    /**
     * 跳转详情页面
     * */
    @GetMapping("insurance_todetail/{id}")
    public String edit(@PathVariable String id, Model model){
        TCmsInsurance insurance = insuranceService.getById(id);
        model.addAttribute("entity",insurance);
        return "insurance/insuranceView";
    }

    /**
     * 更新页面
     */
    @GetMapping("/insurance_toEdit/{id}")
    public String toEdit(@PathVariable("id") String id,Model model) {
        //获取id
        TCmsInsurance byId = insuranceService.getById(id);
        //回显数据的对象
        model.addAttribute("entity",byId);
        return "insurance/insuranceEdit";
    }
    /**
     * 更新异步
     * */
    @PostMapping("/insurance_update")
    @ResponseBody
    public CommonResult edit(TCmsInsurance tCmsInsurance, HttpSession session){
        try {
            // 是数据库中原对象
            TCmsInsurance byId = insuranceService.getById(tCmsInsurance.getId());
            // 如果上传了新照片，就需要删除老照片
            // 所以if来判断旧新的图片地址是不是一样的，不一样，就删掉老照片
            if (!tCmsInsurance.getImgUrl().equals(byId.getImgUrl())){
                String realPath = ResourceUtils.getURL("classpath:").getPath();
                String filePath = byId.getImgUrl();
                realPath = realPath.substring(1,realPath.length())+"static"+filePath;
                File f = new File(realPath);
                if (f.exists()){
                    f.delete();
                    System.out.println("删除了老照片，地址是："+realPath);
                }
            }
            //设置当前系统时间
            byId.setModifyTime(DateUtils.getNowTime());
            //设置新增酒店的人
            TPzAdminUser addAdmin = (TPzAdminUser) session.getAttribute("admin");
            if (addAdmin == null) {
                return new CommonResult(304, "用户未登录或Session已过期");
            }
            // 设置添加人的id
            byId.setAddUserId(addAdmin.getId());
            System.out.println("要更新的对象是："+byId);
            return new CommonResult(200,"请求成功",insuranceService.updateById(tCmsInsurance));

        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult(500,"请求失败");
        }
    }

    /**
     * 删除保险功能
     * 根据id删除
     */
    @GetMapping("/insurance_delete/{id}")
    @ResponseBody
    public CommonResult delete(@PathVariable("id") String id){
        //补代码时需要判断这个车票是否在使用，如果在使用要提示暂时不能删除
        TCmsInsurance byId = insuranceService.getById(id);
        byId.setDeleteStatus(1L);//删除状态
        insuranceService.updateById(byId);
        log.info("删除车票信息",id);
        insuranceService.updateById(byId);
        return new CommonResult(200,"请求成功");
    }


    /*跳转前台保险页面*/
    /*前台分页查询*/
    @RequestMapping("/portal_insurance_list")
    public String carList(@RequestParam(defaultValue = "1") Long pageNumber,
                          @RequestParam(defaultValue = "7") Long pageSize,
                          @RequestParam(defaultValue = "") String title,
                          Model model){
        //构造条件
        QueryWrapper<TCmsInsurance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS","0");
        queryWrapper.orderByDesc("ADD_TIME");
        //条件查询
        if (!"".equals(title)){
            queryWrapper.like("TITLE", title);
        }
        IPage page = insuranceService.page(new Page<TCmsInsurance>(pageNumber, pageSize), queryWrapper);
        //将page对象存入pageHelper对象中
        PageHelper<TCmsInsurance> pageHelper = new PageHelper<TCmsInsurance>(pageNumber,pageSize,page.getPages(),page.getTotal(),page.getRecords());
        model.addAttribute("pagerHelper", pageHelper);
        return "portal/insurance";
    }
}
