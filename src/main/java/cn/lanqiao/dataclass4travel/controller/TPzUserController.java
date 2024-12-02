package cn.lanqiao.dataclass4travel.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.lanqiao.dataclass4travel.pojo.TCmsMessage;
import cn.lanqiao.dataclass4travel.pojo.TPzUser;
import cn.lanqiao.dataclass4travel.pojo.dto.UserLoginDTO;
import cn.lanqiao.dataclass4travel.service.ITCmsMessageService;
import cn.lanqiao.dataclass4travel.service.ITPzUserService;
import cn.lanqiao.dataclass4travel.utils.CommonResult;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
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
    public String register(TPzUser tPzUser, Model model, HttpServletResponse response) throws IOException {
            TPzUser user = tPzUserService.lambdaQuery().eq(TPzUser::getUserName, tPzUser.getUserName()).one();
            if (user != null){
                model.addAttribute("message", "用户名已存在");
                return "portal/register";
            }

            //设置唯一id
            tPzUser.setId(UUID.randomUUID().toString().replace("-",""));
//            设置添加时间
            tPzUser.setAddTime(LocalDateTime.now());
            System.out.println("要新增的对象是:" + tPzUser);
            tPzUserService.save(tPzUser);
            response.getWriter().print("<script>alert('注册成功');</script>");
            return "portal/login";
    }

    /**
     * 旅客用户登录
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
            BeanUtil.copyProperties(userLoginDTO,tPzUser);
//            调用用户登录
            TPzUser user = tPzUserService.lambdaQuery().eq(TPzUser::getUserName, userLoginDTO.getUserName()).eq(TPzUser::getPassWord, userLoginDTO.getPassWord()).one();
            if (user != null){
                if (user.getState() == 0){
                    model.addAttribute("message", "用户已被禁用");
                    return "portal/login";
                }else {
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
     * @param session
     * @param response
     * @throws IOException
     */
    @GetMapping("user_logout")
    public void logout(HttpSession session,HttpServletResponse response) throws IOException{
            response.setContentType("text/html;charset=utf-8");
        session.invalidate();
        response.getWriter().print("<script>alert('注销成功');location.href='/user_tologin'</script>");
    }
    @RequestMapping("/personInfo")
    public String toPersonInfo(HttpSession session , Model model){
        TPzUser user = (TPzUser) session.getAttribute("user");
        model.addAttribute("entity",user);
        return "portal/personalIntro";
    }
    @GetMapping("user_tomyMessage")
    public String toMyMessage(Model model,HttpSession session){
        TPzUser user =(TPzUser) session.getAttribute("user");
        Long count = tCmsMessageService.lambdaQuery().eq(TCmsMessage::getDeleteStatus, 0).eq(TCmsMessage::getUserId, user.getId()).count();
        model.addAttribute("messageCount",count);
        return "portal/myMessage";
    }

    /**
     * 跳转个人资料
     * @param model
     * @param session
     * @return
     */
    @GetMapping("user_topersonInfo")
    public String toPersonInfo(Model model,HttpSession session){
        TPzUser user =(TPzUser) session.getAttribute("user");
        model.addAttribute("entity",user);
        return "portal/personalIntro";
    }

    /**
     * 跳转到更新密码页面
     * @return
     */
    @GetMapping("user_tochangePassword")
    public String toChangePassword(){
        return "portal/changePassword";
    }
}
