package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.pojo.MainData;
import cn.lanqiao.dataclass4travel.pojo.Province;
import cn.lanqiao.dataclass4travel.pojo.User;
import cn.lanqiao.dataclass4travel.service.ProvinceService;
import cn.lanqiao.dataclass4travel.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 李某人
 * @Date: 2024/11/12/22:41
 * @Description:通用的控制器来跳转页面
 */
@Controller
public class CommonController {
    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private UserService userService;
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



    /**
     * 中国地图数据分析
     * @return
     */
    @RequestMapping("/tomain")
    public String tomain(Model model) throws Exception{
        List<MainData> mainDataList=new ArrayList<>();
        List<Province> provinces = provinceService.list();

        for (Province province : provinces) {
            long id = province.getId();
            int count = (int) userService.count(new QueryWrapper<User>().eq("PROVINCE", id).eq("DELETE_STATUS", 0));
            MainData data=new MainData(province.getProvince(),count);
            mainDataList.add(data);
        }

        ObjectMapper objectMapper=new ObjectMapper();
        String datas = objectMapper.writeValueAsString(mainDataList);
        System.out.println(datas);

        model.addAttribute("datas",datas);

        return "data/main";
    }

    /**
     * 注意事项
     * @return
     */
    @GetMapping("/portal_attention")
    public String toPortalAttention(){
        return "/portal/attention";
    }

}
