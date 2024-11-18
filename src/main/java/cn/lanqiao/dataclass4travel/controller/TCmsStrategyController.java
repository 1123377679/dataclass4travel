package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.mapper.TCmsStrategyMapper;

import cn.lanqiao.dataclass4travel.pojo.TCmsStrategy;
import cn.lanqiao.dataclass4travel.service.TCmsStrategyService;
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

import static com.baomidou.mybatisplus.extension.toolkit.Db.page;

@Controller
@Slf4j
public class TCmsStrategyController {

    @Autowired
    private TCmsStrategyService tCmsStrategyService;

    @Autowired
    private TCmsStrategyMapper tCmsStrategyMapper;

    @RequestMapping("/strategy_list")
    public String list(@RequestParam(defaultValue = "1") long pageNumber,
                       @RequestParam(defaultValue = "7") long pageSize,
                       Model model) {

        QueryWrapper<TCmsStrategy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DELETE_STATUS", 0);
        queryWrapper.orderByDesc("ADD_TIME");
        IPage page = tCmsStrategyService.page(new Page<TCmsStrategy>(pageNumber, pageSize),queryWrapper);

        PageHelper<TCmsStrategy> strategyPageHelper = new PageHelper<TCmsStrategy>(pageNumber, pageSize, page.getPages(), page.getTotal(), page.getRecords());
        model.addAttribute("pagerHelper", strategyPageHelper);

        return "strategy/strategyList";
    }
}
