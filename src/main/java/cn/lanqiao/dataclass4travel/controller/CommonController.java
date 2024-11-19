package cn.lanqiao.dataclass4travel.controller;

import org.springframework.stereotype.Controller;
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
}
