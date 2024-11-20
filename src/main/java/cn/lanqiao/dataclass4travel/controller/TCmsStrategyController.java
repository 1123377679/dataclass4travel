package cn.lanqiao.dataclass4travel.controller;

import cn.lanqiao.dataclass4travel.mapper.TCmsStrategyMapper;



import cn.lanqiao.dataclass4travel.pojo.TCmsStrategy;
import cn.lanqiao.dataclass4travel.pojo.TPzAdminUser;
import cn.lanqiao.dataclass4travel.service.TCmsStrategyService;
import cn.lanqiao.dataclass4travel.utils.CommonResult;
import cn.lanqiao.dataclass4travel.utils.DateUtils;
import cn.lanqiao.dataclass4travel.utils.PageHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;


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

    @RequestMapping("/strategy_toAdd")
    public String toAdd(){
        return "strategy/strategyAdd";
    }

    @PostMapping("/strategy_Add")
    @ResponseBody
    public CommonResult add(TCmsStrategy tCmsStrategy, HttpSession session){
        try {

            String nowTime = DateUtils.getNowTime();
            tCmsStrategy.setAddTime(nowTime);
            // 获取当前管理员信息
            TPzAdminUser addAdmin = (TPzAdminUser) session.getAttribute("admin");

            if (addAdmin == null) {
                return new CommonResult(304, "用户未登录或Session已过期");
            }
            // 设置添加人的id
            tCmsStrategy.setAddUserId(addAdmin.getId());
            // 操作数据库进行添加
            System.out.println("要新增的对象是:" + tCmsStrategy);
            Object savedTCmsStrategy = tCmsStrategyService.save(tCmsStrategy);
            if (savedTCmsStrategy == null) {
                return new CommonResult(304, "保存失败");
            }
            // 返回成功结果
            return new CommonResult(200, "请求成功", savedTCmsStrategy);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(500, "请求异常: " + e.getMessage());  // 返回500状态码并附带异常信息
        }
    }
    @GetMapping("/strategy_View/{id}")
    public String toDetail(@PathVariable("id") String id,Model model){
        TCmsStrategy byId = tCmsStrategyService.getById(id);
        model.addAttribute("entity",byId);
        return "strategy/strategyView";
    }

    @GetMapping("/strategy_toupdate/{id}")
    public String toEdit(@PathVariable("id") String id,Model model){
        TCmsStrategy byId = tCmsStrategyService.getById(id);
        model.addAttribute("entity",byId);
        return "strategy/strategyEdit";
    }


    @ResponseBody
    @PostMapping("strategy_update")
    public CommonResult update(TCmsStrategy tCmsStrategy,HttpSession session){
        try {
            // 是数据库中原对象
            TCmsStrategy old = tCmsStrategyService.getById(tCmsStrategy.getId());
            // 如果上传了新照片，就需要删除老照片
            // 所以if来判断旧新的图片地址是不是一样的，不一样，就删掉老照片
            if (!tCmsStrategy.getImgUrl().equals(old.getImgUrl())){
                String realPath = ResourceUtils.getURL("classpath:").getPath();
                String filePath = old.getImgUrl();
                realPath = realPath.substring(1,realPath.length())+"static"+filePath;
                File f = new File(realPath);
                if (f.exists()){
                    f.delete();
                    System.out.println("删除了老图片，地址是："+realPath);
                }
            }
            //设置当前系统时间
            tCmsStrategy.setModifyTime(DateUtils.getNowTime());
            TPzAdminUser addAdmin = (TPzAdminUser) session.getAttribute("admin");
            if (addAdmin == null) {
                return new CommonResult(304, "用户未登录或Session已过期");
            }
            // 设置添加人的id
            tCmsStrategy.setAddUserId(addAdmin.getId());
            System.out.println("要更新的对象是："+tCmsStrategy);
            return new CommonResult(200,"请求成功",tCmsStrategyService.updateById(tCmsStrategy));

        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult(500,"请求失败");
        }
    }
    @GetMapping("strategy_delete/{id}")
    @ResponseBody
    public CommonResult delete(TCmsStrategy tCmsStrategy,@PathVariable("id") String id){

        TCmsStrategy byId = tCmsStrategyService.getById(id);
        tCmsStrategy.setDeleteStatus(1L);
        tCmsStrategyService.updateById(byId);
        return new CommonResult(200,"请求成功");
    }
}
