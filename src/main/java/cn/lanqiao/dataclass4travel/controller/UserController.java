package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.pojo.User;
import cn.lanqiao.dataclass4travel.service.UserService;
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
import org.springframework.web.bind.annotation.*;


/**
 * @BelongsProject: TravelDream
 * @BelongsPackage: com.travel.controller
 * @CreateTime: 2021-05-22 10:19
 * @Description: TODO
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 01-分页查询列表
     * @return
     */
    @RequestMapping("manager_user_list")
    public String list(@RequestParam(defaultValue = "1") Long pageNumber,
                       @RequestParam(defaultValue = "7") Long pageSize,
                       @RequestParam(defaultValue = "") String name, Model model){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("DELETE_STATUS",0);
        queryWrapper.orderByDesc("ADD_TIME");
        if(!"".equals(name)){
            queryWrapper.like("NAME",name);
        }
        IPage page = userService.page(new Page<User>(pageNumber, pageSize), queryWrapper);
        //封装工具类
        PageHelper<User> pagerHelper=new PageHelper<User>(pageNumber,pageSize,page.getPages(),page.getTotal(),page.getRecords());
        model.addAttribute("pagerHelper",pagerHelper);
        model.addAttribute("name",name); //查询数据回显
        return "user/allUsers";
    }

    /**
     * 02-打开一个新增旅游路线页面
     * @return
     */
    @GetMapping("manager_user_toadd")
    public String toadd(){
        return "user/userAdd";
    }

    /**
     * 03-新增(异步新增)
     * @return
     */
    @ResponseBody
    @PostMapping("manager_user_add")
    public CommonResult add(User user, HttpSession session){
        //设置当前系统时间
        user.setAddTime(DateUtils.getNowTime());
        //设置新增路线的人(暂时写死，后期从session中获取)
        user.setAddUserId("b496894b89754a848e9b74ff66a05d44");
        System.out.println("要新增的对象是:"+user);
        return new CommonResult(200,"请求成功",userService.save(user));
    }

    /**
     * 04-跳转详情页面
     * @return
     */
    @GetMapping("manager_user_todetail/{id}")
    public String todetail(@PathVariable("id") String id,Model model){
        User user = userService.getById(id);
        model.addAttribute("entity",user);
        return "user/userView";
    }

    /**
     * 05-跳转到更新页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("manager_user_toEdit/{id}")
    public String toEdit(@PathVariable("id") String id,Model model){
        User user = userService.getById(id);
        model.addAttribute("entity",user); //回显数据的对象
        return "user/userEdit";
    }


    /**
     * 06-更新(异步更新)
     * @return
     */
    @ResponseBody
    @PostMapping("manager_user_update")
    public CommonResult update(User user, HttpSession session) {
        //设置当前系统时间为更新事件
        user.setModifyTime(DateUtils.getNowTime());
        //设置新增路线的人(暂时写死，后期从session中获取)
        user.setModifyUserId("b496894b89754a848e9b74ff66a05d44");
        System.out.println("要更新的对象是:"+user);
        return new CommonResult(200,"请求成功",userService.updateById(user));
    }

    /**
     * 07-删除用户
     * @param id
     * @return
     */
    @GetMapping("manager_user_delete/{id}")
    @ResponseBody
    public CommonResult delete(@PathVariable("id") String id){
        //TODO:后期需要补代码判断这个旅游路线是否在使用用，如果在使用中提示暂时不能删除
        User user = userService.getById(id);
        user.setDeleteStatus(1);//删除状态
        userService.updateById(user);
        return new CommonResult(200,"请求成功");
    }
}
