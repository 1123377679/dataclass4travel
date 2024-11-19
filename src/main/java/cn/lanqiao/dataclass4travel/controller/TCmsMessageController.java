package cn.lanqiao.dataclass4travel.controller;


import cn.lanqiao.dataclass4travel.pojo.TCmsMessage;
import cn.lanqiao.dataclass4travel.pojo.TPzAdminUser;
import cn.lanqiao.dataclass4travel.service.ITCmsMessageService;
import cn.lanqiao.dataclass4travel.utils.PageHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zyh
 * @since 2024-11-18
 */
@Controller
@RequiredArgsConstructor
public class TCmsMessageController {
    private final ITCmsMessageService tCmsMessageService;

    @GetMapping("/message_list")
    public String list(@RequestParam(defaultValue = "1") Long pageNumber,
                       @RequestParam(defaultValue = "7") Long pageSize,
                       Model model) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("DELETE_STATUS", 0);
        queryWrapper.orderByDesc("ADD_TIME");
        IPage page = tCmsMessageService.page(new Page<>(pageNumber, pageSize), queryWrapper);
        PageHelper<TCmsMessage> pagerHelper = new PageHelper<TCmsMessage>(pageNumber, pageSize, page.getPages(), page.getTotal(), page.getRecords());
        model.addAttribute("pagerHelper", pagerHelper);
        return "message/messageList";
    }
}
