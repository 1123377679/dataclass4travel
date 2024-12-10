package cn.lanqiao.dataclass4travel.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.lanqiao.dataclass4travel.pojo.TCmsMessage;
import cn.lanqiao.dataclass4travel.pojo.TPzAdminUser;
import cn.lanqiao.dataclass4travel.pojo.TPzUser;
import cn.lanqiao.dataclass4travel.service.ITCmsMessageService;
import cn.lanqiao.dataclass4travel.utils.CommonResult;
import cn.lanqiao.dataclass4travel.utils.PageHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 分页条件查询
     * @param pageNumber
     * @param pageSize
     * @param userName
     * @param model
     * @return
     */

    @RequestMapping("/message_list")
    public String list(@RequestParam(defaultValue = "1") Long pageNumber,
                       @RequestParam(defaultValue = "7") Long pageSize,
                       @RequestParam(defaultValue = "") String userName,
                       Model model) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("DELETE_STATUS", 0);
        queryWrapper.orderByDesc("ADD_TIME");
        if (!"".equals(userName)){
            queryWrapper.like("USER_NAME", userName);
        }
        IPage page = tCmsMessageService.page(new Page<>(pageNumber, pageSize), queryWrapper);
        PageHelper<TCmsMessage> pagerHelper = new PageHelper<TCmsMessage>(pageNumber, pageSize, page.getPages(), page.getTotal(), page.getRecords());
        model.addAttribute("pagerHelper", pagerHelper);
        return "message/messageList";
    }

    /**
     * 删除留言
     * @param id
     * @return
     */
    @GetMapping("/messageDelete/{id}")
    @ResponseBody
    public CommonResult delete(@PathVariable String id){
        boolean update = tCmsMessageService.lambdaUpdate().set(TCmsMessage::getDeleteStatus, 1).eq(TCmsMessage::getId, id).update();
        return new CommonResult(200, "删除成功");
    }

    /**
     * 跳转详情页面
     * @return
     */
    @GetMapping("message_View/{id}")
    public String toDetail(@PathVariable String id , Model model){
        TCmsMessage byId = tCmsMessageService.getById(id);
        model.addAttribute("entity", byId);
        return "message/messageView";
    }
    @PostMapping("user_leaveMessage")
    @ResponseBody
    public CommonResult leaveMessage(@RequestBody TCmsMessage tCmsMessage, HttpSession session){
        TPzUser user = (TPzUser) session.getAttribute("user");
        BeanUtil.copyProperties(user, tCmsMessage);
        //设置唯一id
        tCmsMessage.setId(UUID.randomUUID().toString().replace("-", ""));
        tCmsMessage.setUserId(user.getId());
        boolean save = tCmsMessageService.save(tCmsMessage);
        if (save){
            return new CommonResult(200, "留言成功");
        }
        return new CommonResult(500, "留言失败");
    }
    @PostMapping("manager/messageSave")
    @ResponseBody
    public CommonResult save(@RequestBody TCmsMessage tCmsMessage, Model model){
        boolean update = tCmsMessageService.lambdaUpdate().set(TCmsMessage::getReplyContent, tCmsMessage.getReplyContent()).eq(TCmsMessage::getId, tCmsMessage.getId()).update();
        TCmsMessage byId = tCmsMessageService.getById(tCmsMessage.getId());
        if (update){
        model.addAttribute("entity", byId);
        model.addAttribute("message", "回复成功");
            return new CommonResult(200, "回复成功");
        }else {
            model.addAttribute("message", "回复失败");
            return new CommonResult(500, "回复失败");
        }

    }
    /**
     * 跳转详情页面
     * @return
     */
    @GetMapping("user_messageView/{id}")
    public String toUserDetail(@PathVariable String id , Model model){
        TCmsMessage byId = tCmsMessageService.getById(id);
        model.addAttribute("userMessage", byId);
        return "portal/messageView";
    }
}
