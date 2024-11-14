package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.pojo.TPzAdminUser;
import cn.lanqiao.dataclass4travel.service.TPzAdminUserService;
import cn.lanqiao.dataclass4travel.utils.CommonResult;
import cn.lanqiao.dataclass4travel.utils.DateUtils;
import cn.lanqiao.dataclass4travel.utils.PageHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: 李某人
 * @Date: 2024/11/12/22:47
 * @Description:
 */
@Controller
public class TPzAdminUserController {
    @Autowired
    private TPzAdminUserService tPzAdminUserService;

    /**
     * 01-管理员登录
     */
    @RequestMapping("/adminUser_login")
    public String toLogin(TPzAdminUser tPzAdminUser, HttpSession session, Model model){
        QueryWrapper<TPzAdminUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_NAME",tPzAdminUser.getUserName());
        queryWrapper.eq("PASSWORD",tPzAdminUser.getPassword());
        queryWrapper.eq("DELETE_STATUS","0");
        //MyBatisPlus实现登录
        TPzAdminUser loginTPzAdminUser = tPzAdminUserService.getOne(queryWrapper);
        if (loginTPzAdminUser !=null){
            //状态必须是1
            if (loginTPzAdminUser.getState() == 1){
                session.setAttribute("admin",loginTPzAdminUser);
                //登录成功
                return "index.html";
            }else {
                model.addAttribute("message","管理员账号已被禁用");
                return "login.html";
            }
        }else {
            model.addAttribute("message","账号或密码错误");
            return "login.html";
        }
    }

    /**
     * 02-分页查询列表
     * @return
     */
    @RequestMapping("/admin_list")
    public String list(@RequestParam(defaultValue = "1") Long pageNumber,
                       @RequestParam(defaultValue = "7") Long pageSize,
                       Model model){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("DELETE_STATUS",0);
        queryWrapper.orderByDesc("ADD_TIME");
        IPage page = tPzAdminUserService.page(new Page<TPzAdminUser>(pageNumber, pageSize), queryWrapper);
        //封装工具类
        PageHelper<TPzAdminUser> pagerHelper=new PageHelper<TPzAdminUser>(pageNumber,pageSize,page.getPages(),page.getTotal(),page.getRecords());
        model.addAttribute("pagerHelper",pagerHelper);
        return "admin/adminList";
    }

    /**
     * 03-跳转新增页面
     */
    @RequestMapping("/admin_toadd")
    public String toAdd(){
        return "admin/adminAdd";
    }
    /**
     * 04-新增功能(异步请求)
     */
    @RequestMapping("/admin_add")
    @ResponseBody
    public CommonResult add(TPzAdminUser tPzAdminUser,HttpSession session){
        try {
            //设置当前系统时间
            String nowTime = DateUtils.getNowTime();
            tPzAdminUser.setAddTime(nowTime);
            //设置添加人的id,从session中获取addUserId
            TPzAdminUser admin = (TPzAdminUser) session.getAttribute("admin");
            tPzAdminUser.setAddUserId(admin.getAddUserId());
            //操作数据库进行添加
            System.out.println("要新增的对象是:"+tPzAdminUser);
            //需要响应类
            return new CommonResult(200,"请求成功",tPzAdminUserService.save(tPzAdminUser));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(304,"请求异常");
        }
    }
}
