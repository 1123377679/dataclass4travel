package cn.lanqiao.dataclass4travel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: 李某人
 * @Date: 2024/11/12/22:41
 * @Description:通用的控制器来跳转页面
 */
@Controller
public class CommonController {
    /**
     * 1.跳转登录页面
     * @return
     */
    @RequestMapping("/login")
    public String toLoginPage(){
        return "login.html";
    }

    /**
     * 跳转用户首页
     * @return
     */
    @RequestMapping("/index")
    public String toIndexPage(){
        return "portal/index.html";
    }

    /**
     * 跳转用户登录界面
     * @return
     */
    @RequestMapping("/user_tologin")
    public String toUserLogin(){
        return "portal/login.html";
    }

    /**
     * 跳转用户注册界面
     * @return
     */
    @RequestMapping("/user_toregister")
    public String toUserRegister(){
        return "portal/register.html";
    }

    /**
     * 跳转个人中心
     * @return
     */
    @GetMapping("/user_tocenter")
    public String toUserCenter(){
        return "portal/userCenter.html";
    }

}
