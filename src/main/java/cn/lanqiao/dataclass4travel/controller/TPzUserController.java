package cn.lanqiao.dataclass4travel.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.lanqiao.dataclass4travel.pojo.TCmsMessage;
import cn.lanqiao.dataclass4travel.pojo.TPzUser;
import cn.lanqiao.dataclass4travel.pojo.dto.UserLoginDTO;
import cn.lanqiao.dataclass4travel.service.ITCmsMessageService;
import cn.lanqiao.dataclass4travel.service.ITPzUserService;
import cn.lanqiao.dataclass4travel.utils.CommonResult;
import cn.lanqiao.dataclass4travel.utils.PageHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zyh
 * @since 2024-11-26
 */
@RequiredArgsConstructor
@Controller
public class TPzUserController {
    private final ITPzUserService tPzUserService;//
    private final ITCmsMessageService tCmsMessageService;

    @PostMapping("/user_register")
    @ResponseBody
    public CommonResult register(TPzUser tPzUser, Model model, HttpServletResponse response) throws IOException {
        TPzUser user = tPzUserService.lambdaQuery().eq(TPzUser::getUserName, tPzUser.getUserName()).one();
        if (user != null) {
            model.addAttribute("message", "用户名已存在");
            return new CommonResult(201, "用户名已存在");
        }

        //设置唯一id
        tPzUser.setId(UUID.randomUUID().toString().replace("-", ""));
//            设置添加时间
        tPzUser.setAddTime(LocalDateTime.now());
        System.out.println("要新增的对象是:" + tPzUser);
        tPzUserService.save(tPzUser);
        return new CommonResult(200, "注册成功");
    }

    /**
     * 旅客用户登录
     *
     * @param userLoginDTO
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/user_login")
    public String checkCaptcha(UserLoginDTO userLoginDTO, HttpSession session, Model model) {
        System.out.println("用户输入的验证码：" + userLoginDTO);
        String captchaCode = (String) session.getAttribute("captchaCode");
        System.out.println("用户输入的验证码：" + captchaCode);
        if (captchaCode != null && captchaCode.equalsIgnoreCase(userLoginDTO.getUserCode())) {
            // 验证成功
//            DTO转换为po
            TPzUser tPzUser = new TPzUser();
            BeanUtil.copyProperties(userLoginDTO, tPzUser);
//            调用用户登录
            TPzUser user = tPzUserService.lambdaQuery().eq(TPzUser::getUserName, userLoginDTO.getUserName()).eq(TPzUser::getPassWord, userLoginDTO.getPassWord()).one();
            if (user != null) {
                if (user.getState() == 0) {
                    model.addAttribute("message", "用户已被禁用");
                    return "portal/login";
                } else {
                    session.setAttribute("user", user);
                    session.setAttribute("userName", user.getUserName());
                    model.addAttribute("entity", user);
                    return "portal/userCenter";
                }
            }
            model.addAttribute("message", "用户名或密码错误");
            return "portal/login";
        } else {
            // 验证失败
            model.addAttribute("message", "验证码错误");
            return "portal/login";
        }
    }

    /**
     * 旅客用户注销
     *
     * @param session
     * @param response
     * @throws IOException
     */
    @GetMapping("user_logout")
    public void logout(HttpSession session, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        session.invalidate();
        response.getWriter().print("<script>alert('注销成功');location.href='/user_tologin'</script>");
    }

    @RequestMapping("/personInfo")
    public String toPersonInfo(HttpSession session, Model model) {
        TPzUser user = (TPzUser) session.getAttribute("user");
        model.addAttribute("entity", user);
        return "portal/personalIntro";
    }

    @GetMapping("user_tomyMessage")
    public String toMyMessage(Model model, HttpSession session) {
        TPzUser user = (TPzUser) session.getAttribute("user");
        Long count = tCmsMessageService.lambdaQuery().eq(TCmsMessage::getDeleteStatus, 0).eq(TCmsMessage::getUserId, user.getId()).count();
        model.addAttribute("messageCount", count);
        return "portal/myMessage";
    }

    /**
     * 跳转个人资料
     *
     * @param model
     * @param session
     * @return
     */
    @GetMapping("user_topersonInfo")
    public String toPersonInfo(Model model, HttpSession session) {
        TPzUser user = (TPzUser) session.getAttribute("user");
        model.addAttribute("entity", user);

        return "portal/personalIntro";
    }

    /**
     * 跳转到更新密码页面
     *
     * @return
     */
    @GetMapping("user_tochangePassword")
    public String toChangePassword() {
        return "portal/changePassword";
    }

    @PostMapping("/user_changePassword")
    @ResponseBody
    public CommonResult changePassword(String password, String newPassword, String checkPassword, HttpSession session) {
        TPzUser user = (TPzUser) session.getAttribute("user");
        if (StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(checkPassword)) {
            return new CommonResult(500, "要修改的密码不能为空!");
        } else if (!newPassword.equals(checkPassword)) {
            return new CommonResult(500, "两次输入的密码不一致!");
        } else if (!user.getPassWord().equals(password)) {
            return new CommonResult(500, "原密码输入错误!");
        } else if (newPassword.equals(password)) {
            return new CommonResult(500, "新密码不能与原密码一致!");
        } else {
            user.setPassWord(newPassword);
            boolean b = tPzUserService.updateById(user);
            if (!b) {
                return new CommonResult(500, "密码更新异常");
            } else {
                session.invalidate();
                return new CommonResult(200, "修改密码成功");
            }

        }
    }
    @GetMapping("/user_messageList")
    public String messageList(@RequestParam(defaultValue = "1") Long pageNumber,
                              @RequestParam(defaultValue = "7") Long pageSize,
            Model model, HttpSession session) {
        QueryWrapper queryWrapper = new QueryWrapper();
        TPzUser user = (TPzUser) session.getAttribute("user");
        queryWrapper.eq("USER_ID", user.getId());
        queryWrapper.eq("DELETE_STATUS", 0);
        queryWrapper.orderByDesc("ADD_TIME");
        IPage page = tCmsMessageService.page(new Page<>(pageNumber, pageSize), queryWrapper);
        PageHelper<TCmsMessage> pagerHelper = new PageHelper<TCmsMessage>(pageNumber, pageSize, page.getPages(), page.getTotal(), page.getRecords());
        model.addAttribute("pagerHelper", pagerHelper);
        return "portal/messageList";
    }
    /**
     * 用户数据分析
     */
    @RequestMapping("/touserData")
    public String toUserData(Model model) {
        Long count_0 = tPzUserService.lambdaQuery().eq(TPzUser::getDeleteStatus, 0).count();
        Long count_1 = tPzUserService.lambdaQuery().eq(TPzUser::getDeleteStatus, 1).count();
        model.addAttribute("count_0",count_0);
        model.addAttribute("count_1",count_1);
        return "data/userData";
    }

    /**
     * 修改密码
     * @param user
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/user_updateInfo")
    public String userUpdateInfo(@RequestBody TPzUser user,HttpSession session,Model model){
        boolean update = tPzUserService.lambdaUpdate().set(TPzUser::getUserName, user.getUserName())
                .set(TPzUser::getName, user.getName())
                .set(TPzUser::getLinkTel, user.getLinkTel())
                .set(TPzUser::getIcCode, user.getIcCode())
                .set(TPzUser::getProvince, user.getProvince())
                .eq(TPzUser::getId, user.getId())
                .update();
        if (update){
            TPzUser byId = tPzUserService.getById(user.getId());
            session.removeAttribute("user");
            session.setAttribute("user",byId);
            model.addAttribute("entity", byId);
//            model.addAttribute("message", "修改成功");
        }
        return "portal/personalIntro";
    }
}
