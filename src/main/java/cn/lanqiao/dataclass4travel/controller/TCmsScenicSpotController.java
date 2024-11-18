package cn.lanqiao.dataclass4travel.controller;

import org.springframework.ui.Model;
import cn.lanqiao.dataclass4travel.pojo.TCmsScenicSpot;
import cn.lanqiao.dataclass4travel.service.TCmsScenicSpotService;
import cn.lanqiao.dataclass4travel.utils.PageHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TCmsScenicSpotController {
    @Autowired
    private TCmsScenicSpotService tCmsScenicSpotService;

    /**
     * 分页查询
     */
    @RequestMapping("/scenicSpot_list")
    public String list(@RequestParam(defaultValue = "1") Long pageNumber,
                @RequestParam(defaultValue = "7") Long pageSize,
                Model model){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("DELETE_STATUS",0);
        queryWrapper.orderByDesc("ADD_TIME");
        IPage page = tCmsScenicSpotService.page(new Page<TCmsScenicSpot>(pageNumber, pageSize), queryWrapper);
        // 封装
        PageHelper<TCmsScenicSpot> pagerHelper = new PageHelper<TCmsScenicSpot>(pageNumber, pageSize, page.getPages(), page.getTotal(), page.getRecords());
        model.addAttribute("pagerHelper",pagerHelper);
        return "scenicSpot/scenicSpotList.html";
    }
}
