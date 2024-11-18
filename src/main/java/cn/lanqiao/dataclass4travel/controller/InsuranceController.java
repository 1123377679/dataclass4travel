package cn.lanqiao.dataclass4travel.controller;


import cn.lanqiao.dataclass4travel.pojo.TCmsInsurance;
import cn.lanqiao.dataclass4travel.pojo.TCmsTravelRoute;
import cn.lanqiao.dataclass4travel.service.InsuranceService;
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
import org.springframework.web.bind.annotation.ResponseBody;

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
        return "insurance/insuranceList";
    }
}
