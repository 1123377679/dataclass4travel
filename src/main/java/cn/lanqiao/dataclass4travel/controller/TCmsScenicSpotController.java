package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.mapper.TCmsScenicSpotMapper;
import cn.lanqiao.dataclass4travel.utils.CommonResult;
import cn.lanqiao.dataclass4travel.utils.DateUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class TCmsScenicSpotController {

    @Autowired
    private TCmsScenicSpotService tCmsScenicSpotService;

    @Autowired
    private TCmsScenicSpotMapper tCmsScenicSpotMapper;

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
    @RequestMapping("/scenicSpot_toadd")
    public String toAdd(){
        return "scenicSpot/scenicSpotAdd";
    }


    @RequestMapping("/scenicSpot_add")
    @ResponseBody
    public CommonResult add(TCmsScenicSpot tCmsScenicSpot, HttpSession session){
        try {
            //设置当前系统时间
            String nowTime = DateUtils.getNowTime();
            tCmsScenicSpot.setAddTime(nowTime);
            //设置添加人的id,从session中获取addUserId
            TCmsScenicSpot admin = (TCmsScenicSpot) session.getAttribute("admin");
            tCmsScenicSpot.setAddUserId(admin.getAddUserId());
            //操作数据库进行添加
            System.out.println("要新增的对象是:"+tCmsScenicSpot);
            //需要响应类
            return new CommonResult(200,"请求成功",tCmsScenicSpotService.save(tCmsScenicSpot));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(304,"请求异常");
        }
    }
}
